package rafasconquest.gear;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import rafasconquest.RafasConquest;
import rafasconquest.people.Homeland;

import java.util.EnumMap;
import java.util.Map;

/**
 * Kits of Epic Knights armour and arms ({@code magistuarmory}), when that mod is installed: for heavy infantry, cavalry
 * and officers in battle, for guards, for rulers, nobles and the sword-titled, and for enlisted players by their rank.
 * Each land has a few looks ({@link Style}), which its nations take in turn. Armour and decorations that take a dye are
 * dyed the side's colour, and surcoats and shields carry its banner. Without Epic Knights every kit is empty, and the callers keep their
 * vanilla gear. Epic Knights is never needed to build or run: its items are looked up by name.
 */
public final class Armoury {
    static final String MOD = "magistuarmory";

    /** Who a kit is for. */
    public enum Kit {
        HEAVY_INFANTRY, CAVALRY, CAPTAIN, GENERAL, GUARD, ARCHER_GUARD,
        RULER, GREAT_NOBLE, NOBLE, SWORD_SAINT, SWORD_KING, SWORD_GOD
    }

    /**
     * A look: the heavy infantry's helmet, body (chest, legs, feet), weapon and shield; the cavalry's helmet, body and
     * weapon; the officers' helmet, body and weapon; and the decoration an officer's helmet wears.
     */
    public record Style(String infantryHelmet, String[] infantryBody, String infantryWeapon, String shield,
                        String cavalryHelmet, String[] cavalryBody, String cavalryWeapon,
                        String officerHelmet, String[] officerBody, String officerWeapon, String crest) {
    }

    /** Each land's looks; its nations take them in turn, so nations of one culture still dress apart. */
    private static final Map<Homeland, java.util.List<Style>> STYLES = new EnumMap<>(Homeland.class);

    private static void looks(Homeland land, Style... looks) {
        STYLES.put(land, java.util.List.of(looks));
    }

