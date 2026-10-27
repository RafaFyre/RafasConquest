package rafasconquest.bridge;

import com.talhanation.recruits.FactionEvents;
import com.talhanation.recruits.config.RecruitsServerConfig;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import com.talhanation.recruits.entities.BowmanEntity;
import com.talhanation.recruits.init.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import com.talhanation.recruits.world.RecruitsFaction;
import com.talhanation.recruits.world.RecruitsFactionManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.faction.Faction;
import rafasconquest.faction.FactionBanner;
import rafasconquest.faction.FactionBannerNbt;
import rafasconquest.faction.FactionType;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Registers this mod's factions as Recruits factions, so they show in the Recruits faction menu: a scoreboard team
 * (Recruits factions are teams) plus a {@link RecruitsFaction} with the faction's name, colour and banner.
 * <p>
 * A Recruits faction made here has no player leader. Its leader id is our faction's id, which identifies it as ours,
 * and its leader name is the faction type ("Empire", "City-state"). Recruits saves these with the world.
 */
public class RecruitsFactions implements RecruitsBridge {
    /** Dye id → nearest chat colour, used for the team colour. */
    private static final ChatFormatting[] CHAT_COLORS = {
            ChatFormatting.WHITE, ChatFormatting.GOLD, ChatFormatting.LIGHT_PURPLE, ChatFormatting.AQUA,
            ChatFormatting.YELLOW, ChatFormatting.GREEN, ChatFormatting.LIGHT_PURPLE, ChatFormatting.DARK_GRAY,
            ChatFormatting.GRAY, ChatFormatting.DARK_AQUA, ChatFormatting.DARK_PURPLE, ChatFormatting.BLUE,
            ChatFormatting.GOLD, ChatFormatting.DARK_GREEN, ChatFormatting.RED, ChatFormatting.BLACK};

    /** Recruits' follow state for "hold your position". */
    private static final int HOLD_POSITION = 2;

    private final ServerLevel overworld;

    public RecruitsFactions(ServerLevel overworld) {
        this.overworld = overworld;
    }

    @Override
    public void onFactionCreated(Faction faction) {
        if (register(faction)) {
            publish();
        }
    }

    @Override
    public void syncAll(Collection<Faction> factions) {
        int added = 0;
        for (Faction faction : factions) {
            if (register(faction)) {
                added++;
            }
        }
        if (added > 0) {
            publish();
            RafasConquest.LOGGER.info("Registered {} factions with Recruits", added);
        }
    }

    /** Returns true if a new Recruits faction was created. */
    private boolean register(Faction faction) {
        RecruitsFactionManager manager = FactionEvents.recruitsFactionManager;
        if (manager == null) {
            return false; // Recruits hasn't started yet; syncAll on server start catches up
        }
        String id = teamId(manager, faction);
        if (id == null) {
            return false; // already registered
        }
        FactionBanner banner = FactionBanner.of(faction.id());
        ChatFormatting color = CHAT_COLORS[banner.base()];
        ServerScoreboard scoreboard = overworld.getServer().getScoreboard();
        PlayerTeam team = scoreboard.getPlayerTeam(id);
        if (team == null) {
            team = scoreboard.addPlayerTeam(id);
        }
        team.setDisplayName(Component.literal(faction.name()));
        team.setColor(color);
        applyTeamRules(team);
        manager.addTeam(id, faction.name(), faction.id(), typeName(faction.type()),
                FactionBannerNbt.item(overworld.registryAccess(), banner).serializeNBT(), (byte) banner.base(), color);
        return true;
    }

