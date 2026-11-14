package rafasconquest.style;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;
import rafasconquest.bridge.RecruitsFactions;
import rafasconquest.content.ConquestContent;
import rafasconquest.faction.Faction;
import rafasconquest.map.BaseMap;
import rafasconquest.npc.ConquestEntities;
import rafasconquest.npc.Recruiter;
import rafasconquest.people.Organization;
import rafasconquest.people.Person;
import rafasconquest.people.Personality;
import rafasconquest.people.Realm;
import rafasconquest.people.Stat;
import rafasconquest.sim.ConquestServer;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Character Markers ({@link MarkerBlock}) at work: armed as world generation builds them, and once their chunk is in the
 * world, becoming whom they name, for whoever holds the land they stand on (its province's holder): soldiers of its
 * nation; its recruiter; or a person of the realm, chosen by the marker's filters (the best fit not yet placed
 * elsewhere, unless it may be in two places at once). A Sword God, King or Saint is of the sanctum this place belongs to
 * (else the nearest): the rank asked for, or the next below it if no one of it is free.
 */
public final class CharacterMarkers {
    /** How far a marker looks for another marker of its build, by its label. */
    private static final int LABEL_REACH = 128;

    private CharacterMarkers() {
    }

    /** As a structure is built: its markers in this chunk are armed, keeping their settings. */
    public static void arm(WorldGenLevel level, StructureTemplate template, BlockPos origin, StructurePlaceSettings settings, BoundingBox chunk) {
        for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(origin, settings, ConquestContent.MARKER.get())) {
            if (!chunk.isInside(info.pos())) {
                continue;
            }
            BlockState placed = level.getBlockState(info.pos());
            if (!placed.is(ConquestContent.MARKER.get())) {
                continue;
            }
            level.setBlock(info.pos(), placed.setValue(MarkerBlock.ARMED, true), 2);
            if (info.nbt() != null && level.getBlockEntity(info.pos()) instanceof MarkerBlockEntity marker) {
                marker.loadWithComponents(info.nbt(), level.registryAccess());
            }
        }
    }

    /** An armed marker fires: it becomes its people and then its block; or waits (on the map, or a marker it refers to). */
    static void fire(ServerLevel level, BlockPos pos, BlockState state, MarkerSpec spec, int age) {
        BaseMap map = ConquestRuntime.map();
        ConquestServer conquest = ConquestServer.get();
        if (map == null || conquest == null) {
            return;
        }
        int province = map.provinceIndexAt(pos.getX(), pos.getZ());
        int nation = province < 0 ? -1 : map.holders()[province];
        Faction owner = nation < 0 ? null : conquest.data().faction(map.nations().get(nation).id());
        float yaw = MarkerBlock.yaw(state);
        RecruitsFactions recruits = conquest.recruits() instanceof RecruitsFactions r ? r : null;
        Placed placed = Placed.get(level);
        switch (spec.type()) {
            case "BOWMAN", "SHIELDMAN", "KNIGHT", "CAVALRY" -> {
                if (owner != null && recruits != null) {
                    for (int i = 0; i < spec.count(); i++) {
                        recruits.spawnMarkerSoldier(owner, spec.type(), spec.kit(), around(pos, i), yaw, spec.patrol());
                    }
                }
            }
            case "RECRUITER" -> {
                Recruiter recruiter = owner == null ? null : ConquestEntities.RECRUITER.get().create(level);
                if (recruiter != null) {
                    recruiter.post(owner.id(), owner.name(), pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, yaw, Math.floorMod(pos.getX() * 31 + pos.getZ(), 20));
                    rafasconquest.gear.Armoury.dress(recruiter, rafasconquest.gear.Armoury.Kit.CAPTAIN, map, owner.id());
                    level.addFreshEntity(recruiter);
                }
            }
            case "SWORD_GOD", "SWORD_KING", "SWORD_SAINT", "NAMED", "CITY_HOLDER" -> {
                int person;
                if (spec.type().equals("CITY_HOLDER")) {
                    // Whoever holds this place: its ruler, duke, count or lord.
                    int holder = province < 0 ? -1 : map.realm().holderOf(province);
                    person = holder >= 0 && (spec.twoPlaces() || !placed.has(holder)) ? holder : -1;
                } else if (spec.type().equals("NAMED")) {
                    Integer chosen = named(map, spec, pos, province, nation, placed, age);
                    if (chosen == null) {
                        return; // waiting on a marker it refers to
                    }
                    person = chosen;
                } else {
                    person = sworn(map, spec, pos, nation, placed);
                }
                if (person >= 0) {
                    character(level, map, person, pos, yaw);
                    placed.place(person, spec.label(), pos);
                } else if (spec.ifNone().equals("GUARD") && owner != null && recruits != null) {
                    recruits.spawnMarkerSoldier(owner, "SHIELDMAN", 2, pos, yaw, false);
                }
            }
            default -> {
                return; // Quest givers and shop owners are yet to come: it waits for them, unseen
            }
        }
        BlockState after = Blocks.AIR.defaultBlockState();
        if (!spec.replace().isEmpty()) {
            ResourceLocation id = ResourceLocation.tryParse(spec.replace());
            if (id != null && BuiltInRegistries.BLOCK.containsKey(id)) {
                after = BuiltInRegistries.BLOCK.get(id).defaultBlockState();
            }
        }
        level.setBlock(pos, after, 3);
    }

    /** The i-th soldier's place: the marker, then the blocks round it. */
    private static BlockPos around(BlockPos pos, int i) {
        int[][] ring = {{0, 0}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        return pos.offset(ring[i % ring.length][0], 0, ring[i % ring.length][1]);
    }

    private static void character(ServerLevel level, BaseMap map, int person, BlockPos pos, float yaw) {
        Person p = map.realm().people().get(person);
        Recruiter noble = ConquestEntities.RECRUITER.get().create(level);
        if (noble == null || p.nation() < 0) {
            return;
        }
        noble.noble(person, rafasconquest.peerage.Names.styled(map, p), map.nations().get(p.nation()).id(), pos.getX() + 0.5, pos.getY(),
                pos.getZ() + 0.5, yaw, Math.floorMod(person * 7, 20));
        rafasconquest.gear.Armoury.dressCharacter(noble, map, person);
        level.addFreshEntity(noble);
    }

    /** A Sword God, King or Saint of this place's sanctum (else the nearest), stepping down a rank if none is free; -1 if none. */
    private static int sworn(BaseMap map, MarkerSpec spec, BlockPos pos, int nation, Placed placed) {
        Realm realm = map.realm();
        Organization sanctum = null;
        double best = Double.MAX_VALUE;
        for (Organization o : realm.organizations()) {
            BaseMap.Nation n = map.nations().get(o.nation());
            double d = o.nation() == nation ? -1 : Math.hypot(n.x() - pos.getX(), n.z() - pos.getZ());
            if (d < best) {
                best = d;
                sanctum = o;
            }
        }
        if (sanctum == null) {
            return -1;
        }
        List<List<Integer>> ranks = List.of(List.of(sanctum.head()), sanctum.kings(), sanctum.saints());
        int from = spec.type().equals("SWORD_GOD") ? 0 : spec.type().equals("SWORD_KING") ? 1 : 2;
        for (int r = from; r < ranks.size(); r++) {
            int chosen = -1;
            for (int i : ranks.get(r)) {
                if (i >= 0 && (spec.twoPlaces() || !placed.has(i))
                        && (chosen < 0 || realm.people().get(i).stat(Stat.PROWESS) > realm.people().get(chosen).stat(Stat.PROWESS))) {
                    chosen = i;
                }
            }
            if (chosen >= 0) {
                return chosen;
            }
        }
        return -1;
    }

    /** The person a Named Character marker names: -1 if no one fits; null while it waits on the marker it refers to. */
    @Nullable
    private static Integer named(BaseMap map, MarkerSpec spec, BlockPos pos, int province, int nation, Placed placed, int age) {
        Realm realm = map.realm();
        int other = -1;
        if (!spec.relation().equals("NONE")) {
            other = switch (spec.other()) {
                case "place" -> province < 0 ? -1 : realm.holderOf(province);
                case "nation" -> nation < 0 ? -1 : realm.ruler(nation);
                default -> placed.labelled(spec.other(), pos);
            };
            if (other < 0 && !spec.other().equals("place") && !spec.other().equals("nation") && age < MarkerBlockEntity.PATIENCE) {
                return null;
            }
        }
        Personality trait = parse(Personality.class, spec.trait());
        Stat stat = parse(Stat.class, spec.stat());
        int chosen = -1;
        double chosenScore = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < realm.people().size(); i++) {
            Person p = realm.people().get(i);
            if (p.hold().kind() == Person.Kind.DEAD || nation >= 0 && p.nation() != nation || !spec.twoPlaces() && placed.has(i) || !ranked(realm, spec.rank(), i, p)) {
                continue;
            }
            if (other >= 0 && !related(realm, spec.relation(), i, other)) {
                continue;
            }
            if (trait != null && !p.has(trait) || stat != null && spec.statMin() >= 0 && p.stat(stat) < spec.statMin()) {
                continue;
            }
            double score = stat != null && spec.statMin() < 0 ? p.stat(stat)
                    : p.hold() == null ? -1e9 : -Math.hypot(p.hold().x() - pos.getX(), p.hold().z() - pos.getZ());
            if (score > chosenScore) {
                chosenScore = score;
                chosen = i;
            }
        }
        return chosen;
    }

    private static boolean ranked(Realm realm, String rank, int i, Person p) {
        if (rank.equals("ANY")) {
            return true;
        }
        if (rank.equals("RULER")) {
            return p.nation() >= 0 && realm.ruler(p.nation()) == i;
        }
        if (p.hold() == null) {
            return false;
        }
        return switch (rank) {
            case "CAPITAL_LORD" -> p.hold().kind() == Person.Kind.LORD;
            case "CITY" -> p.hold().kind() == Person.Kind.CITY || p.hold().kind() == Person.Kind.CAPITAL;
            case "TOWN" -> p.hold().kind() == Person.Kind.TOWN;
            case "CASTLE" -> p.hold().kind() == Person.Kind.CASTLE;
            default -> true;
        };
    }

    private static boolean related(Realm realm, String relation, int i, int other) {
        return switch (relation) {
            case "LIEGE_OF" -> realm.people().get(other).liege() == i;
            case "VASSAL_OF" -> realm.people().get(i).liege() == other;
            case "FRIEND_OF" -> realm.bond(i, other) == Realm.Bond.Kind.FRIEND;
            case "RIVAL_OF" -> realm.bond(i, other) == Realm.Bond.Kind.RIVAL;
            default -> true;
        };
    }

    @Nullable
    private static <E extends Enum<E>> E parse(Class<E> type, String name) {
        try {
            return name.isEmpty() ? null : Enum.valueOf(type, name.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /** Who has been placed by a marker, and by which labelled ones, where. */
    static final class Placed extends SavedData {
        private static final String NAME = "rafasconquest_markers";
        private final Set<Integer> people = new HashSet<>();
        private final List<CompoundTag> labels = new ArrayList<>();

        static Placed get(ServerLevel level) {
            return rafasconquest.data.SaveGuard.get(level.getServer(), NAME, Placed::new, Placed::load);
        }

        boolean has(int person) {
            return people.contains(person);
        }

        void place(int person, String label, BlockPos pos) {
            people.add(person);
            if (!label.isEmpty()) {
                CompoundTag t = new CompoundTag();
                t.putString("label", label);
                t.putInt("x", pos.getX());
                t.putInt("z", pos.getZ());
                t.putInt("person", person);
                labels.add(t);
            }
            setDirty();
        }

        /** The person placed by the nearest marker of this label within reach, or -1. */
        int labelled(String label, BlockPos pos) {
            int found = -1;
            double best = LABEL_REACH;
            for (CompoundTag t : labels) {
                double d = Math.hypot(t.getInt("x") - pos.getX(), t.getInt("z") - pos.getZ());
                if (t.getString("label").equals(label) && d <= best) {
                    best = d;
                    found = t.getInt("person");
                }
            }
            return found;
        }

        static Placed load(CompoundTag tag) {
            Placed p = new Placed();
            for (int i : tag.getIntArray("people")) {
                p.people.add(i);
            }
            for (Tag t : tag.getList("labels", Tag.TAG_COMPOUND)) {
                p.labels.add((CompoundTag) t);
            }
            return p;
        }

        @Override
        public CompoundTag save(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
            tag.putIntArray("people", people.stream().mapToInt(Integer::intValue).toArray());
            ListTag list = new ListTag();
            list.addAll(labels);
            tag.put("labels", list);
            return tag;
        }
    }
}