    static {
        // The plains: the knights of the west: mail and kettle hats, knightly harness, plate for the officers.
        looks(Homeland.PLAINS, new Style("kettlehat", body("chainmail_chestplate", "chainmail_leggings", "chainmail_boots"),
                "iron_heavymace", "iron_heatershield",
                "armet", body("knight_chestplate", "knight_leggings", "knight_boots"), "iron_chivalrylance",
                "grand_bascinet", body("platemail_chestplate", "platemail_leggings", "platemail_boots"), "steel_bastardsword",
                "big_plume_decoration"),
                // free companies: brigandines and bascinets, cuirassiers, gothic plate and a lion for the officers
                new Style("bascinet", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"),
                        "iron_bastardsword", "iron_tartsche",
                        "sallet", body("cuirassier_chestplate", "cuirassier_leggings", "cuirassier_boots"), "iron_chivalrylance",
                        "armet_with_plume", body("gothic_chestplate", "gothic_leggings", "gothic_boots"), "steel_estoc",
                        "lion_decoration"),
                // the old crusade: mail hoods and crusader surcoats, great helms and a griffin for the officers
                new Style("chainmail_helmet", body("crusader_chestplate", "crusader_leggings", "crusader_boots"),
                        "iron_heavymace", "iron_kiteshield",
                        "greathelm", body("xivcenturyknight_chestplate", "xivcenturyknight_leggings", "xivcenturyknight_boots"), "iron_chivalrylance",
                        "greathelm", body("platemail_chestplate", "platemail_leggings", "platemail_boots"), "steel_bastardsword",
                        "griffin_decoration"));
        // The taiga: the north: nasal helms and lamellar, a bear's head for the officers.
        looks(Homeland.TAIGA, new Style("norman_helmet", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"),
                "iron_morgenstern", "iron_roundshield",
                "shishak", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"), "iron_chivalrylance",
                "face_helmet", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "steel_lochaberaxe",
                "bear_decoration"),
                // mail from head to foot, horned officers
                new Style("chainmail_helmet", body("chainmail_chestplate", "chainmail_leggings", "chainmail_boots"),
                        "iron_heavymace", "iron_roundshield",
                        "norman_helmet", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "iron_chivalrylance",
                        "norman_helmet", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"), "steel_lochaberaxe",
                        "viking_horns_decoration"),
                // padded levies, masked riders, antlered officers
                new Style("kettlehat", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                        "iron_morgenstern", "iron_kiteshield",
                        "face_helmet", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"), "iron_chivalrylance",
                        "face_helmet", body("xivcenturyknight_chestplate", "xivcenturyknight_leggings", "xivcenturyknight_boots"), "steel_claymore",
                        "antlers_decoration"));
        // The snows: heavy crusader mail under great helms, a fur hood for the officers.
        looks(Homeland.SNOW, new Style("greathelm", body("crusader_chestplate", "crusader_leggings", "crusader_boots"),
                "iron_heavywarhammer", "iron_kiteshield",
                "greathelm", body("crusader_chestplate", "crusader_leggings", "crusader_boots"), "iron_chivalrylance",
                "bascinet", body("xivcenturyknight_chestplate", "xivcenturyknight_leggings", "xivcenturyknight_boots"), "steel_claymore",
                "hood_decoration"),
                // mail on foot, knights on horse, a dragon over the officers
                new Style("chainmail_helmet", body("chainmail_chestplate", "chainmail_leggings", "chainmail_boots"),
                        "iron_heavymace", "iron_heatershield",
                        "greathelm", body("knight_chestplate", "knight_leggings", "knight_boots"), "iron_chivalrylance",
                        "greathelm", body("crusader_chestplate", "crusader_leggings", "crusader_boots"), "steel_bastardsword",
                        "dragon_decoration"),
                // bascinets behind pavises, gothic officers
                new Style("bascinet", body("xivcenturyknight_chestplate", "xivcenturyknight_leggings", "xivcenturyknight_boots"),
                        "iron_heavywarhammer", "iron_pavese",
                        "bascinet", body("platemail_chestplate", "platemail_leggings", "platemail_boots"), "iron_chivalrylance",
                        "grand_bascinet", body("gothic_chestplate", "gothic_leggings", "gothic_boots"), "steel_zweihander",
                        "bear_decoration"));
        // The desert: pointed helms and lamellar, round shields, a torse and mantle for the officers.
        looks(Homeland.DESERT, new Style("shishak", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"),
                "messer_sword", "iron_rondache",
                "shishak", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"), "iron_ranseur",
                "shishak", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "steel_estoc",
                "torse_and_mantle_decoration"),
                // light padded foot, half-armoured riders, a torse for the officers
                new Style("chainmail_helmet", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                        "iron_shortsword", "iron_roundshield",
                        "shishak", body("halfarmor_chestplate", "chainmail_leggings", "chainmail_boots"), "iron_chivalrylance",
                        "shishak", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"), "steel_katzbalger",
                        "torse_decoration"),
                // coifed foot, brigandine riders, horned officers
                new Style("coif", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"),
                        "iron_estoc", "iron_target",
                        "chainmail_helmet", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "iron_ranseur",
                        "shishak", body("cuirassier_chestplate", "cuirassier_leggings", "cuirassier_boots"), "messer_sword",
                        "gazelle_horns_decoration"));
        // The savanna: light gambesons and open helms, feathers for the officers.
        looks(Homeland.SAVANNA, new Style("barbute", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                "iron_shortsword", "iron_target",
                "barbute", body("halfarmor_chestplate", "chainmail_leggings", "gambeson_boots"), "iron_guisarme",
                "sallet", body("cuirassier_chestplate", "cuirassier_leggings", "cuirassier_boots"), "steel_katzbalger",
                "feathers_decoration"),
                // kettle hats and maces, a plume for the officers
                new Style("kettlehat", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                        "iron_heavymace", "iron_rondache",
                        "barbute", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "iron_ranseur",
                        "barbute", body("halfarmor_chestplate", "chainmail_leggings", "brigandine_boots"), "steel_estoc",
                        "plume_middle_decoration"),
                // coifs and oval shields, a lion over the officers
                new Style("coif", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                        "iron_shortsword", "iron_ellipticalshield",
                        "sallet", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"), "iron_chivalrylance",
                        "sallet", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "steel_flamebladedsword",
                        "lion_decoration"));
        // The jungle: sallets and gambesons, oval shields, feathers for the officers.
        looks(Homeland.JUNGLE, new Style("sallet", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                "iron_morgenstern", "iron_ellipticalshield",
                "sallet", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "iron_ranseur",
                "bascinet", body("kastenbrust_chestplate", "kastenbrust_leggings", "kastenbrust_boots"), "steel_flamebladedsword",
                "feathers_decoration"),
                // coifs and targes, spiked officers
                new Style("coif", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                        "iron_heavymace", "iron_target",
                        "barbute", body("halfarmor_chestplate", "chainmail_leggings", "gambeson_boots"), "iron_guisarme",
                        "barbute", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "steel_katzbalger",
                        "spike_decoration"),
                // brigandines behind tall shields, armets for the officers
                new Style("kettlehat", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"),
                        "iron_morgenstern", "iron_tartsche",
                        "sallet", body("cuirassier_chestplate", "cuirassier_leggings", "cuirassier_boots"), "iron_chivalrylance",
                        "armet", body("kastenbrust_chestplate", "kastenbrust_leggings", "kastenbrust_boots"), "steel_estoc",
                        "horse_decoration"));
        // The swamp: coifs and gambesons, bucklers, two plumes for the officers.
        looks(Homeland.SWAMP, new Style("coif", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                "iron_shortsword", "iron_buckler",
                "bascinet", body("halfarmor_chestplate", "chainmail_leggings", "chainmail_boots"), "iron_guisarme",
                "bascinet", body("kastenbrust_chestplate", "kastenbrust_leggings", "kastenbrust_boots"), "steel_lucernhammer",
                "two_plumes_decoration"),
                // mail and hammers, lilies over gothic officers
                new Style("chainmail_helmet", body("chainmail_chestplate", "chainmail_leggings", "chainmail_boots"),
                        "iron_heavywarhammer", "iron_buckler",
                        "kettlehat", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "iron_guisarme",
                        "sallet", body("gothic_chestplate", "gothic_leggings", "gothic_boots"), "steel_lucernhammer",
                        "lily_decoration"),
                // pavises in the reeds, plate for the officers
                new Style("kettlehat", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                        "iron_morgenstern", "iron_pavese",
                        "barbute", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "iron_chivalrylance",
                        "armet", body("platemail_chestplate", "platemail_leggings", "platemail_boots"), "steel_ahlspiess",
                        "rondel_decoration"));
    }

