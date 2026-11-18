package rafasconquest.gear;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.fml.ModList;
import net.minecraft.core.registries.BuiltInRegistries;
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
@net.neoforged.fml.common.EventBusSubscriber(modid = RafasConquest.MOD_ID)
public final class Armoury {
    static final String MOD = "magistuarmory";
    /** Epic Knights: Addon, optional on top: its looks join each land's, and characters' taste draws on all its pieces. */
    static final String ADDON = "magistuarmoryaddon";

    /** Who a kit is for. */
    public enum Kit {
        /** Battle blocks by kind: {@link rafasconquest.battle.BattlePlan.Arm}. */
        LEVY, HEAVY_INFANTRY, SKIRMISHER, ZWEIHANDLER, ARCHER, CAVALRY, HEAVY_CAVALRY, MUSKETEER, HALBERDIER,
        CAPTAIN, GENERAL, GUARD, ARCHER_GUARD,
        RULER, GREAT_NOBLE, NOBLE, SWORD_SAINT, SWORD_KING, SWORD_GOD
    }

    /**
     * A look: the heavy infantry's helmet, body (chest, legs, feet), weapon and shield; the cavalry's helmet, body and
     * weapon; the officers' helmet, body and weapon; the decoration an officer's helmet wears; the decoration on the
     * helmets of the cavalry and the heavy infantry; and the piece on an officer's chest (a hood, an ecranche, a rondel).
     * Then, for when Epic Knights: Antique Legacy is installed, a decoration of its own for those troops' helmets (in
     * place of their crest) and one for the officers' chest (on top of theirs). Only its decorations, never its armour.
     */
    public record Style(String infantryHelmet, String[] infantryBody, String infantryWeapon, String shield,
                        String cavalryHelmet, String[] cavalryBody, String cavalryWeapon,
                        String officerHelmet, String[] officerBody, String officerWeapon, String crest,
                        String troopCrest, String officerChest, String antiqueCrest, String antiqueChest) {

        /** The troops' helmet decoration: Antique Legacy's if it is installed, else Epic Knights'. */
        String lineCrest() {
            return stack(antiqueCrest, -1) != null ? antiqueCrest : troopCrest;
        }
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
                "big_plume_decoration", "plume_middle_decoration", "ecranche_decoration",
                "antiquelegacy:triple_feather_decoration", "antiquelegacy:antique_cloak_decoration"),
                // free companies: brigandines and bascinets, cuirassiers, gothic plate and a lion for the officers
                new Style("bascinet", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"),
                        "iron_bastardsword", "iron_tartsche",
                        "sallet", body("cuirassier_chestplate", "cuirassier_leggings", "cuirassier_boots"), "iron_chivalrylance",
                        "armet_with_plume", body("gothic_chestplate", "gothic_leggings", "gothic_boots"), "steel_estoc",
                        "lion_decoration", "plume_left_decoration", "rondel_decoration",
                        "antiquelegacy:roman_two_feathers_decoration", "antiquelegacy:right_gladiator_shoulder_pad_decoration"),
                // the old crusade: mail hoods and crusader surcoats, great helms and a griffin for the officers
                new Style("chainmail_helmet", body("crusader_chestplate", "crusader_leggings", "crusader_boots"),
                        "iron_heavymace", "iron_kiteshield",
                        "greathelm", body("xivcenturyknight_chestplate", "xivcenturyknight_leggings", "xivcenturyknight_boots"), "iron_chivalrylance",
                        "greathelm", body("platemail_chestplate", "platemail_leggings", "platemail_boots"), "steel_bastardsword",
                        "griffin_decoration", "torse_decoration", "hood_decoration",
                        "antiquelegacy:hair_plume_decoration", "antiquelegacy:antique_cloak_decoration"));
        // The taiga: the north: nasal helms and lamellar, a bear's head for the officers.
        looks(Homeland.TAIGA, new Style("norman_helmet", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"),
                "iron_morgenstern", "iron_roundshield",
                "shishak", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"), "iron_chivalrylance",
                "face_helmet", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "steel_lochaberaxe",
                "bear_decoration", "bullhorns_decoration", "hood_decoration",
                "antiquelegacy:bronze_horns_decoration", "antiquelegacy:fur_cloak_decoration"),
                // mail from head to foot, horned officers
                new Style("chainmail_helmet", body("chainmail_chestplate", "chainmail_leggings", "chainmail_boots"),
                        "iron_heavymace", "iron_roundshield",
                        "norman_helmet", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "iron_chivalrylance",
                        "norman_helmet", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"), "steel_lochaberaxe",
                        "viking_horns_decoration", "spike_decoration", "rondel_decoration",
                        "antiquelegacy:bronze_small_wings_decoration", "antiquelegacy:fur_cloak_decoration"),
                // padded levies, masked riders, antlered officers
                new Style("kettlehat", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                        "iron_morgenstern", "iron_kiteshield",
                        "face_helmet", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"), "iron_chivalrylance",
                        "face_helmet", body("xivcenturyknight_chestplate", "xivcenturyknight_leggings", "xivcenturyknight_boots"), "steel_claymore",
                        "antlers_decoration", "plume_right_decoration", "hood_decoration",
                        "antiquelegacy:two_hair_plumes_decoration", "antiquelegacy:celtic_tunic_underarmor_decoration"));
        // The snows: heavy crusader mail under great helms, a fur hood for the officers.
        looks(Homeland.SNOW, new Style("greathelm", body("crusader_chestplate", "crusader_leggings", "crusader_boots"),
                "iron_heavywarhammer", "iron_kiteshield",
                "greathelm", body("crusader_chestplate", "crusader_leggings", "crusader_boots"), "iron_chivalrylance",
                "bascinet", body("xivcenturyknight_chestplate", "xivcenturyknight_leggings", "xivcenturyknight_boots"), "steel_claymore",
                "griffin_decoration", "plume_middle_decoration", "hood_decoration",
                "antiquelegacy:hair_plume_decoration", "antiquelegacy:fur_cloak_decoration"),
                // mail on foot, knights on horse, a dragon over the officers
                new Style("chainmail_helmet", body("chainmail_chestplate", "chainmail_leggings", "chainmail_boots"),
                        "iron_heavymace", "iron_heatershield",
                        "greathelm", body("knight_chestplate", "knight_leggings", "knight_boots"), "iron_chivalrylance",
                        "greathelm", body("crusader_chestplate", "crusader_leggings", "crusader_boots"), "steel_bastardsword",
                        "dragon_decoration", "torse_decoration", "ecranche_decoration",
                        "antiquelegacy:bronze_horns_decoration", "antiquelegacy:fur_cloak_decoration"),
                // bascinets behind pavises, gothic officers
                new Style("bascinet", body("xivcenturyknight_chestplate", "xivcenturyknight_leggings", "xivcenturyknight_boots"),
                        "iron_heavywarhammer", "iron_pavese",
                        "bascinet", body("platemail_chestplate", "platemail_leggings", "platemail_boots"), "iron_chivalrylance",
                        "grand_bascinet", body("gothic_chestplate", "gothic_leggings", "gothic_boots"), "steel_zweihander",
                        "bear_decoration", "two_plumes_decoration", "rondel_decoration",
                        "antiquelegacy:triple_feather_decoration", "antiquelegacy:subarmalis_decoration"));
        // The desert: pointed helms and lamellar, round shields, a torse and mantle for the officers.
        looks(Homeland.DESERT, new Style("shishak", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"),
                "messer_sword", "iron_rondache",
                "shishak", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"), "iron_ranseur",
                "shishak", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "steel_estoc",
                "torse_and_mantle_decoration", "torse_decoration", "ecranche_decoration",
                "antiquelegacy:roman_crest_decoration", "antiquelegacy:chiton_decoration"),
                // light padded foot, half-armoured riders, a torse for the officers
                new Style("chainmail_helmet", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                        "iron_shortsword", "iron_roundshield",
                        "shishak", body("halfarmor_chestplate", "chainmail_leggings", "chainmail_boots"), "iron_chivalrylance",
                        "shishak", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"), "steel_katzbalger",
                        "torse_decoration", "spike_decoration", "rondel_decoration",
                        "antiquelegacy:greek_crest_decoration", "antiquelegacy:bronze_left_hand_manika_decoration"),
                // coifed foot, brigandine riders, horned officers
                new Style("coif", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"),
                        "iron_estoc", "iron_target",
                        "chainmail_helmet", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "iron_ranseur",
                        "shishak", body("cuirassier_chestplate", "cuirassier_leggings", "cuirassier_boots"), "messer_sword",
                        "gazelle_horns_decoration", "plume_middle_decoration", "hood_decoration",
                        "antiquelegacy:roman_crest_alt_decoration", "antiquelegacy:chiton_decoration"));
        // The savanna: light gambesons and open helms, feathers for the officers.
        looks(Homeland.SAVANNA, new Style("barbute", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                "iron_shortsword", "iron_target",
                "barbute", body("halfarmor_chestplate", "chainmail_leggings", "gambeson_boots"), "iron_guisarme",
                "sallet", body("cuirassier_chestplate", "cuirassier_leggings", "cuirassier_boots"), "steel_katzbalger",
                "feathers_decoration", "plume_left_decoration", "ecranche_decoration",
                "antiquelegacy:greek_high_crest_decoration", "antiquelegacy:fabric_right_hand_manika_decoration"),
                // kettle hats and maces, a plume for the officers
                new Style("kettlehat", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                        "iron_heavymace", "iron_rondache",
                        "barbute", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "iron_ranseur",
                        "barbute", body("halfarmor_chestplate", "chainmail_leggings", "brigandine_boots"), "steel_estoc",
                        "plume_middle_decoration", "feathers_decoration", "rondel_decoration",
                        "antiquelegacy:roman_feathers_decoration", "antiquelegacy:chiton_decoration"),
                // coifs and oval shields, a lion over the officers
                new Style("coif", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                        "iron_shortsword", "iron_ellipticalshield",
                        "sallet", body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"), "iron_chivalrylance",
                        "sallet", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "steel_flamebladedsword",
                        "lion_decoration", "plume_right_decoration", "hood_decoration",
                        "antiquelegacy:greek_crest_decoration", "antiquelegacy:left_gladiator_shoulder_pad_decoration"));
        // The jungle: sallets and gambesons, oval shields, feathers for the officers.
        looks(Homeland.JUNGLE, new Style("sallet", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                "iron_morgenstern", "iron_ellipticalshield",
                "sallet", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "iron_ranseur",
                "bascinet", body("kastenbrust_chestplate", "kastenbrust_leggings", "kastenbrust_boots"), "steel_flamebladedsword",
                "feathers_decoration", "plume_middle_decoration", "ecranche_decoration",
                "antiquelegacy:roman_feathers_decoration", "antiquelegacy:fabric_left_hand_manika_decoration"),
                // coifs and targes, spiked officers
                new Style("coif", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                        "iron_heavymace", "iron_target",
                        "barbute", body("halfarmor_chestplate", "chainmail_leggings", "gambeson_boots"), "iron_guisarme",
                        "barbute", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "steel_katzbalger",
                        "spike_decoration", "feathers_decoration", "rondel_decoration",
                        "antiquelegacy:two_hair_plumes_decoration", "antiquelegacy:celtic_tunic_underarmor_decoration"),
                // brigandines behind tall shields, armets for the officers
                new Style("kettlehat", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"),
                        "iron_morgenstern", "iron_tartsche",
                        "sallet", body("cuirassier_chestplate", "cuirassier_leggings", "cuirassier_boots"), "iron_chivalrylance",
                        "armet", body("kastenbrust_chestplate", "kastenbrust_leggings", "kastenbrust_boots"), "steel_estoc",
                        "horse_decoration", "spike_decoration", "hood_decoration",
                        "antiquelegacy:greek_high_crest_decoration", "antiquelegacy:subarmalis_decoration"));
        // The swamp: coifs and gambesons, bucklers, two plumes for the officers.
        looks(Homeland.SWAMP, new Style("coif", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                "iron_shortsword", "iron_buckler",
                "bascinet", body("halfarmor_chestplate", "chainmail_leggings", "chainmail_boots"), "iron_guisarme",
                "bascinet", body("kastenbrust_chestplate", "kastenbrust_leggings", "kastenbrust_boots"), "steel_lucernhammer",
                "two_plumes_decoration", "plume_left_decoration", "rondel_decoration",
                "antiquelegacy:roman_two_feathers_decoration", "antiquelegacy:celtic_tunic_underarmor_decoration"),
                // mail and hammers, lilies over gothic officers
                new Style("chainmail_helmet", body("chainmail_chestplate", "chainmail_leggings", "chainmail_boots"),
                        "iron_heavywarhammer", "iron_buckler",
                        "kettlehat", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "iron_guisarme",
                        "sallet", body("gothic_chestplate", "gothic_leggings", "gothic_boots"), "steel_lucernhammer",
                        "lily_decoration", "torse_decoration", "ecranche_decoration",
                        "antiquelegacy:roman_crest_alt_decoration", "antiquelegacy:iron_right_hand_manika_decoration"),
                // pavises in the reeds, plate for the officers
                new Style("kettlehat", body("gambeson_chestplate", "chainmail_leggings", "gambeson_boots"),
                        "iron_morgenstern", "iron_pavese",
                        "barbute", body("brigandine_chestplate", "chainmail_leggings", "brigandine_boots"), "iron_chivalrylance",
                        "armet", body("platemail_chestplate", "platemail_leggings", "platemail_boots"), "steel_ahlspiess",
                        "bullhorns_decoration", "feathers_decoration", "rondel_decoration",
                        "antiquelegacy:bronze_small_wings_decoration", "antiquelegacy:antique_cloak_decoration"));
    }

    /** Looks built from Epic Knights: Addon's pieces, which join each land's own only when the addon is installed. */
    private static final Map<Homeland, java.util.List<Style>> ADDON_STYLES = new EnumMap<>(Homeland.class);

    /** An Epic Knights: Addon piece's name. */
    private static String a(String name) {
        return ADDON + ":" + name;
    }

    static {
        ADDON_STYLES.put(Homeland.PLAINS, java.util.List.of(
                // late harness: coats of plates, cuirassiers, Greenwich plate and a royal plume for the officers
                new Style(a("sallet_without_visor"), body(a("coat_of_plates_chestplate"), "chainmail_leggings", a("coat_of_plates_boots")),
                        a("steel_war_hammer"), "iron_heatershield",
                        a("closed_burgonet"), body(a("heavy_cuirassier_chestplate"), "cuirassier_leggings", a("heavy_cuirassier_boots")), a("steel_lance"),
                        a("greenwich_armet"), body(a("greenwich_chestplate"), "gothic_leggings", a("greenwich_boots")), a("steel_longsword"),
                        a("royal_plume_decoration"), a("landsknecht_feathers_decoration"), "rondel_decoration",
                        "antiquelegacy:triple_feather_decoration", "antiquelegacy:antique_cloak_decoration"),
                // English: chapels and chained gambesons, English knights, avant harness under British armets
                new Style(a("chapel"), body(a("chained_gambeson"), "chainmail_leggings", a("chained_gambeson_boots")),
                        a("steel_round_mace"), "iron_heatershield",
                        a("late_bascinet"), body(a("english_knight_chestplate"), "knight_leggings", a("english_knight_boots")), a("steel_lance"),
                        a("british_armet"), body(a("avant_chestplate"), a("avant_leggings"), a("avant_boots")), a("steel_arming_sword_type_xv"),
                        a("frontal_feather_decoration"), a("horse_tail_decoration"), "ecranche_decoration",
                        "antiquelegacy:roman_two_feathers_decoration", "antiquelegacy:antique_cloak_decoration")));
        ADDON_STYLES.put(Homeland.TAIGA, java.util.List.of(
                // gallowglass axemen, splinted riders, lion-helmed officers
                new Style(a("cervelliere"), body(a("gallowglass_chestplate"), a("gallowglass_leggings"), a("gallowglass_boots")),
                        a("steel_broadaxe"), "iron_roundshield",
                        a("scale_helmet"), body(a("splint_chestplate"), a("splint_leggings"), a("splint_boots")), a("steel_lance"),
                        a("lion_helmet"), body(a("xiii_century_knight_chestplate"), a("xiii_century_knight_leggings"), a("xiii_century_knight_boots")),
                        a("steel_gallowglass_axe"),
                        a("ram_horns_decoration"), a("horse_tail_decoration"), "hood_decoration",
                        "antiquelegacy:bronze_horns_decoration", "antiquelegacy:fur_cloak_decoration"),
                // the steppe: Cuman helmets and lamellar, heavy brigandine for the captains
                new Style(a("cuman_helmet"), body("lamellar_chestplate", "chainmail_leggings", "lamellar_boots"),
                        a("steel_francisca_axe"), "iron_roundshield",
                        a("light_cuman_helmet"), body(a("splint_chestplate"), a("splint_leggings"), a("splint_boots")), a("steel_lance"),
                        a("cuman_captain_helmet"), body(a("heavy_brigandine_chestplate"), a("heavy_brigandine_leggings"), a("heavy_brigandine_boots")),
                        a("steel_long_seax"),
                        a("horse_tail_decoration"), a("goose_decoration"), "rondel_decoration",
                        "antiquelegacy:bronze_small_wings_decoration", "antiquelegacy:fur_cloak_decoration")));
        ADDON_STYLES.put(Homeland.SNOW, java.util.List.of(
                // a military order: early great helms, white helms on horse, dark crusaders
                new Style(a("early_greathelm"), body(a("xiii_century_knight_chestplate"), a("xiii_century_knight_leggings"), a("xiii_century_knight_boots")),
                        a("steel_war_axe"), "iron_kiteshield",
                        a("white_greathelm"), body("knight_chestplate", "knight_leggings", "knight_boots"), a("steel_lance"),
                        a("late_greathelm"), body(a("dark_crusader_chestplate"), a("dark_crusader_leggings"), a("dark_crusader_boots")),
                        a("steel_german_greatsword"),
                        a("royal_plume_decoration"), a("frontal_feather_decoration"), "hood_decoration",
                        "antiquelegacy:hair_plume_decoration", "antiquelegacy:fur_cloak_decoration"),
                // black harness: German bascinets, dark cuirassiers, dark Maximilian plate
                new Style(a("german_bascinet"), body(a("coat_of_plates_chestplate"), "chainmail_leggings", a("coat_of_plates_boots")),
                        a("steel_round_mace"), "iron_heatershield",
                        a("dark_late_sallet"), body(a("dark_heavy_cuirassier_chestplate"), "cuirassier_leggings", a("dark_heavy_cuirassier_boots")),
                        a("steel_lance"),
                        a("dark_maximilian_helmet"), body(a("dark_maximilian_chestplate"), a("dark_maximilian_leggings"), a("dark_maximilian_boots")),
                        a("steel_executioners_sword"),
                        a("ram_horns_decoration"), a("horse_tail_decoration"), "rondel_decoration",
                        "antiquelegacy:triple_feather_decoration", "antiquelegacy:subarmalis_decoration")));
        ADDON_STYLES.put(Homeland.DESERT, java.util.List.of(
                // Saracens and Mamluks: mirror armour and scimitars
                new Style(a("saracen_helmet"), body(a("saracen_chestplate"), "chainmail_leggings", a("saracen_boots")),
                        a("steel_scimitar"), "iron_rondache",
                        a("kulah_khud"), body(a("mirror_chestplate"), "chainmail_leggings", a("mirror_boots")), a("steel_lance"),
                        a("mamluk_helmet"), body(a("mirror_chestplate"), "chainmail_leggings", a("mirror_boots")), a("steel_grand_falchion"),
                        a("horse_tail_decoration"), a("frontal_feather_decoration"), "ecranche_decoration",
                        "antiquelegacy:roman_crest_decoration", "antiquelegacy:chiton_decoration"),
                // the far south-west: cabassets and morions, falchions and rapiers
                new Style(a("cabasset"), body(a("chained_gambeson"), "chainmail_leggings", a("chained_gambeson_boots")),
                        a("steel_falchion"), "iron_target",
                        a("early_cabasset"), body(a("splint_chestplate"), a("splint_leggings"), a("splint_boots")), a("steel_lance"),
                        a("morion"), body(a("alla_tedesca_chestplate"), "chainmail_leggings", a("alla_tedesca_boots")), a("steel_rapier"),
                        a("frontal_feather_decoration"), a("horse_tail_decoration"), "hood_decoration",
                        "antiquelegacy:greek_crest_decoration", "antiquelegacy:chiton_decoration")));
        ADDON_STYLES.put(Homeland.SAVANNA, java.util.List.of(
                // condottieri: visored kettle hats, Milanese armets, closed barbutes
                new Style(a("visored_kettlehat"), body(a("pikeman_chestplate"), "chainmail_leggings", a("pikeman_boots")),
                        a("steel_sidesword"), "iron_target",
                        a("milanese_armet"), body(a("avant_chestplate"), a("avant_leggings"), a("avant_boots")), a("steel_lance"),
                        a("closed_barbute"), body(a("articulated_chestplate"), "gothic_leggings", "platemail_boots"), a("steel_arming_sword_type_xiv"),
                        a("royal_plume_decoration"), a("landsknecht_feathers_decoration"), "rondel_decoration",
                        "antiquelegacy:greek_high_crest_decoration", "antiquelegacy:fabric_right_hand_manika_decoration"),
                // landsknechts: puff and slash, two-handed messers, sabres for the captains
                new Style(a("late_kettlehat"), body(a("steel_puff_and_slash_chestplate"), "chainmail_leggings", a("steel_puff_and_slash_boots")),
                        a("steel_messer_sword"), "iron_buckler",
                        a("light_burgonet"), body(a("heavy_cuirassier_chestplate"), "cuirassier_leggings", a("heavy_cuirassier_boots")), a("steel_lance"),
                        a("sturmhaube"), body(a("gilded_steel_puff_and_slash_chestplate"), "chainmail_leggings", a("gilded_steel_puff_and_slash_boots")),
                        a("steel_cavalry_sabre"),
                        a("landsknecht_feathers_decoration"), a("frontal_feather_decoration"), "ecranche_decoration",
                        "antiquelegacy:roman_feathers_decoration", "antiquelegacy:chiton_decoration")));
        ADDON_STYLES.put(Homeland.JUNGLE, java.util.List.of(
                // open sallets and chained gambesons, heavy brigandine riders, proto-Maximilian officers
                new Style(a("sallet_without_neck_protection"), body(a("chained_gambeson"), "chainmail_leggings", a("chained_gambeson_boots")),
                        a("steel_bar_mace"), "iron_ellipticalshield",
                        a("opened_sallet"), body(a("heavy_brigandine_chestplate"), a("heavy_brigandine_leggings"), a("heavy_brigandine_boots")),
                        a("steel_lance"),
                        a("two_eye_slits_sallet"), body(a("proto_maximilian_chestplate"), "gothic_leggings", a("proto_maximilian_boots")),
                        a("steel_feder"),
                        a("goose_decoration"), a("horse_tail_decoration"), "hood_decoration",
                        "antiquelegacy:two_hair_plumes_decoration", "antiquelegacy:celtic_tunic_underarmor_decoration"),
                // tunics and bicoques, grilled helms, lobster-tailed officers in engraved plate
                new Style(a("bicoque"), body(a("tunic"), "chainmail_leggings", a("tunic_boots")),
                        a("steel_crow_beak"), "iron_target",
                        a("grilled_helmet"), body(a("coat_of_plates_chestplate"), "chainmail_leggings", a("coat_of_plates_boots")), a("steel_lance"),
                        a("lobster_tailed_helmet"), body(a("engraved_chestplate"), "cuirassier_leggings", "cuirassier_boots"), a("steel_saxon_sword"),
                        a("ram_horns_decoration"), a("frontal_feather_decoration"), "rondel_decoration",
                        "antiquelegacy:greek_high_crest_decoration", "antiquelegacy:subarmalis_decoration")));
        ADDON_STYLES.put(Homeland.SWAMP, java.util.List.of(
                // linen coifs and hammer spears, klappvisor knights, gilded kastenbrust for the officers
                new Style(a("linen_coif"), body(a("chained_gambeson"), "chainmail_leggings", a("chained_gambeson_boots")),
                        a("steel_short_spear"), "iron_buckler",
                        a("klappvisor_bascinet"), body(a("xiii_century_knight_chestplate"), a("xiii_century_knight_leggings"), a("xiii_century_knight_boots")),
                        a("steel_lance"),
                        a("gilded_grand_bascinet"), body(a("ceremonial_kastenbrust_chestplate"), "kastenbrust_leggings", a("ceremonial_kastenbrust_boots")),
                        a("steel_arming_sword_type_xiii"),
                        a("royal_plume_decoration"), a("goose_decoration"), "ecranche_decoration",
                        "antiquelegacy:roman_two_feathers_decoration", "antiquelegacy:celtic_tunic_underarmor_decoration"),
                // tablet helms behind pavises, late sallets, blued gothic plate under close helmets
                new Style(a("tablet_helmet"), body(a("pikeman_chestplate"), "chainmail_leggings", a("pikeman_boots")),
                        a("steel_sabre"), "iron_pavese",
                        a("late_sallet"), body(a("splint_chestplate"), a("splint_leggings"), a("splint_boots")), a("steel_lance"),
                        a("close_helmet"), body(a("darkblued_gothic_chestplate"), a("darkblued_gothic_leggings"), a("darkblued_gothic_boots")),
                        a("steel_italian_poleaxe"),
                        a("horse_tail_decoration"), a("ram_horns_decoration"), "hood_decoration",
                        "antiquelegacy:roman_crest_alt_decoration", "antiquelegacy:iron_right_hand_manika_decoration")));
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

    /** A land's {@code turn}th look, wrapping round; Epic Knights: Addon's looks follow the land's own when it is installed. */
    public static Style style(Homeland land, int turn) {
        java.util.List<Style> looks = STYLES.getOrDefault(land, STYLES.get(Homeland.PLAINS));
        if (addonAvailable()) {
            looks = java.util.stream.Stream.concat(looks.stream(), ADDON_STYLES.getOrDefault(land, java.util.List.of()).stream()).toList();
        }
        return looks.get(Math.floorMod(turn, looks.size()));
    }

    /** Whether Epic Knights: Addon is installed. */
    public static boolean addonAvailable() {
        return ModList.get() != null && ModList.get().isLoaded(ADDON);
    }

    /** Every piece Epic Knights: Addon registers, by kind, read from the game: helmets, chestplates, clothes (light chests), weapons, decorations by slot. */
    record AddonPieces(java.util.List<String> helmets, java.util.List<String> chests, java.util.List<String> clothes,
                       java.util.List<String> weapons, java.util.List<String> headTrims, java.util.List<String> bodyTrims) {
        static final AddonPieces NONE = new AddonPieces(java.util.List.of(), java.util.List.of(), java.util.List.of(),
                java.util.List.of(), java.util.List.of(), java.util.List.of());
    }

    @Nullable
    private static AddonPieces addonPieces;

    /** The addon's pieces, gathered once; none without it. */
    static AddonPieces addonPieces() {
        if (!addonAvailable()) {
            return AddonPieces.NONE;
        }
        if (addonPieces == null) {
            AddonPieces p = new AddonPieces(new java.util.ArrayList<>(), new java.util.ArrayList<>(), new java.util.ArrayList<>(),
                    new java.util.ArrayList<>(), new java.util.ArrayList<>(), new java.util.ArrayList<>());
            try {
                Class<?> decoration = Class.forName("com.magistuarmory.item.ArmorDecoration");
                java.lang.reflect.Method type = decoration.getMethod("getType");
                for (Item item : BuiltInRegistries.ITEM) {
                    ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
                    if (!ADDON.equals(id.getNamespace())) {
                        continue;
                    }
                    String name = id.toString();
                    if (decoration.isInstance(item)) {
                        (type.invoke(item) == net.minecraft.world.item.ArmorItem.Type.HELMET ? p.headTrims() : p.bodyTrims()).add(name);
                    } else if (item instanceof net.minecraft.world.item.ArmorItem armour) {
                        if (armour.getType() == net.minecraft.world.item.ArmorItem.Type.HELMET) {
                            p.helmets().add(name);
                        } else if (armour.getType() == net.minecraft.world.item.ArmorItem.Type.CHESTPLATE) {
                            // shortcut: defence 3 or less counts as clothing (tunic, doublet, puff and slash); name them if some plate slips in
                            (armour.getDefense() <= 3 ? p.clothes() : p.chests()).add(name);
                        }
                    } else if (item instanceof net.minecraft.world.item.SwordItem && !name.contains("training")) {
                        p.weapons().add(name);
                    }
                }
            } catch (ReflectiveOperationException | RuntimeException e) {
                RafasConquest.LOGGER.warn("Could not read Epic Knights: Addon's pieces: {}", e.toString());
            }
            addonPieces = p;
        }
        return addonPieces;
    }

    /** One of {@code base} or {@code more}, all equally likely. */
    static String pick(java.util.Random r, String[] base, java.util.List<String> more) {
        int n = r.nextInt(base.length + more.size());
        return n < base.length ? base[n] : more.get(n - base.length);
    }

    /** Swaps the body for a chest piece and its matching legs and feet, where it has them (the others are kept). */
    private static void dressBody(Map<EquipmentSlot, ItemStack> slots, String chest, int colour) {
        String set = chest.endsWith("_chestplate") ? chest.substring(0, chest.length() - "_chestplate".length()) : chest;
        put(slots, EquipmentSlot.CHEST, stack(chest, colour));
        put(slots, EquipmentSlot.LEGS, stack(set + "_leggings", colour));
        put(slots, EquipmentSlot.FEET, stack(set + "_boots", colour));
    }

    /** A nation's colour (its banner's ground), as RGB. */
    public static int colour(java.util.UUID nation) {
        return rafasconquest.faction.FactionBannerNbt.baseColor(rafasconquest.faction.FactionBanner.of(nation)).getTextureDiffuseColor() & 0xFFFFFF;
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
                case 3 -> outfit(Kit.ZWEIHANDLER, land, colour(nation), banner).slots();
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
        Kit kit = kitOf(map, person);
        Outfit outfit;
        int colour;
        if (p.nation() >= 0) {
            java.util.UUID nation = map.nations().get(p.nation()).id();
            colour = colour(nation);
            outfit = outfit(kit, style(map, nation), colour, banner(who.level().registryAccess(), nation));
        } else {
            colour = 0x6B5A44;
            outfit = outfit(kit, style(map.realm().cultures().get(p.culture()).homeland(), 0), colour, null);
        }
        Map<EquipmentSlot, ItemStack> slots = new EnumMap<>(EquipmentSlot.class);
        slots.putAll(outfit.slots());
        personalise(slots, kit, colour, new java.util.Random(person * 0x9E3779B97F4A7C15L + 17));
        slots.forEach((slot, stack) -> {
            who.setItemSlot(slot, stack);
            if (who instanceof com.talhanation.recruits.entities.AbstractRecruitEntity recruit) {
                recruit.getInventory().setItem(recruit.getInventorySlotIndex(slot), stack.copy());
            }
        });
    }

    private static final String[] TASTE_HELMETS = {"armet", "armet_with_plume", "grand_bascinet", "greathelm", "bascinet",
            "sallet", "barbute", "face_helmet", "shishak", "norman_helmet", "kettlehat"};
    private static final String[] TASTE_WEAPONS = {"steel_bastardsword", "steel_estoc", "steel_claymore", "steel_zweihander",
            "steel_lochaberaxe", "steel_katzbalger", "steel_flamebladedsword", "steel_lucernhammer", "messer_sword", "noble_sword"};
    private static final String[] HEAD_TRIMS = {"big_plume_decoration", "plume_middle_decoration", "plume_left_decoration",
            "plume_right_decoration", "two_plumes_decoration", "feathers_decoration", "torse_decoration", "torse_and_mantle_decoration",
            "lion_decoration", "griffin_decoration", "dragon_decoration", "bear_decoration", "bullhorns_decoration",
            "viking_horns_decoration", "antlers_decoration", "gazelle_horns_decoration", "horse_decoration", "lily_decoration"};
    private static final String[] CHEST_TRIMS = {"hood_decoration", "ecranche_decoration", "rondel_decoration",
            "antiquelegacy:antique_cloak_decoration", "antiquelegacy:fur_cloak_decoration", "antiquelegacy:subarmalis_decoration"};
    /** Accessory dyes besides the side's own: gold, silver, black, crimson, royal blue, green, purple, white. */
    private static final int[] TRIM_COLOURS = {0xD4AF37, 0xC0C0C0, 0x1E1B18, 0x9B1B1B, 0x1F3F8F, 0x2E6B2E, 0x5B2A86, 0xEDE8DC};

    /**
     * A character's own taste on top of their kit, the same each time for the same person: some swap the helmet or the
     * weapon for one they prefer, some nobles carry a sword, and each wears none to three accessories in a colour of their own.
     * With Epic Knights: Addon every one of its helmets, harnesses, clothes, weapons and decorations is among the choices.
     */
    static void personalise(Map<EquipmentSlot, ItemStack> slots, Kit kit, int colour, java.util.Random r) {
        boolean armoured = kit == Kit.SWORD_SAINT || kit == Kit.SWORD_KING || kit == Kit.SWORD_GOD;
        AddonPieces addon = addonPieces();
        if (armoured && r.nextInt(5) < 2) {
            put(slots, EquipmentSlot.HEAD, stack(pick(r, TASTE_HELMETS, addon.helmets()), colour));
        }
        // the addon's harness for the armoured, its doublets and puff-and-slash for the rest (no draw without it, so looks stay put)
        java.util.List<String> bodies = armoured ? addon.chests() : addon.clothes();
        if (!bodies.isEmpty() && r.nextInt(5) < 2) {
            dressBody(slots, bodies.get(r.nextInt(bodies.size())), colour);
        }
        if (r.nextInt(5) < 2) { // a preferred blade, or a sword for an unarmed noble
            put(slots, EquipmentSlot.MAINHAND, stack(pick(r, TASTE_WEAPONS, addon.weapons()), -1));
            if (twoHanded(slots.get(EquipmentSlot.MAINHAND))) {
                slots.put(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            }
        }
        int trim = r.nextBoolean() ? colour : TRIM_COLOURS[r.nextInt(TRIM_COLOURS.length)];
        for (int n = r.nextInt(4); n > 0; n--) {
            if (armoured && r.nextBoolean()) {
                decorate(slots, EquipmentSlot.HEAD, pick(r, HEAD_TRIMS, addon.headTrims()), trim);
            } else {
                String piece = pick(r, CHEST_TRIMS, addon.bodyTrims());
                // the addon's body decorations sit on the chest, legs (skirts, codpieces) or feet; each takes only its own
                for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
                    decorate(slots, slot, piece, trim);
                }
            }
        }
    }

    /** Epic Knights' steel halberd, which half the heavy cavalry carries in place of a lance; null without it. */
    @Nullable
    public static ItemStack halberd() {
        return available() ? stack("steel_concavehalberd", -1) : null;
    }

    /** Musket Mod's bayonet musket and {@code n} paper cartridges; nulls without the mod. */
    @Nullable
    public static ItemStack musket() {
        return stack("musketmod:musket_with_bayonet", -1);
    }

    @Nullable
    public static ItemStack cartridges(int n) {
        ItemStack s = stack("musketmod:cartridge", -1);
        if (s != null) {
            s.setCount(Math.min(n, s.getMaxStackSize()));
        }
        return s;
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
            case LEVY -> {
                // A gambeson and a pike; the head keeps its cap in the side's colour.
                wear(slots, null, body("gambeson_chestplate", null, "gambeson_boots"), colour, banner);
                hold(slots, "iron_pike", null, banner);
                slots.put(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            }
            case HEAVY_INFANTRY -> {
                // Chainmail under a kettle hat, a pike and the land's shield.
                wear(slots, "kettlehat", body("chainmail_chestplate", "chainmail_leggings", "chainmail_boots"), colour, banner);
                hold(slots, "iron_pike", s.shield(), banner);
            }
            case SKIRMISHER -> {
                // The heavy infantry's harness, a sword and no shield.
                wear(slots, "kettlehat", body("chainmail_chestplate", "chainmail_leggings", "chainmail_boots"), colour, banner);
                hold(slots, "iron_shortsword", null, banner);
                slots.put(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            }
            case ARCHER -> {
                wear(slots, null, body("gambeson_chestplate", null, "gambeson_boots"), colour, banner);
                slots.remove(EquipmentSlot.MAINHAND); // keeps its bow
            }
            case MUSKETEER -> {
                // A gambeson under a kettle hat; keeps its bayonet musket.
                wear(slots, "kettlehat", body("gambeson_chestplate", null, "gambeson_boots"), colour, banner);
                slots.remove(EquipmentSlot.MAINHAND);
                slots.put(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            }
            case HEAVY_CAVALRY -> {
                // The frog-mouthed jousting harness and a lance (the battle gives half of them steel halberds).
                wear(slots, "stechhelm", body("jousting_chestplate", "jousting_leggings", "jousting_boots"), colour, banner);
                decorate(slots, EquipmentSlot.HEAD, s.lineCrest(), colour);
                hold(slots, "iron_chivalrylance", null, banner);
                slots.put(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            }
            case ZWEIHANDLER -> {
                // Knights on foot, in every land: a sallet and knightly harness, an iron zweihänder in both hands.
                wear(slots, "sallet", body("knight_chestplate", "knight_leggings", "knight_boots"), colour, banner);
                decorate(slots, EquipmentSlot.HEAD, s.lineCrest(), colour);
                hold(slots, "iron_zweihander", null, banner);
                slots.put(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            }
            case HALBERDIER -> {
                // Plate under a bascinet, a steel halberd in both hands.
                wear(slots, "bascinet", body("platemail_chestplate", "platemail_leggings", "platemail_boots"), colour, banner);
                decorate(slots, EquipmentSlot.HEAD, s.lineCrest(), colour);
                hold(slots, "steel_concavehalberd", null, banner);
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
                // Full plate under the land's helmet, and a lance.
                wear(slots, s.cavalryHelmet(), body("platemail_chestplate", "platemail_leggings", "platemail_boots"), colour, banner);
                decorate(slots, EquipmentSlot.HEAD, s.lineCrest(), colour);
                hold(slots, "iron_chivalrylance", null, banner);
                slots.put(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            }
            case CAPTAIN -> {
                // Their body armour; their head keeps the banner they lead by.
                wear(slots, null, s.infantryBody(), colour, banner);
            }
            case GENERAL -> {
                wear(slots, s.officerHelmet(), s.officerBody(), colour, banner);
                decorate(slots, EquipmentSlot.HEAD, s.crest(), colour);
                decorate(slots, EquipmentSlot.CHEST, s.officerChest(), colour);
                decorate(slots, EquipmentSlot.CHEST, s.antiqueChest(), colour); // nothing without Antique Legacy
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
                decorate(slots, EquipmentSlot.HEAD, "big_plume_decoration", colour);
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
            decorate(slots, EquipmentSlot.HEAD, s.lineCrest(), colour);
            hold(slots, s.officerWeapon(), s.shield(), banner);
        } else {
            wear(slots, s.officerHelmet(), s.officerBody(), colour, banner);
            decorate(slots, EquipmentSlot.HEAD, s.crest(), colour);
            decorate(slots, EquipmentSlot.CHEST, s.officerChest(), colour);
            decorate(slots, EquipmentSlot.CHEST, s.antiqueChest(), colour); // nothing without Antique Legacy
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
        if (twoHanded(slots.get(EquipmentSlot.MAINHAND))) {
            slots.put(EquipmentSlot.OFFHAND, ItemStack.EMPTY); // both hands on the weapon: no shield
            return;
        }
        ItemStack s = stack(shield, -1);
        if (s != null) {
            herald(s, banner);
        }
        put(slots, EquipmentSlot.OFFHAND, s);
    }

    private static Class<?> weaponClass;
    private static java.lang.reflect.Field weaponType;
    private static java.lang.reflect.Method twoHandedLevel;

    /** Whether Epic Knights marks this weapon Two-Handed (I or II): nothing may be held in the other hand. */
    public static boolean twoHanded(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty() || !available()) {
            return false;
        }
        try {
            if (weaponClass == null) {
                weaponType = Class.forName("com.magistuarmory.item.MedievalWeaponItem").getField("type");
                twoHandedLevel = weaponType.getType().getMethod("getTwoHanded");
                weaponClass = weaponType.getDeclaringClass();
            }
            return weaponClass.isInstance(stack.getItem()) && (int) twoHandedLevel.invoke(weaponType.get(stack.getItem())) > 0;
        } catch (ReflectiveOperationException | RuntimeException e) {
            return false;
        }
    }

    /**
     * A player holding a two-handed weapon has their off-hand item put back in their pack (dropped when it is full),
     * unless their Strength is high enough to hold it in one hand.
     */
    @net.neoforged.bus.api.SubscribeEvent
    public static void onPlayerTick(net.neoforged.neoforge.event.tick.PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player) || player.tickCount % 5 != 0
                || player.getOffhandItem().isEmpty() || !twoHanded(player.getMainHandItem())
                || rafasconquest.peerage.WeaponSkills.unhindered(player)) {
            return;
        }
        ItemStack off = player.getOffhandItem();
        player.setItemInHand(net.minecraft.world.InteractionHand.OFF_HAND, ItemStack.EMPTY);
        if (!player.getInventory().add(off)) {
            player.drop(off, false);
        }
        player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                player.getMainHandItem().getHoverName().getString() + " takes both hands"), true);
    }

    /** An Epic Knights decoration on the piece worn in {@code slot} (a plume on a helmet, a rondel on a chest), dyed the side's colour. */
    private static void decorate(Map<EquipmentSlot, ItemStack> slots, EquipmentSlot slot, @Nullable String decoration, int colour) {
        ItemStack piece = slots.get(slot);
        ItemStack crest = stack(decoration, colour);
        if (piece == null || crest == null) {
            return;
        }
        try {
            Class<?> type = Class.forName("com.magistuarmory.item.ArmorDecoration");
            // isApplicableForDecoration: armour of the decoration's own slot, with room for one more
            if (type.isInstance(crest.getItem())
                    && (boolean) type.getMethod("isApplicableForDecoration", ItemStack.class).invoke(crest.getItem(), piece)) {
                type.getMethod("decorate", ItemStack.class, ItemStack.class).invoke(crest.getItem(), piece, crest);
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            RafasConquest.LOGGER.debug("Could not decorate {} with {}: {}", piece, crest, e.toString());
        }
    }

    /**
     * A warhorse's Epic Knights armour: chainmail for the heavy (guards, generals), cloth barding for the rest, with
     * the side's banner as its caparison. Null without Epic Knights.
     */
    @Nullable
    public static ItemStack barding(boolean heavy, @Nullable ItemStack banner) {
        if (!available()) {
            return null;
        }
        ItemStack armour = stack(heavy ? "chainmail_horse_armor" : "barding", -1);
        if (armour != null) {
            herald(armour, banner);
        }
        return armour;
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
        for (ResourceLocation id : new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath("better_weaponry", "iron_glaive"), ResourceLocation.fromNamespaceAndPath(MOD, "iron_guisarme")}) {
            Item item = BuiltInRegistries.ITEM.getOptional(id).orElse(null);
            if (item != null && item != Items.AIR) {
                return new ItemStack(item);
            }
        }
        return null;
    }

    /** An Epic Knights item by name (or another mod's, as {@code mod:name}), dyed {@code colour} if it takes a dye (and colour is not -1); null if there is none such. */
    @Nullable
    static ItemStack stack(@Nullable String name, int colour) {
        if (name == null) {
            return null;
        }
        Item item = BuiltInRegistries.ITEM.getOptional(name.indexOf(':') >= 0 ? ResourceLocation.parse(name) : ResourceLocation.fromNamespaceAndPath(MOD, name)).orElse(null);
        if (item == null || item == Items.AIR) {
            return null;
        }
        ItemStack stack = new ItemStack(item);
        if (colour >= 0 && stack.is(net.minecraft.tags.ItemTags.DYEABLE)) {
            stack.set(net.minecraft.core.component.DataComponents.DYED_COLOR, new net.minecraft.world.item.component.DyedItemColor(colour & 0xFFFFFF, true));
        }
        return stack;
    }

    /** Puts a banner's design on a surcoat or a shield, as Epic Knights' heraldry (and vanilla's shields) keep it. */
    static void herald(ItemStack stack, @Nullable ItemStack banner) {
        if (banner == null || !(banner.getItem() instanceof BannerItem bannerItem)) {
            return;
        }
        stack.set(net.minecraft.core.component.DataComponents.BANNER_PATTERNS,
                banner.getOrDefault(net.minecraft.core.component.DataComponents.BANNER_PATTERNS, net.minecraft.world.level.block.entity.BannerPatternLayers.EMPTY));
        stack.set(net.minecraft.core.component.DataComponents.BASE_COLOR, bannerItem.getColor());
    }
}
