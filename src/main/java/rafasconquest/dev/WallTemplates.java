package rafasconquest.dev;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.StringTag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates the city wall structure templates (run with {@code gradlew generateWallTemplates}).
 * <p>
 * A segment is 32 long (one street length: a 29-block street plus its 3-wide crossing) × 8 tall × 3 wide, in template
 * coordinates x = along the wall, y = up, z = across with z = 0 the outer face:
 * <ul>
 *   <li>y 0–5: solid body (y 0 sits flush with the ground, replacing the surface block, like vanilla streets)</li>
 *   <li>y 6: outer parapet, walkway behind it (where a guard stands)</li>
 *   <li>y 7: crenellations along the outer edge, with 2 standing banners at x = 8 and x = 24, facing out</li>
 *   <li>jigsaws at both ends (in the body just above the gate opening): {@code wall_start} faces back,
 *       {@code wall_end} faces forward and targets the next segment's {@code wall_start}</li>
 * </ul>
 * The gate variant has a 3-wide, 4-tall opening at x 0–2 (left as structure void, so the road beneath shows through).
 * Banners are white here; the owning faction's colours are applied when the chunk loads.
 */
public final class WallTemplates {
    public static final int LENGTH = 32;
    public static final int HEIGHT = 8;
    public static final int WIDTH = 3;
    public static final int[] BANNER_X = {8, 24};
    /** Top layer: crenellations and banners. The layer below is the parapet; everything under that is solid. */
    public static final int BANNER_Y = HEIGHT - 1;
    private static final int PARAPET_Y = HEIGHT - 2;
    public static final int GATE_WIDTH = 3;
    /** Clear height of the gate opening above the road. */
    public static final int GATE_HEIGHT = 4;
    /** The walkway: standing on the solid body, behind the parapet. Guards stand here. */
    public static final int WALKWAY_Y = HEIGHT - 2;
    public static final int WALKWAY_Z = 1;
    public static final int JIGSAW_Y = GATE_HEIGHT + 1;
    /** Minecraft 1.20.1. */
    private static final int DATA_VERSION = 3465;

    private WallTemplates() {
    }

    public static void main(String[] args) throws IOException {
        Path dir = Path.of(args[0]);
        Files.createDirectories(dir);
        NbtIo.writeCompressed(build(false), dir.resolve("segment.nbt").toFile());
        NbtIo.writeCompressed(build(true), dir.resolve("gate.nbt").toFile());
        System.out.println("Wrote wall templates to " + dir.toAbsolutePath());
    }

    public static CompoundTag build(boolean gate) {
        Palette palette = new Palette();
        int bricks = palette.state("minecraft:stone_bricks", Map.of());
        int air = palette.state("minecraft:air", Map.of());
        // Standing banner rotation 8 faces north, which is the outer side (z = 0) before the template is rotated.
        int banner = palette.state("minecraft:white_banner", Map.of("rotation", "8"));
        int jigsawBack = palette.state("minecraft:jigsaw", Map.of("orientation", "west_up"));
        int jigsawFront = palette.state("minecraft:jigsaw", Map.of("orientation", "east_up"));

        ListTag blocks = new ListTag();
        for (int x = 0; x < LENGTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int z = 0; z < WIDTH; z++) {
                    if (gate && x < GATE_WIDTH && y <= GATE_HEIGHT) {
                        continue; // gate opening (road level + GATE_HEIGHT clear): structure void, so the road beneath is kept
                    }
                    int state;
                    CompoundTag nbt = null;
                    if (y < PARAPET_Y) {
                        state = bricks;
                    } else if (z > 0) {
                        state = air; // walkway
                    } else if (y == PARAPET_Y) {
                        state = bricks; // outer parapet
                    } else if (x == BANNER_X[0] || x == BANNER_X[1]) {
                        state = banner;
                        nbt = new CompoundTag();
                        nbt.putString("id", "minecraft:banner");
                    } else {
                        state = x % 2 == 0 ? bricks : air; // crenellations
                    }
                    if (y == JIGSAW_Y && z == 1 && (x == 0 || x == LENGTH - 1)) {
                        boolean end = x == LENGTH - 1;
                        state = end ? jigsawFront : jigsawBack;
                        nbt = jigsaw(end ? "rafasconquest:wall_end" : "rafasconquest:wall_start",
                                end ? "rafasconquest:wall_start" : "minecraft:empty");
                    }
                    blocks.add(block(x, y, z, state, nbt));
                }
            }
        }

        CompoundTag root = new CompoundTag();
        root.putInt("DataVersion", DATA_VERSION);
        root.put("size", intList(LENGTH, HEIGHT, WIDTH));
        root.put("palette", palette.toTag());
        root.put("blocks", blocks);
        root.put("entities", new ListTag());
        return root;
    }

    /**
     * Pool is empty on purpose: the city builder chooses each next segment itself (so gates land on the avenues) and
     * uses these connectors to line it up.
     */
    private static CompoundTag jigsaw(String name, String target) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("id", "minecraft:jigsaw");
        nbt.putString("name", name);
        nbt.putString("target", target);
        nbt.putString("pool", "minecraft:empty");
        nbt.putString("final_state", "minecraft:stone_bricks");
        nbt.putString("joint", "rollable");
        return nbt;
    }

    private static CompoundTag block(int x, int y, int z, int state, CompoundTag nbt) {
        CompoundTag tag = new CompoundTag();
        tag.put("pos", intList(x, y, z));
        tag.putInt("state", state);
        if (nbt != null) {
            tag.put("nbt", nbt);
        }
        return tag;
    }

    private static ListTag intList(int... values) {
        ListTag list = new ListTag();
        for (int v : values) {
            list.add(IntTag.valueOf(v));
        }
        return list;
    }

    private static final class Palette {
        private final List<CompoundTag> states = new ArrayList<>();
        private final Map<String, Integer> index = new LinkedHashMap<>();

        int state(String name, Map<String, String> properties) {
            String key = name + properties;
            return index.computeIfAbsent(key, k -> {
                CompoundTag tag = new CompoundTag();
                tag.putString("Name", name);
                if (!properties.isEmpty()) {
                    CompoundTag props = new CompoundTag();
                    properties.forEach((p, v) -> props.put(p, StringTag.valueOf(v)));
                    tag.put("Properties", props);
                }
                states.add(tag);
                return states.size() - 1;
            });
        }

        ListTag toTag() {
            ListTag list = new ListTag();
            list.addAll(states);
            return list;
        }
    }
}