    /** A worn outfit: the stack for each slot it fills (the others are left as they are). */
    public record Outfit(Map<EquipmentSlot, ItemStack> slots) {
        public static final Outfit NONE = new Outfit(Map.of());

        public boolean isEmpty() {
            return slots.isEmpty();
        }
    }

    private Armoury() {
    }

    /** The land whose style a nation's people wear: its culture's. Plains if it isn't on the map. */
    public static Homeland homeland(@Nullable rafasconquest.map.BaseMap map, @Nullable java.util.UUID nation) {
        if (map != null && nation != null) {
            for (int n = 0; n < map.nations().size(); n++) {
                if (map.nations().get(n).id().equals(nation) && n < map.realm().nationCulture().length) {
                    return map.realm().cultures().get(map.realm().nationCulture()[n]).homeland();
                }
            }
        }
        return Homeland.PLAINS;
    }

    /**
     * The look a nation's people wear: one of its land's, the nations of one land taking them in turn by their order on
     * the map. A nation off the map takes one by its id.
     */
    public static Style style(@Nullable rafasconquest.map.BaseMap map, @Nullable java.util.UUID nation) {
        Homeland land = homeland(map, nation);
        int turn = nation == null ? 0 : nation.hashCode();
        if (map != null && nation != null) {
            int before = 0;
            for (int n = 0; n < map.nations().size(); n++) {
                if (map.nations().get(n).id().equals(nation)) {
                    turn = before;
                    break;
                }
                if (n < map.realm().nationCulture().length
                        && map.realm().cultures().get(map.realm().nationCulture()[n]).homeland() == land) {
                    before++;
                }
            }
        }
        return style(land, turn);
    }

    /** A land's {@code turn}th look, wrapping round. */
    public static Style style(Homeland land, int turn) {
        java.util.List<Style> looks = STYLES.getOrDefault(land, STYLES.get(Homeland.PLAINS));
        return looks.get(Math.floorMod(turn, looks.size()));
    }

    /** A nation's colour (its banner's ground), as RGB. */
    public static int colour(java.util.UUID nation) {
        float[] c = rafasconquest.faction.FactionBannerNbt.baseColor(rafasconquest.faction.FactionBanner.of(nation)).getTextureDiffuseColors();
        return (int) (c[0] * 255) << 16 | (int) (c[1] * 255) << 8 | (int) (c[2] * 255);
    }

