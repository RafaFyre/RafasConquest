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
final class RecruitmentPosts {
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
                    level.addFreshEntity(recruiter);
                    data.markGuardSpawned(at);
                    placed++;
                }
            }
        }
        return placed + nobles(level, chunk, data, precomputer);
    }

    /**
     * The holds' nobles, at their places once (as long as no player has taken the hold): a city's at its recruitment
     * post, at the back of the room facing it; a castle's lord in the middle of the keep's ground floor.
     */
    private static int nobles(ServerLevel level, LevelChunk chunk, ConquestSavedData data, PoiPrecomputer precomputer) {
        rafasconquest.map.BaseMap map = rafasconquest.worldgen.ConquestRuntime.map();
        if (map == null) {
            return 0;
        }
        rafasconquest.peerage.Peerage peerage = rafasconquest.peerage.Peerage.get(level.getServer());
        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();
        int placed = 0;
        for (int i = 0; i < map.realm().people().size(); i++) {
            rafasconquest.people.Person p = map.realm().people().get(i);
            rafasconquest.people.Person.Hold hold = p.hold();
            if (p.nation() < 0 || peerage.replaced(hold.province())) {
                continue;
            }
            int[] spot;
            float yaw;
            if (hold.kind() == rafasconquest.people.Person.Kind.CASTLE) {
                spot = new int[]{hold.x(), hold.z()};
                yaw = 0;
            } else {
                CityLayout.Site site = new CityLayout.Site(hold.x(), hold.z(), false, cityCells(map, hold));
                spot = RecruitmentPost.of(site).noble();
                yaw = 0; // south, into the room
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
            level.addFreshEntity(noble);
            data.markGuardSpawned(at);
            placed++;
        }
        return placed;
    }

    /** A city hold's size, in districts a side (from the map's cities). */
    private static int cityCells(rafasconquest.map.BaseMap map, rafasconquest.people.Person.Hold hold) {
        for (rafasconquest.core.SeatCities.City c : map.cities()) {
            if (c.province() == hold.province()) {
                return c.districts();
            }
        }
        return CityLayout.CELLS_PER_SIDE;
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
