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
        return placed;
    }

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
