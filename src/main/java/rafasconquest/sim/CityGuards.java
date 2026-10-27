package rafasconquest.sim;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import rafasconquest.bridge.RecruitsBridge;
import rafasconquest.core.CityLayout;
import rafasconquest.data.ConquestSavedData;
import rafasconquest.dev.WallTemplates;
import rafasconquest.faction.Faction;

/**
 * Posts 3 archers of the city owner's faction on the walkway of every wall segment, facing out. Runs when a wall
 * chunk loads (the first load after generation included); each post gets its guard once, recorded in save data, so a
 * guard that dies is not replaced. Server thread only.
 */
final class CityGuards {
    private CityGuards() {
    }

    /** Returns how many guards were spawned. */
    static int postChunk(ServerLevel level, LevelChunk chunk, ConquestSavedData data, PoiPrecomputer precomputer, RecruitsBridge recruits) {
        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();
        int r = CityLayout.HALF_EXTENT + CityLayout.WALL_OUTSET + 1;
        int spawned = 0;
        for (CityLayout.Site site : CityLayout.allSitesInBox(level.getSeed(), data.settings(), minX - r, minZ - r, minX + 15 + r, minZ + 15 + r)) {
            for (CityLayout.WallSegment seg : CityLayout.wallSegments()) {
                // A corner segment's posts sit as on any other segment, past its lead.
                int lead = seg.length() - WallTemplates.LENGTH;
                for (int postX : WallTemplates.GUARD_X) {
                    int[] local = CityLayout.wallBlock(seg, lead + postX, WallTemplates.WALKWAY_Z);
                    int x = site.originX() + local[0];
                    int z = site.originZ() + local[1];
                    if (x < minX || x > minX + 15 || z < minZ || z > minZ + 15) {
                        continue;
                    }
                    // The walkway is the first free block above the wall's body.
                    int top = chunk.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x & 15, z & 15);
                    BlockPos post = new BlockPos(x, top + 1, z);
                    if (data.guardSpawned(post) || !isWall(chunk.getBlockState(post.below()))) {
                        continue;
                    }
                    Faction owner = CityBanners.owner(data, precomputer, site);
                    if (owner != null && recruits.spawnWallGuard(owner, post, outwardYaw(seg.side()))) {
                        data.markGuardSpawned(post);
                        spawned++;
                    }
                }
            }
        }
        return spawned;
    }

    /** Wall material, after its processor list: stone brick variants, or cut sandstone in the desert. */
    private static boolean isWall(BlockState state) {
        return state.is(BlockTags.STONE_BRICKS) || state.is(Blocks.CUT_SANDSTONE);
    }

    /** Minecraft yaw facing away from the city: 0 south, 90 west, 180 north, -90 east. */
    private static float outwardYaw(CityLayout.Side side) {
        return switch (side) {
            case NORTH -> 180;
            case EAST -> -90;
            case SOUTH -> 0;
            case WEST -> 90;
        };
    }
}
