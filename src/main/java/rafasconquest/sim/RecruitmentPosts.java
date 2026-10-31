package rafasconquest.sim;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import rafasconquest.bridge.RecruitsBridge;
import rafasconquest.core.CityLayout;
import rafasconquest.core.RecruitmentPost;
import rafasconquest.data.ConquestSavedData;
import rafasconquest.faction.Faction;
import rafasconquest.npc.ConquestEntities;
import rafasconquest.npc.Recruiter;
import rafasconquest.worldgen.CityGridBuilder;

/**
 * Sets each recruitment post's people at their places ({@link RecruitmentPost}) when its chunk first loads: the five
 * guards of the city's owner, and the recruiter behind the counter, all facing the door (west). Each place is filled
 * once (and remembered), like the city walls' guards.
 */
public final class RecruitmentPosts {
    /** West: toward the door and the road beyond it. */
    private static final float FACING = 90.0F;

    private RecruitmentPosts() {
    }

    /** Returns how many were set at their places. */
    static int postChunk(ServerLevel level, LevelChunk chunk, ConquestSavedData data, PoiPrecomputer precomputer, RecruitsBridge recruits) {
        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();
        int r = CityLayout.MAX_HALF_EXTENT;
        int placed = 0;
        for (CityLayout.Site site : CityLayout.allSitesInBox(level.getSeed(), data.settings(), minX - r, minZ - r, minX + 15 + r, minZ + 15 + r)) {
            RecruitmentPost post = RecruitmentPost.of(site);
            if (post.x1() < minX || post.plotX() > minX + 15 || post.z1() < minZ || post.z0() > minZ + 15 || !CityGridBuilder.heldByANation(site)) {
                continue;
            }
            Faction owner = null;
            for (int[] spot : post.guards()) {
                BlockPos at = standing(chunk, spot[0], spot[1]);
                if (at == null || data.guardSpawned(at)) {
                    continue;
                }
                owner = owner != null ? owner : CityBanners.owner(data, precomputer, site);
                if (owner != null && recruits.spawnPostGuard(owner, at, FACING)) {
                    data.markGuardSpawned(at);
                    placed++;
                }
            }
            int[] spot = post.recruiter();
            BlockPos at = standing(chunk, spot[0], spot[1]);
            if (at != null && !data.guardSpawned(at)) {
                owner = owner != null ? owner : CityBanners.owner(data, precomputer, site);
                Recruiter recruiter = owner == null ? null : ConquestEntities.RECRUITER.get().create(level);
                if (recruiter != null) {
                    recruiter.post(owner.id(), owner.name(), at.getX() + 0.5, at.getY(), at.getZ() + 0.5, FACING,
                            Math.floorMod(site.x() * 31 + site.z(), 20));
                    // An officer: armoured in the nation's style (with Epic Knights).
                    rafasconquest.gear.Armoury.dress(recruiter, rafasconquest.gear.Armoury.Kit.CAPTAIN, rafasconquest.worldgen.ConquestRuntime.map(), owner.id());
                    level.addFreshEntity(recruiter);
                    data.markGuardSpawned(at);
                    placed++;
                }
            }
        }
        return placed + nobles(level, chunk, data, precomputer);
    }

    /** The chunks holding a castle's keep (its lord's place), for the map they were worked out for. */
    private static volatile java.util.Set<Long> keepChunks = java.util.Set.of();
    private static volatile rafasconquest.map.BaseMap keepsOf;

    /** Whether a castle hold's lord stands in this chunk. Cheap; safe on any thread. */
    public static boolean holdInChunk(int chunkX, int chunkZ) {
        rafasconquest.map.BaseMap map = rafasconquest.worldgen.ConquestRuntime.map();
        if (map == null) {
            return false;
        }
        if (keepsOf != map) {
            java.util.Set<Long> keeps = new java.util.HashSet<>();
            for (rafasconquest.people.Person p : map.realm().people()) {
                if (cityCells(map, p.hold()) < 0) {
                    keeps.add(ConquestSavedData.xzKey(p.hold().x() >> 4, p.hold().z() >> 4));
                }
            }
            keepChunks = keeps;
            keepsOf = map;
        }
        return keepChunks.contains(ConquestSavedData.xzKey(chunkX, chunkZ));
    }