    /**
     * The Recruits id for a faction: its name in lower case. Returns null if this faction is already registered;
     * if another faction (ours or a player's) holds the name, a numeric suffix is added.
     */
    private static String teamId(RecruitsFactionManager manager, Faction faction) {
        String base = faction.name().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_]", "");
        for (int n = 1; ; n++) {
            String id = n == 1 ? base : base + n;
            RecruitsFaction existing = manager.getFactionByStringID(id);
            if (existing == null) {
                return id;
            }
            if (faction.id().equals(existing.getTeamLeaderUUID())) {
                return null;
            }
        }
    }

    /**
     * The team rules Recruits gives its own factions: no friendly fire (unless the server's Recruits config turns it
     * on), so members' arrows pass through each other. A new scoreboard team allows friendly fire, and Recruits only
     * resets it for every team when the server starts, so a team made mid-game needs it set here.
     */
    private static void applyTeamRules(PlayerTeam team) {
        boolean global = RecruitsServerConfig.GlobalTeamSetting.get();
        team.setAllowFriendlyFire(global && RecruitsServerConfig.GlobalTeamFriendlyFireSetting.get());
        team.setSeeFriendlyInvisibles(global && RecruitsServerConfig.GlobalTeamSeeFriendlyInvisibleSetting.get());
    }

    private static String typeName(FactionType type) {
        return switch (type) {
            case EMPIRE -> "Empire";
            case CITY_STATE -> "City-state";
            case BANDIT -> "Bandits";
        };
    }

    /**
     * A bowman set up the way Recruits sets up its own patrol bowmen (chainmail, a bow, arrows, never despawns), on the
     * owner's team, told to hold its post.
     */
    @Override
    public boolean spawnWallGuard(Faction owner, BlockPos feet, float yaw) {
        PlayerTeam team = team(owner);
        BowmanEntity bowman = ModEntityTypes.BOWMAN.get().create(overworld);
        if (team == null || bowman == null) {
            return false;
        }
        Vec3 post = Vec3.atBottomCenterOf(feet);
        bowman.moveTo(post.x, post.y, post.z, yaw, 0);
        bowman.finalizeSpawn(overworld, overworld.getCurrentDifficultyAt(feet), MobSpawnType.STRUCTURE, null, null);
        bowman.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
        bowman.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
        bowman.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
        bowman.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        bowman.getInventory().addItem(new ItemStack(Items.ARROW, 64));
        AbstractRecruitEntity.applyBiomeAndVariant(bowman);
        bowman.setPersistenceRequired();
        bowman.setHunger(100);
        bowman.setMoral(100);
        // Hold this spot on the wall.
        bowman.setHoldPos(post);
        bowman.setShouldHoldPos(true);
        bowman.setFollowState(HOLD_POSITION);
        overworld.addFreshEntity(bowman);
        FactionEvents.addRecruitToTeam(bowman, team, overworld);
        return true;
    }

    /**
     * The faction's scoreboard team, registering the faction with Recruits first if needed, with its team rules
     * applied (which also covers teams registered before these rules were set). Null if Recruits isn't ready.
     */
    @Nullable
    PlayerTeam team(Faction faction) {
        RecruitsFactionManager manager = FactionEvents.recruitsFactionManager;
        if (manager == null) {
            return null;
        }
        if (registeredId(manager, faction) == null) {
            onFactionCreated(faction);
        }
        String id = registeredId(manager, faction);
        PlayerTeam team = id == null ? null : overworld.getServer().getScoreboard().getPlayerTeam(id);
        if (team != null) {
            applyTeamRules(team);
        }
        return team;
    }

    /** The Recruits id this mod registered for a faction, or null. */
    private static String registeredId(RecruitsFactionManager manager, Faction faction) {
        for (RecruitsFaction rf : manager.getFactions()) {
            if (faction.id().equals(rf.getTeamLeaderUUID())) {
                return rf.getStringID();
            }
        }
        return null;
    }

    private void publish() {
        RecruitsFactionManager manager = FactionEvents.recruitsFactionManager;
        manager.save(overworld);
        manager.broadcastFactionsToAll(overworld);
    }

    /** For tests and debugging: the Recruits factions this mod created. */
    public static List<RecruitsFaction> ours(Collection<Faction> factions) {
        RecruitsFactionManager manager = FactionEvents.recruitsFactionManager;
        if (manager == null) {
            return List.of();
        }
        return manager.getFactions().stream()
                .filter(rf -> factions.stream().anyMatch(f -> f.id().equals(rf.getTeamLeaderUUID())))
                .toList();
    }
}