    /** A nation's banner, as an item. */
    public static ItemStack banner(net.minecraft.core.RegistryAccess registries, java.util.UUID nation) {
        return rafasconquest.faction.FactionBannerNbt.item(registries, rafasconquest.faction.FactionBanner.of(nation));
    }

    /** Dresses someone in a nation's kit: its land's style, its colour and its banner. Nothing without Epic Knights. */
    public static void dress(net.minecraft.world.entity.LivingEntity who, Kit kit, @Nullable rafasconquest.map.BaseMap map, java.util.UUID nation) {
        if (!available()) {
            return;
        }
        outfit(kit, style(map, nation), colour(nation), banner(who.level().registryAccess(), nation)).slots()
                .forEach((slot, stack) -> {
                    who.setItemSlot(slot, stack);
                    if (who instanceof com.talhanation.recruits.entities.AbstractRecruitEntity recruit) {
                        // Recruits keeps a copy of what is worn; both must change or the old kit comes back.
                        recruit.getInventory().setItem(recruit.getInventorySlotIndex(slot), stack.copy());
                    }
                });
    }

    /**
     * Dresses a soldier in one of a Character Marker's kits, in its nation's style: 1 levy (half harness), 2 heavy
     * infantry, 3 elite (a sallet and knightly harness), 4 officer's plate. Iron, without Epic Knights.
     */
    public static void dressTier(com.talhanation.recruits.entities.AbstractRecruitEntity who, int tier, @Nullable rafasconquest.map.BaseMap map,
                                 java.util.UUID nation) {
        Map<EquipmentSlot, ItemStack> slots;
        if (!available()) {
            slots = new EnumMap<>(EquipmentSlot.class);
            if (tier >= 2) {
                slots.put(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
                slots.put(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                slots.put(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                slots.put(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
            }
            slots.put(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        } else {
            Style land = style(map, nation);
            ItemStack banner = banner(who.level().registryAccess(), nation);
            slots = switch (Math.max(1, Math.min(4, tier))) {
                case 1 -> rankKit(1, land, colour(nation), banner).slots();
                case 2 -> rankKit(3, land, colour(nation), banner).slots();
                case 3 -> outfit(Kit.HEAVY_INFANTRY, land, colour(nation), banner).slots();
                default -> rankKit(7, land, colour(nation), banner).slots();
            };
        }
        slots.forEach((slot, stack) -> {
            who.setItemSlot(slot, stack);
            who.getInventory().setItem(who.getInventorySlotIndex(slot), stack.copy());
        });
    }

    /**
     * What a character wears: the harness of their sword title if they have one (Saint, King, God), else a ruler's crown,
     * a duke's or capital lord's small crown, or a noble's ceremonial dress.
     */
    public static Kit kitOf(rafasconquest.map.BaseMap map, int person) {
        rafasconquest.people.Person p = map.realm().people().get(person);
        int prowess = p.stat(rafasconquest.people.Stat.PROWESS);
        if (prowess >= rafasconquest.people.Stat.SWORD_GOD) {
            return Kit.SWORD_GOD;
        }
        if (prowess >= rafasconquest.people.Stat.SWORD_KING) {
            return Kit.SWORD_KING;
        }
        if (prowess >= rafasconquest.people.Stat.SUPERHUMAN) {
            return Kit.SWORD_SAINT;
        }
        if (p.nation() >= 0 && map.realm().ruler(p.nation()) == person) {
            return Kit.RULER;
        }
        return p.hold().kind() == rafasconquest.people.Person.Kind.CAPITAL || p.hold().kind() == rafasconquest.people.Person.Kind.CITY
                ? Kit.GREAT_NOBLE : Kit.NOBLE;
    }

    /** Dresses a character as {@link #kitOf} says, in their nation's style, colour and banner (their culture's, if of none). */
    public static void dressCharacter(net.minecraft.world.entity.LivingEntity who, rafasconquest.map.BaseMap map, int person) {
        if (!available()) {
            return;
        }
        rafasconquest.people.Person p = map.realm().people().get(person);
        if (p.nation() >= 0) {
            dress(who, kitOf(map, person), map, map.nations().get(p.nation()).id());
        } else {
            outfit(kitOf(map, person), style(map.realm().cultures().get(p.culture()).homeland(), 0), 0x6B5A44, null).slots().forEach(who::setItemSlot);
        }
    }

    /** Whether Epic Knights is installed. */
    public static boolean available() {
        return ModList.get() != null && ModList.get().isLoaded(MOD);
    }

    /**
     * A kit in look {@code s}, their side's {@code colour} (RGB) on whatever takes a dye and its
     * {@code banner} (a banner item, or null) on surcoats and shields. Empty without Epic Knights.
     */
    public static Outfit outfit(Kit kit, Style s, int colour, @Nullable ItemStack banner) {
        if (!available()) {
            return Outfit.NONE;
        }
        Map<EquipmentSlot, ItemStack> slots = new EnumMap<>(EquipmentSlot.class);
        switch (kit) {
            case HEAVY_INFANTRY -> {
                // Knights on foot, in every land: a sallet and knightly harness, an iron zweihänder in both hands.
                wear(slots, "sallet", body("knight_chestplate", "knight_leggings", "knight_boots"), colour, banner);
                hold(slots, "iron_zweihander", null, banner);
                slots.put(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            }
            case GUARD -> {
                wear(slots, s.infantryHelmet(), s.infantryBody(), colour, banner);
                hold(slots, s.infantryWeapon(), s.shield(), banner);
            }
            case ARCHER_GUARD -> {
                wear(slots, s.infantryHelmet(), s.infantryBody(), colour, banner);
                slots.remove(EquipmentSlot.MAINHAND); // keeps its bow
            }
            case CAVALRY -> {
                wear(slots, s.cavalryHelmet(), s.cavalryBody(), colour, banner);
                hold(slots, s.cavalryWeapon(), s.shield(), banner);
            }
            case CAPTAIN -> {
                // Their body armour; their head keeps the banner they lead by.
                wear(slots, null, s.infantryBody(), colour, banner);
            }
            case GENERAL -> {
                wear(slots, s.officerHelmet(), s.officerBody(), colour, banner);
                crest(slots, s.crest(), colour);
                hold(slots, s.officerWeapon(), null, banner);
            }
            case RULER -> {
                wear(slots, "crown_decoration", body("ceremonial_chestplate", null, "ceremonial_boots"), colour, banner);
                hold(slots, "noble_sword", null, banner);
            }
            case GREAT_NOBLE -> wear(slots, "minicrown_decoration", body("ceremonial_chestplate", null, "ceremonial_boots"), colour, banner);
            case NOBLE -> wear(slots, null, body("ceremonial_chestplate", null, "ceremonial_boots"), colour, banner);
            case SWORD_SAINT -> {
                wear(slots, s.cavalryHelmet(), body("knight_chestplate", "knight_leggings", "knight_boots"), colour, banner);
                hold(slots, "steel_bastardsword", null, banner);
            }
            case SWORD_KING -> {
                wear(slots, "armet_with_plume", body("gothic_chestplate", "gothic_leggings", "gothic_boots"), colour, banner);
                hold(slots, "steel_claymore", null, banner);
            }
            case SWORD_GOD -> {
                wear(slots, "maximilian_helmet", body("maximilian_chestplate", "maximilian_leggings", "maximilian_boots"), colour, banner);
                crest(slots, "big_plume_decoration", colour);
                hold(slots, "steel_zweihander", null, banner);
            }
        }
        return new Outfit(slots);
    }

    /**
     * What an enlisted player is given on reaching a rank (1 Soldier to 8 Grand Marshal), in their nation's look: the
     * levy's half harness at first, the heavy infantry's from Serjeant, the cavalry's from Major, the officers' from
     * Marshal. Empty without Epic Knights.
     */
    public static Outfit rankKit(int rank, Style s, int colour, @Nullable ItemStack banner) {
        if (!available()) {
            return Outfit.NONE;
        }
        Map<EquipmentSlot, ItemStack> slots = new EnumMap<>(EquipmentSlot.class);
        if (rank <= 2) {
            wear(slots, rank == 1 ? "coif" : s.infantryHelmet(), body("gambeson_chestplate", null, "gambeson_boots"), colour, banner);
            hold(slots, "iron_shortsword", null, banner);
        } else if (rank <= 4) {
            wear(slots, s.infantryHelmet(), s.infantryBody(), colour, banner);
            hold(slots, s.infantryWeapon(), s.shield(), banner);
        } else if (rank <= 6) {
            wear(slots, s.cavalryHelmet(), s.cavalryBody(), colour, banner);
            hold(slots, s.officerWeapon(), s.shield(), banner);
        } else {
            wear(slots, s.officerHelmet(), s.officerBody(), colour, banner);
            crest(slots, s.crest(), colour);
            hold(slots, s.officerWeapon(), s.shield(), banner);
        }
        return new Outfit(slots);
    }

    private static String[] body(@Nullable String chest, @Nullable String legs, @Nullable String feet) {
        return new String[]{chest, legs, feet};
    }

    /** A helmet and a body, dyed, the chest with the banner as its surcoat. */
    private static void wear(Map<EquipmentSlot, ItemStack> slots, @Nullable String helmet, String[] body, int colour, @Nullable ItemStack banner) {
        put(slots, EquipmentSlot.HEAD, stack(helmet, colour));
        ItemStack chest = stack(body[0], colour);
        if (chest != null) {
            herald(chest, banner);
        }
        put(slots, EquipmentSlot.CHEST, chest);
        put(slots, EquipmentSlot.LEGS, stack(body[1], colour));
        put(slots, EquipmentSlot.FEET, stack(body[2], colour));
    }

    /** A weapon, and a shield bearing the banner. */
    private static void hold(Map<EquipmentSlot, ItemStack> slots, @Nullable String weapon, @Nullable String shield, @Nullable ItemStack banner) {
        put(slots, EquipmentSlot.MAINHAND, stack(weapon, -1));
        ItemStack s = stack(shield, -1);
        if (s != null) {
            herald(s, banner);
        }
        put(slots, EquipmentSlot.OFFHAND, s);
    }

    /** A decoration on the helmet (a plume, a hood, a mantle), dyed the side's colour. */
    private static void crest(Map<EquipmentSlot, ItemStack> slots, String decoration, int colour) {
        ItemStack helmet = slots.get(EquipmentSlot.HEAD);
        ItemStack crest = stack(decoration, colour);
        if (helmet == null || crest == null) {
            return;
        }
        try {
            Class<?> type = Class.forName("com.magistuarmory.item.ArmorDecoration");
            if (type.isInstance(crest.getItem())) {
                type.getMethod("decorate", ItemStack.class, ItemStack.class).invoke(crest.getItem(), helmet, crest);
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            RafasConquest.LOGGER.debug("Could not decorate {} with {}: {}", helmet, crest, e.toString());
        }
    }

    private static void put(Map<EquipmentSlot, ItemStack> slots, EquipmentSlot slot, @Nullable ItemStack stack) {
        if (stack != null) {
            slots.put(slot, stack);
        }
    }

    /**
     * A support soldier's polearm: Better Weaponry's iron glaive, else Epic Knights' iron guisarme (the nearest it has);
     * null with neither mod.
     */
    @Nullable
    public static ItemStack glaive() {
        for (ResourceLocation id : new ResourceLocation[]{new ResourceLocation("better_weaponry", "iron_glaive"), new ResourceLocation(MOD, "iron_guisarme")}) {
            Item item = ForgeRegistries.ITEMS.getValue(id);
            if (item != null && item != Items.AIR) {
                return new ItemStack(item);
            }
        }
        return null;
    }

    /** One of Epic Knights' items by name, dyed {@code colour} if it takes a dye (and colour is not -1); null if it has none such. */
    @Nullable
    static ItemStack stack(@Nullable String name, int colour) {
        if (name == null) {
            return null;
        }
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MOD, name));
        if (item == null || item == Items.AIR) {
            return null;
        }
        ItemStack stack = new ItemStack(item);
        if (colour >= 0 && item instanceof DyeableLeatherItem dyeable) {
            dyeable.setColor(stack, colour & 0xFFFFFF);
        }
        return stack;
    }

    /** Puts a banner's design on a surcoat or a shield, as Epic Knights' heraldry (and vanilla's shields) keep it. */
    static void herald(ItemStack stack, @Nullable ItemStack banner) {
        if (banner == null || !(banner.getItem() instanceof BannerItem bannerItem)) {
            return;
        }
        CompoundTag design = BlockItem.getBlockEntityData(banner);
        CompoundTag tag = design == null ? new CompoundTag() : design.copy();
        tag.putInt("Base", bannerItem.getColor().getId());
        stack.addTagElement("BlockEntityTag", tag);
    }
}