    /**
     * The holds' nobles, at their places once (as long as no player has taken the hold): a city's ruler in its gold
     * church facing the door, with the city's own lord beside them in a duchy's or a nation's capital; a castle's lord
     * in the middle of the keep's ground floor.
     */
    private static int nobles(ServerLevel level, LevelChunk chunk, ConquestSavedData data, PoiPrecomputer precomputer) {
        rafasconquest.map.BaseMap map = rafasconquest.worldgen.ConquestRuntime.map();
        if (map == null) {
            return 0;
        }
        rafasconquest.peerage.Peerage peerage = rafasconquest.peerage.Peerage.get(level.getServer());
        int placed = 0;
        for (int i = 0; i < map.realm().people().size(); i++) {
            rafasconquest.people.Person p = map.realm().people().get(i);
            rafasconquest.people.Person.Hold hold = p.hold();
            // Not where a player has taken the hold, nor where it was conquered in war.
            if (p.nation() < 0 || peerage.replaced(hold.province()) && hold.kind() != rafasconquest.people.Person.Kind.LORD
                    || map.holders()[hold.province()] != p.nation()) {
                continue;
            }
            int[] spot;
            float yaw;
            int cells = cityCells(map, hold);
            if (cells < 0) {
                spot = new int[]{hold.x(), hold.z()};
                yaw = 0;
            } else {
                CityLayout.Site site = new CityLayout.Site(hold.x(), hold.z(), false, cells);
                int[] plot = RecruitmentPost.of(site).church();
                if (Math.abs((plot[0] >> 4) - chunk.getPos().x) > 1 || Math.abs((plot[1] >> 4) - chunk.getPos().z) > 1) {
                    continue; // the church lies in other chunks
                }
                // Only where it stands across the plot matters here; the floor is found in the column below.
                java.util.Optional<rafasconquest.worldgen.GoldChurch.Layout> church = rafasconquest.worldgen.GoldChurch.layout(
                        level.getStructureManager(), plot[0], plot[1], 0);
                if (church.isEmpty()) {
                    continue;
                }
                spot = hold.kind() == rafasconquest.people.Person.Kind.LORD ? church.get().lord() : church.get().ruler();
                yaw = FACING; // toward the door
            }
            if (spot[0] >> 4 != chunk.getPos().x || spot[1] >> 4 != chunk.getPos().z) {
                continue;
            }
            BlockPos at = floor(chunk, spot[0], spot[1]);
            if (at == null || data.guardSpawned(at)) {
                continue;
            }
            Recruiter noble = ConquestEntities.RECRUITER.get().create(level);
            if (noble == null) {
                continue;
            }
            noble.noble(i, rafasconquest.peerage.Names.styled(map, p), map.nations().get(p.nation()).id(), at.getX() + 0.5, at.getY(),
                    at.getZ() + 0.5, yaw, Math.floorMod(hold.x() * 17 + hold.z() * 3, 20));
            rafasconquest.gear.Armoury.dressCharacter(noble, map, i); // crown, ceremonial dress or sword-titled harness
            level.addFreshEntity(noble);
            data.markGuardSpawned(at);
            placed++;
        }
        return placed;
    }

    /** A hold's city's size, in districts a side (from the map's cities); -1 if it stands in no city (a castle). */
    private static int cityCells(rafasconquest.map.BaseMap map, rafasconquest.people.Person.Hold hold) {
        for (rafasconquest.core.SeatCities.City c : map.cities()) {
            if (c.province() == hold.province() && c.x() == hold.x() && c.z() == hold.z()) {
                return c.districts();
            }
        }
        return -1;
    }

    /** The lowest standing room under the top of a column (within {@link #FLOOR_SCAN} blocks): a building's ground floor. */
    private static BlockPos floor(LevelChunk chunk, int x, int z) {
        int top = chunk.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x & 15, z & 15) + 1;
        BlockPos found = null;
        for (int y = top; y > top - FLOOR_SCAN; y--) {
            BlockState below = chunk.getBlockState(new BlockPos(x, y - 1, z));
            if (chunk.getBlockState(new BlockPos(x, y, z)).isAir() && chunk.getBlockState(new BlockPos(x, y + 1, z)).isAir()
                    && !below.isAir() && below.getFluidState().isEmpty()) {
                found = new BlockPos(x, y, z);
            }
        }
        return found;
    }

    private static final int FLOOR_SCAN = 18;

    /**
     * Where someone stands at (x, z), if that column is in the chunk: on the floor under the post's roof, or on the
     * ground outside it.
     */
    private static BlockPos standing(LevelChunk chunk, int x, int z) {
        if (x >> 4 != chunk.getPos().x || z >> 4 != chunk.getPos().z) {
            return null;
        }
        int y = chunk.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x & 15, z & 15);
        BlockState top = chunk.getBlockState(new BlockPos(x, y, z));
        if (top.is(Blocks.STONE_BRICK_SLAB)) {
            // Under the roof: down through the room to its floor.
            int down = y - 1;
            while (down > chunk.getMinBuildHeight() && chunk.getBlockState(new BlockPos(x, down, z)).isAir()) {
                down--;
            }
            return new BlockPos(x, down + 1, z);
        }
        return new BlockPos(x, y + 1, z);
    }
}
