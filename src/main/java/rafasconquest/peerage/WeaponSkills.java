package rafasconquest.peerage;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.bridge.RecruitsBattle;
import rafasconquest.gear.Armoury;

/**
 * A player's weapon skills (Light, Medium, Heavy) at work. Each level of the skill for the weapon in hand gives, in a
 * cycle of three: a level of Haste to the swing, then a level of Strength, then a level of knockback, then Haste again.
 * Haste is on whenever the weapon is held; the Strength and knockback land only on battlefield soldiers. Also feeds
 * Minecraft's XP into the player's own level ({@link PlayerStats}).
 */
@EventBusSubscriber(modid = RafasConquest.MOD_ID)
public final class WeaponSkills {
    private static final ResourceLocation HASTE = ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, "weapon_skill_haste");
    /** As the potions: Haste +10% attack speed a level, Strength +3 damage a level, a knockback level half a block's push. */
    private static final double HASTE_PER_LEVEL = 0.1;
    private static final float STRENGTH_PER_LEVEL = 3;
    private static final float KNOCKBACK_PER_LEVEL = 0.5f;
    /** Strength at which a two-handed weapon can be held in one hand at full strength, and heavy shields don't slow. */
    public static final int ONE_HAND_HEAVY = 44;
    /** Fortitude at which jousting armour no longer slows. */
    public static final int JOUST_UNSLOWED = 30;
    /** The client's copies for its own player (sent on login and when Strength or Fortitude changes). */
    public static boolean localUnhindered;
    public static boolean localHardy;

    /** Whether jousting armour leaves this player unslowed: Fortitude {@link #JOUST_UNSLOWED} only (Strength doesn't count). */
    public static boolean joustUnslowed(net.minecraft.world.entity.Entity entity) {
        if (entity instanceof ServerPlayer player) {
            return PlayerStats.score(player, "Fortitude") >= JOUST_UNSLOWED;
        }
        return entity instanceof net.minecraft.world.entity.player.Player player && player.isLocalPlayer() && localHardy;
    }

    /**
     * Whether this is a player strong enough to ignore Epic Knights' penalties: one-handed two-handers, heavy shields'
     * and jousting armour's slowness. Asked on both sides, since the client predicts its own movement and attack speed.
     */
    public static boolean unhindered(net.minecraft.world.entity.Entity entity) {
        if (entity instanceof ServerPlayer player) {
            return PlayerStats.score(player, "Strength") >= ONE_HAND_HEAVY;
        }
        return entity instanceof net.minecraft.world.entity.player.Player player && player.isLocalPlayer() && localUnhindered;
    }

    private WeaponSkills() {
    }

    /** {haste, strength, knockback} levels from a weapon skill: level 1 haste, 2 strength, 3 knockback, 4 haste again... */
    static int[] bonuses(int skill) {
        return new int[]{(skill + 2) / 3, (skill + 1) / 3, skill / 3};
    }

    /** Which weapon skill a held item uses ({@link PlayerStats#WEAPONS}), or null for no weapon. */
    @Nullable
    public static String caliber(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        if (Armoury.twoHanded(stack)) {
            return "Heavy";
        }
        String id = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        for (String light : new String[]{"stiletto", "shortsword", "katzbalger", "dagger", "knife"}) {
            if (id.contains(light)) {
                return "Light";
            }
        }
        boolean weapon = stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem || stack.getItem() instanceof TridentItem
                || stack.getItem() instanceof MaceItem || stack.getItem().getClass().getName().startsWith("com.magistuarmory.");
        return weapon ? "Medium" : null;
    }

    private static int skill(ServerPlayer player) {
        String caliber = caliber(player.getMainHandItem());
        return caliber == null ? 0 : PlayerStats.score(player, caliber);
    }

    /** Main-hand weapons of unhindered players, each with its holder (weakly, so neither is kept alive by this). */
    private static final java.util.Map<ItemStack, java.lang.ref.WeakReference<net.minecraft.world.entity.player.Player>> WIELDED =
            java.util.Collections.synchronizedMap(new java.util.WeakHashMap<>());
    private static ItemStack sword;

    /**
     * Better Combat's moveset for a stack ({@code attributes}, its WeaponAttributes): a two-handed weapon in the main
     * hand of a player strong enough ({@link #ONE_HAND_HEAVY}) swings as a one-handed sword, so the off hand stays free.
     * Asked by Better Combat on both sides; the same stack object is what the holder has in hand (ItemStack has no equals).
     */
    public static ItemStack movesetOf(ItemStack stack) {
        var holder = WIELDED.get(stack);
        net.minecraft.world.entity.player.Player player = holder == null ? null : holder.get();
        // inventory, not getMainHandItem: Better Combat hooks getItemBySlot to ask getAttributes, which asks us again (stack overflow)
        if (player == null || player.getInventory().getSelected() != stack || !unhindered(player)) {
            return stack;
        }
        if (sword == null) {
            sword = new ItemStack(net.minecraft.world.item.Items.IRON_SWORD);
        }
        return sword;
    }

    /** The swing's Haste follows the weapon in hand; a strong player's weapon is noted for {@link #movesetOf}. */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (unhindered(event.getEntity()) && !event.getEntity().getMainHandItem().isEmpty()) {
            WIELDED.put(event.getEntity().getMainHandItem(), new java.lang.ref.WeakReference<>(event.getEntity()));
        }
        if (!(event.getEntity() instanceof ServerPlayer player) || player.tickCount % 5 != 0) {
            return;
        }
        AttributeInstance speed = player.getAttribute(Attributes.ATTACK_SPEED);
        if (speed == null) {
            return;
        }
        int haste = bonuses(skill(player))[0];
        if (haste == 0) {
            speed.removeModifier(HASTE);
        } else {
            speed.addOrUpdateTransientModifier(new AttributeModifier(HASTE, haste * HASTE_PER_LEVEL, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }

    /** A player's melee blow on a battlefield soldier: Strength and knockback from the weapon's skill. */
    @SubscribeEvent
    public static void onHurt(LivingIncomingDamageEvent event) {
        if (!(event.getSource().getDirectEntity() instanceof ServerPlayer player) || !RecruitsBattle.isOurs(event.getEntity())) {
            return;
        }
        int[] bonus = bonuses(skill(player));
        event.setAmount(event.getAmount() + bonus[1] * STRENGTH_PER_LEVEL);
        if (bonus[2] > 0) {
            float yaw = player.getYRot() * Mth.DEG_TO_RAD;
            event.getEntity().knockback(bonus[2] * KNOCKBACK_PER_LEVEL, Mth.sin(yaw), -Mth.cos(yaw));
        }
    }

    /** Minecraft's XP feeds the player's own level, for now. */
    @SubscribeEvent
    public static void onXp(PlayerXpEvent.XpChange event) {
        if (event.getAmount() > 0 && event.getEntity() instanceof ServerPlayer player) {
            PlayerStats.gainExperience(player, event.getAmount());
        }
    }
}
