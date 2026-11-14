package rafasconquest.style;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import rafasconquest.content.ConquestContent;
import rafasconquest.content.SailBlock;
import rafasconquest.core.SeaLanes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * The blocks of a ship on the sea lanes ({@link SeaLanes}), in its own frame: x across, y up from its keel, z from
 * stern to bow (bow towards +z), centred on x = z = 0. A ship is a structure from the default style's
 * {@code ships/warships|merchant/<size>} slot, or, while that slot is empty, a plain built-in hull of its size. The server builds
 * the real ship from these blocks and Distant Horizons draws them as its far version, so both look alike.
 */
public final class ShipHulls {
    public record Part(int x, int y, int z, BlockState state) {
    }

    /** The plain hull's deck height above its keel; soldiers stand a block above it. */
    public static final int DECK = 4;

    private static final Map<StructureTemplate, List<Part>> READ = new WeakHashMap<>();
    private static final Map<SeaLanes.Size, List<Part>> PLAIN = new java.util.EnumMap<>(SeaLanes.Size.class);

    static {
        for (SeaLanes.Size size : SeaLanes.Size.values()) {
            PLAIN.put(size, plain(size.length, size.width));
        }
    }

    private ShipHulls() {
    }

    public static List<Part> of(SeaLanes.Ship ship) {
        String slot = (ship.warship() ? "ships/warships/" : "ships/merchant/") + ship.size().slot();
        String file = StyleLibrary.pickIn(StyleLibrary.DEFAULT, slot, ship.id().hashCode());
        // shortcut: a ship file wider than one style tile (32 blocks) is not read, and the plain hull sails instead.
        StructureTemplate template = file == null ? null : StyleLibrary.template(file);
        if (template == null) {
            return PLAIN.get(ship.size());
        }
        synchronized (READ) {
            return READ.computeIfAbsent(template, ShipHulls::read);
        }
    }

    /** A structure's solid blocks, turned so its longer side runs along z. */
    private static List<Part> read(StructureTemplate template) {
        CompoundTag tag = template.save(new CompoundTag());
        List<BlockState> palette = new ArrayList<>();
        for (Tag t : tag.getList("palette", Tag.TAG_COMPOUND)) {
            palette.add(NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), (CompoundTag) t));
        }
        int sx = template.getSize().getX();
        int sz = template.getSize().getZ();
        boolean turn = sx > sz;
        List<Part> parts = new ArrayList<>();
        for (Tag t : tag.getList("blocks", Tag.TAG_COMPOUND)) {
            CompoundTag b = (CompoundTag) t;
            BlockState state = palette.get(b.getInt("state"));
            if (state.isAir() || state.is(Blocks.STRUCTURE_VOID) || state.is(Blocks.STRUCTURE_BLOCK)) {
                continue;
            }
            ListTag pos = b.getList("pos", Tag.TAG_INT);
            int x = pos.getInt(0) - sx / 2;
            int z = pos.getInt(2) - sz / 2;
            // A quarter turn (not a mirror), so stairs, artillery and other facing blocks turn with the hull.
            parts.add(turn ? new Part(-z, pos.getInt(1), x, state.rotate(net.minecraft.world.level.block.Rotation.CLOCKWISE_90))
                    : new Part(x, pos.getInt(1), z, state));
        }
        return parts.isEmpty() ? PLAIN.get(SeaLanes.Size.MEDIUM) : List.copyOf(parts);
    }

    /**
     * A battle ship's hull: always the plain flat-topped one of its size (a style's ship may have no open deck for a
     * formation to stand on), with hay, barrels and chests on deck if it is a {@code cargo} ship (the fleet's camp), and its
     * sail in its side's flag: {@code sail}'s dye with a band of {@code stripe}'s across the middle. Each kind is built
     * once and the same list handed out after, so the client can keep its shape by the list.
     */
    public static List<Part> battle(SeaLanes.Size size, boolean cargo, DyeColor sail, DyeColor stripe) {
        return BATTLE.computeIfAbsent(new BattleKind(size, cargo, sail, stripe),
                k -> buildBattle(k.size(), k.cargo(), k.sail(), k.stripe()));
    }

    private record BattleKind(SeaLanes.Size size, boolean cargo, DyeColor sail, DyeColor stripe) {
    }

    private static final Map<BattleKind, List<Part>> BATTLE = new java.util.concurrent.ConcurrentHashMap<>();

    private static List<Part> buildBattle(SeaLanes.Size size, boolean cargo, DyeColor sail, DyeColor stripe) {
        int middle = (DECK + 4 + DECK + 1 + size.length / 2) / 2; // halfway between plain()'s lowest and highest sail rows
        List<Part> parts = new ArrayList<>();
        for (Part p : PLAIN.get(size)) {
            parts.add(p.state().getBlock() instanceof SailBlock
                    ? new Part(p.x(), p.y(), p.z(), p.state().setValue(SailBlock.COLOR, p.y() == middle ? stripe : sail)) : p);
        }
        if (!cargo) {
            return List.copyOf(parts);
        }
        BlockState[] goods = {Blocks.HAY_BLOCK.defaultBlockState(), Blocks.BARREL.defaultBlockState(), Blocks.CHEST.defaultBlockState()};
        int half = size.length / 2;
        for (int z = -half / 2; z <= half / 2; z += 2) {
            for (int x = -1; x <= 1; x += 2) {
                parts.add(new Part(x, DECK + 1, z, goods[Math.floorMod(x + z, goods.length)]));
            }
        }
        return List.copyOf(parts);
    }

    /**
     * A plain flat-topped boat, six blocks tall: a keel, planked sides up to a flat deck narrowing to bow and stern, a rail one block high round the
     * deck's edge, and one mast with a sail stepped near the stern, clear of the deck's middle where soldiers stand.
     */
    private static List<Part> plain(int length, int width) {
        BlockState keel = Blocks.SPRUCE_PLANKS.defaultBlockState();
        BlockState deck = Blocks.OAK_PLANKS.defaultBlockState();
        BlockState mast = Blocks.SPRUCE_LOG.defaultBlockState();
        BlockState sail = ConquestContent.SAIL.get().defaultBlockState().setValue(SailBlock.FACING, Direction.SOUTH); // the bow is +z
        List<Part> parts = new ArrayList<>();
        int half = length / 2;
        for (int z = -half; z <= half; z++) {
            // The beam narrows over the last quarter at each end.
            int end = half - Math.abs(z);
            int w = Math.min(width / 2, end * 2 * (width / 2) / Math.max(1, half / 2));
            for (int x = -w; x <= w; x++) {
                parts.add(new Part(x, 0, z, keel));
                parts.add(new Part(x, DECK, z, deck));
                if (Math.abs(x) == w || end == 0) {
                    for (int y = 1; y < DECK; y++) {
                        parts.add(new Part(x, y, z, keel)); // the hull's planked side, keel to deck
                    }
                    parts.add(new Part(x, DECK + 1, z, keel));
                }
            }
        }
        int stern = -half + 2;
        int top = DECK + 2 + length / 2;
        for (int y = DECK + 1; y <= top; y++) {
            parts.add(new Part(0, y, stern, mast));
        }
        int sailHalf = width / 2 + 1;
        for (int y = DECK + 4; y < top; y++) {
            for (int x = -sailHalf; x <= sailHalf; x++) {
                parts.add(new Part(x, y, stern + 1, sail)); // hung before the mast, so it is one sheet
            }
        }
        return List.copyOf(parts);
    }
}
