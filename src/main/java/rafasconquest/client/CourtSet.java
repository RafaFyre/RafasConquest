package rafasconquest.client;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import rafasconquest.people.Homeland;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A nation's court, built in blocks for the nation and character pages to stand their people in (never placed in the
 * world): a hall in the style of the nation's land, its floor, back wall and pillars, a carpet in the nation's colour up
 * the middle, and a stepped pyramid of podiums: the ruler's highest at the back, the three greatest nobles' below it, then
 * two rows of lesser ones, each lower and nearer. Generated for now; meant to be replaced by built ones later.
 *
 * <p>Coordinates: x across (0 the middle), y up (the floor's top at 0), z toward the viewer (the back wall at
 * {@link #BACK}).
 */
final class CourtSet {
    /** Seats, by row: the ruler, the greatest nobles, then the two rows of lesser ones. */
    static final int[] ROW_SEATS = {1, 3, 9, 8};
    static final int MAX_SEATS = 21;
    static final int HALF_WIDTH = 15;
    static final int BACK = -14;
    static final int FRONT = 4;
    static final int WALL_HEIGHT = 12;
    private static final int[] ROW_Z = {-10, -7, -4, -1};
    private static final int[] ROW_HEIGHT = {4, 3, 2, 1};

    /** Where someone stands: their feet's middle. */
    record Seat(double x, double y, double z) {
    }

    /** A hanging banner on the back wall: the top of its pole's middle. */
    record BannerSpot(double x, double y, double z) {
    }

    /** A land's materials, and the colour behind the hall. */
    record Style(BlockState floor, BlockState floorAlt, BlockState wall, BlockState trim, BlockState pillar, BlockState podium,
                 BlockState podiumTop, BlockState decor, int sky) {
    }

    final Map<BlockPos, BlockState> blocks = new LinkedHashMap<>();
    final List<Seat> seats = new ArrayList<>();
    final List<BannerSpot> banners = new ArrayList<>();
    final Style style;

    private CourtSet(Style style) {
        this.style = style;
    }

    /** The court of a nation of {@code homeland}'s style, its carpet {@code carpet}, with room for {@code people} people. */
    static CourtSet build(Homeland homeland, DyeColor carpet, int people) {
        CourtSet set = new CourtSet(style(homeland));
        set.hall(carpet);
        int left = Math.min(people, MAX_SEATS);
        for (int row = 0; row < ROW_SEATS.length && left > 0; row++) {
            int count = Math.min(left, ROW_SEATS[row]);
            set.row(row, count);
            left -= count;
        }
        return set;
    }

    private void hall(DyeColor carpet) {
        Style s = style;
        for (int x = -HALF_WIDTH; x <= HALF_WIDTH; x++) {
            for (int z = BACK; z <= FRONT; z++) {
                put(x, -1, z, (x + z & 1) == 0 ? s.floor() : s.floorAlt());
            }
            for (int y = 0; y < WALL_HEIGHT; y++) {
                put(x, y, BACK, y == 0 || y == WALL_HEIGHT - 1 || y == 7 ? s.trim() : s.wall());
            }
        }
        // Pillars along the back wall, each with a lantern on its face, and the land's touch at its foot.
        for (int x : new int[]{-12, -6, 6, 12}) {
            for (int y = 0; y < WALL_HEIGHT - 1; y++) {
                put(x, y, BACK + 1, s.pillar());
            }
            put(x, 6, BACK + 2, Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, false));
            put(x, 5, BACK + 2, s.trim());
            put(x - 1, 0, BACK + 1, s.decor());
            put(x + 1, 0, BACK + 1, s.decor());
        }
        // The carpet up the middle, from the front to the greatest nobles' podiums.
        Block rug = carpet(carpet);
        for (int z = ROW_Z[1] + 2; z <= FRONT; z++) {
            for (int x = -1; x <= 1; x++) {
                put(x, 0, z, rug.defaultBlockState());
            }
        }
        // The ruler's banners, either side of the throne, hung from the wall.
        banners.add(new BannerSpot(-9, 10.5, BACK + 0.6));
        banners.add(new BannerSpot(9, 10.5, BACK + 0.6));
    }

    /** A row of podiums and the seats on them. */
    private void row(int row, int count) {
        int z = ROW_Z[row];
        int height = ROW_HEIGHT[row];
        for (int i = 0; i < count; i++) {
            double x;
            int size;
            if (row == 0) {
                x = 0;
                size = 3;
            } else if (row == 1) {
                x = i == 0 ? 0 : i == 1 ? -5 : 5;
                size = 3;
            } else {
                // Spread about the middle, three apart; the last row set between the one before.
                x = Math.floor((i - (count - 1) / 2.0) * 3);
                size = 2;
            }
            podium(x, z, height, size);
            seats.add(new Seat(x + (size == 3 ? 0.5 : 0), height, z + (size == 3 ? 0.5 : 0)));
        }
    }

    /** A podium {@code size} blocks square, {@code height} high, about (x, z): that block in its middle, or that corner for an even size. */
    private void podium(double x, int z, int height, int size) {
        int x0 = (int) x - 1;
        int z0 = z - 1;
        for (int dx = 0; dx < size; dx++) {
            for (int dz = 0; dz < size; dz++) {
                for (int y = 0; y < height; y++) {
                    put(x0 + dx, y, z0 + dz, y == height - 1 ? style.podiumTop() : style.podium());
                }
            }
        }
    }

    private void put(int x, int y, int z, BlockState state) {
        blocks.put(new BlockPos(x, y, z), state);
    }

    private static Block carpet(DyeColor colour) {
        return switch (colour) {
            case WHITE -> Blocks.WHITE_CARPET;
            case ORANGE -> Blocks.ORANGE_CARPET;
            case MAGENTA -> Blocks.MAGENTA_CARPET;
            case LIGHT_BLUE -> Blocks.LIGHT_BLUE_CARPET;
            case YELLOW -> Blocks.YELLOW_CARPET;
            case LIME -> Blocks.LIME_CARPET;
            case PINK -> Blocks.PINK_CARPET;
            case GRAY -> Blocks.GRAY_CARPET;
            case LIGHT_GRAY -> Blocks.LIGHT_GRAY_CARPET;
            case CYAN -> Blocks.CYAN_CARPET;
            case PURPLE -> Blocks.PURPLE_CARPET;
            case BLUE -> Blocks.BLUE_CARPET;
            case BROWN -> Blocks.BROWN_CARPET;
            case GREEN -> Blocks.GREEN_CARPET;
            case RED -> Blocks.RED_CARPET;
            case BLACK -> Blocks.BLACK_CARPET;
        };
    }

    private static BlockState log(Block block) {
        return block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
    }

    /** Each land's hall. */
    static Style style(Homeland homeland) {
        return switch (homeland) {
            case DESERT -> new Style(Blocks.SMOOTH_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(),
                    Blocks.SANDSTONE.defaultBlockState(), Blocks.ORANGE_TERRACOTTA.defaultBlockState(), Blocks.CHISELED_SANDSTONE.defaultBlockState(),
                    Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.SMOOTH_SANDSTONE.defaultBlockState(), Blocks.POTTED_CACTUS.defaultBlockState(),
                    0xFF3A2A1A);
            case JUNGLE -> new Style(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), Blocks.STONE_BRICKS.defaultBlockState(),
                    Blocks.JUNGLE_PLANKS.defaultBlockState(), Blocks.MOSSY_COBBLESTONE.defaultBlockState(), log(Blocks.JUNGLE_LOG),
                    Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), Blocks.CHISELED_STONE_BRICKS.defaultBlockState(),
                    Blocks.POTTED_FERN.defaultBlockState(), 0xFF16261A);
            case PLAINS -> new Style(Blocks.POLISHED_ANDESITE.defaultBlockState(), Blocks.ANDESITE.defaultBlockState(),
                    Blocks.STONE_BRICKS.defaultBlockState(), Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), log(Blocks.STRIPPED_OAK_LOG),
                    Blocks.STONE_BRICKS.defaultBlockState(), Blocks.POLISHED_ANDESITE.defaultBlockState(),
                    Blocks.POTTED_POPPY.defaultBlockState(), 0xFF202226);
            case SAVANNA -> new Style(Blocks.ACACIA_PLANKS.defaultBlockState(), Blocks.STRIPPED_ACACIA_WOOD.defaultBlockState(),
                    Blocks.TERRACOTTA.defaultBlockState(), Blocks.RED_TERRACOTTA.defaultBlockState(), log(Blocks.ACACIA_LOG),
                    Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState(), Blocks.CUT_RED_SANDSTONE.defaultBlockState(),
                    Blocks.POTTED_DEAD_BUSH.defaultBlockState(), 0xFF3A2418);
            case SNOW -> new Style(Blocks.POLISHED_DIORITE.defaultBlockState(), Blocks.PACKED_ICE.defaultBlockState(),
                    Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SNOW_BLOCK.defaultBlockState(), log(Blocks.STRIPPED_SPRUCE_LOG),
                    Blocks.POLISHED_DIORITE.defaultBlockState(), Blocks.SNOW_BLOCK.defaultBlockState(),
                    Blocks.POTTED_SPRUCE_SAPLING.defaultBlockState(), 0xFF1E2A36);
            case SWAMP -> new Style(Blocks.MUD_BRICKS.defaultBlockState(), Blocks.PACKED_MUD.defaultBlockState(),
                    Blocks.DARK_OAK_PLANKS.defaultBlockState(), Blocks.MOSS_BLOCK.defaultBlockState(), log(Blocks.MANGROVE_LOG),
                    Blocks.MUD_BRICKS.defaultBlockState(), Blocks.PACKED_MUD.defaultBlockState(),
                    Blocks.POTTED_BLUE_ORCHID.defaultBlockState(), 0xFF181E16);
            case TAIGA -> new Style(Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.STRIPPED_SPRUCE_WOOD.defaultBlockState(),
                    Blocks.COBBLESTONE.defaultBlockState(), Blocks.MOSSY_COBBLESTONE.defaultBlockState(), log(Blocks.SPRUCE_LOG),
                    Blocks.COBBLESTONE.defaultBlockState(), Blocks.POLISHED_ANDESITE.defaultBlockState(),
                    Blocks.POTTED_FERN.defaultBlockState(), 0xFF1C2018);
        };
    }
}
