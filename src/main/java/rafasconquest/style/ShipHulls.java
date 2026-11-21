package rafasconquest.style;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import rafasconquest.content.ConquestContent;
import rafasconquest.content.HelmBlock;
import rafasconquest.content.OarBlock;
import rafasconquest.content.SailBlock;
import rafasconquest.core.SeaLanes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The blocks of a ship, in its own frame: x across, y up from its keel, z from stern to bow (bow towards +z), centred on
 * x = z = 0. A ship card ({@link SeaLanes.Size}) names its hull, a structure {@code .nbt} in
 * {@code mods/RafasConquest/units/ships} (its warship and battle hull, and a merchant one for the lanes); the built-in
 * cards' plain hulls are written there when missing, so they can be edited or swapped. A lane ship may also come from
 * the default style's {@code ships/warships|merchant/<card id>} slot, which wins. The server builds the real ship from
 * these blocks and Distant Horizons draws them as its far version, so both look alike.
 */
public final class ShipHulls {
    public record Part(int x, int y, int z, BlockState state) {
    }

    /** The plain hull's deck height above its keel; soldiers stand a block above it. A card's own is {@link SeaLanes.Size#deck}. */
    public static final int DECK = 4;

    private static final Map<StructureTemplate, List<Part>> READ = new WeakHashMap<>();
    /** The hull files read, by file name, and their bytes (gzipped NBT) to send a client. */
    private static final Map<String, List<Part>> FILES = new ConcurrentHashMap<>();
    private static final Map<String, byte[]> BYTES = new ConcurrentHashMap<>();
    /** Each hull file's Character Markers: where its crew stand (a gunship's), by file name. */
    private static final Map<String, List<SeaLanes.Size.Post>> POSTS = new ConcurrentHashMap<>();
    /** Plain hulls made for cards with no file read, by card id and "/m" for the merchant one. */
    private static final Map<String, List<Part>> MADE = new ConcurrentHashMap<>();

    private ShipHulls() {
    }

    public static List<Part> of(SeaLanes.Ship ship) {
        String slot = (ship.warship() ? "ships/warships/" : "ships/merchant/") + ship.size().slot();
        String file = StyleLibrary.pickIn(StyleLibrary.DEFAULT, slot, ship.id().hashCode());
        // shortcut: a ship file wider than one style tile (32 blocks) is not read, and the card's hull sails instead.
        StructureTemplate template = file == null ? null : StyleLibrary.template(file);
        if (template == null) {
            return hull(ship.size(), !ship.warship());
        }
        synchronized (READ) {
            return READ.computeIfAbsent(template, t -> read(t.save(new CompoundTag()), new ArrayList<>()));
        }
    }

    /** A card's hull: its file's, else (none read) a plain one of its size, rowed unless {@code merchant}. */
    public static List<Part> hull(SeaLanes.Size size, boolean merchant) {
        String name = merchant && !size.merchant.isBlank() ? size.merchant : size.hull;
        List<Part> read = name.isBlank() ? null : FILES.get(name);
        if (read != null) {
            return read;
        }
        return MADE.computeIfAbsent(size.id + (merchant ? "/m" : ""), k -> made(size, merchant));
    }

    private static List<Part> made(SeaLanes.Size size, boolean merchant) {
        Integer guns = SIDE_GUNS.get(size);
        if (guns != null) {
            return manOWar(size, guns);
        }
        // A galley steps its mast amidships; a merchant, and a rowed hull carded to sail (the Gunship), near the stern.
        boolean galley = !merchant && size.propulsionSet != SeaLanes.Propulsion.SAIL;
        List<Part> plain = plain(size.length, size.width, galley ? 0 : -(size.length / 2) + 2);
        if (merchant) {
            return plain;
        }
        List<Part> rowed = rowed(plain, size.length, size.width);
        return size.gunship() ? gunned(rowed, size)
                : size.bowGuns > 0 ? bowGuns(rowed, size)
                : size == SeaLanes.Size.TOWER_GALLEY ? fightingTower(rowed, size)
                : rowed;
    }

    /** plain()'s half-beam at {@code z}: full over the middle, narrowing over the last quarter at each end. */
    private static int beamAt(int z, int length, int width) {
        int half = length / 2;
        return Math.min(width / 2, (half - Math.abs(z)) * 2 * (width / 2) / Math.max(1, half / 2));
    }

    /** The furthest row forward where the deck is at least {@code room} blocks either side of the middle. */
    private static int bowRow(int length, int width, int room) {
        int z = length / 2;
        while (z > 0 && beamAt(z, length, width) < room) {
            z--;
        }
        return z;
    }

    /**
     * A galley with its card's {@code bow_guns} of its {@code gun_block} on the foredeck, pointing over the bow, as the
     * galleys of Lepanto carried guns and Roman ones engines, each with its crew's Character Marker behind it: one on the
     * centreline, or two side by side (a cannon's carriage a block either side, an engine's frame two). An engine stands
     * further back, its Siegeworks model reaching past its block. Its card's hull is longer than a plain galley's for them.
     */
    private static List<Part> bowGuns(List<Part> rowed, SeaLanes.Size size) {
        BlockState gun = gunBlock(size).setValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING, Direction.SOUTH); // the bow is +z
        boolean engine = gun.getBlock() instanceof rafasconquest.content.ShipEngineBlock;
        int z = engine ? bowRow(size.length, size.width, 4) - 3 : bowRow(size.length, size.width, 3) - 1;
        int apart = engine ? 2 : 1;
        int[] xs = size.bowGuns == 1 ? new int[]{0} : new int[]{-apart, apart};
        List<Part> parts = new ArrayList<>();
        for (Part p : rowed) {
            boolean taken = false;
            for (int x : xs) {
                taken |= p.y() == DECK + 1 && p.x() == x && (p.z() == z || p.z() == z - 1);
            }
            if (!taken) {
                parts.add(p);
            }
        }
        for (int x : xs) {
            parts.add(new Part(x, DECK + 1, z, gun));
            parts.add(new Part(x, DECK + 1, z - 1, ConquestContent.MARKER.get().defaultBlockState()));
        }
        return List.copyOf(parts);
    }

    /** How high a Fighting Tower Galley's platform stands over its deck. */
    static final int TOWER_HEIGHT = 7;

    /**
     * A Fighting Tower Galley: a wooden tower on the foredeck, seven blocks square, four log posts holding a planked
     * platform {@link #TOWER_HEIGHT} over the deck behind a fence, a ladder up a fifth post at its back, and a Character Marker on each
     * of its inner 25 blocks for its archers, who shoot down on enemy decks from it. Its card's hull is longer for it.
     */
    private static List<Part> fightingTower(List<Part> rowed, SeaLanes.Size size) {
        int front = bowRow(size.length, size.width, 3);
        int zc = front - 3;
        int top = DECK + TOWER_HEIGHT;
        BlockState post = Blocks.SPRUCE_LOG.defaultBlockState();
        BlockState floor = Blocks.SPRUCE_PLANKS.defaultBlockState();
        BlockState fence = Blocks.SPRUCE_FENCE.defaultBlockState();
        BlockState ladder = Blocks.LADDER.defaultBlockState().setValue(net.minecraft.world.level.block.LadderBlock.FACING, Direction.NORTH);
        Map<Long, Part> at = new java.util.LinkedHashMap<>();
        for (Part p : rowed) {
            at.put(net.minecraft.core.BlockPos.asLong(p.x(), p.y(), p.z()), p);
        }
        for (int x = -3; x <= 3; x++) {
            for (int z = zc - 3; z <= zc + 3; z++) {
                boolean corner = Math.abs(x) == 3 && Math.abs(z - zc) == 3;
                if (corner) {
                    for (int y = DECK + 1; y < top; y++) {
                        put(at, x, y, z, post);
                    }
                }
                put(at, x, top, z, floor);
                if (Math.abs(x) == 3 || Math.abs(z - zc) == 3) {
                    if (!(x == 0 && z == zc - 3)) { // a gap in the fence over the ladder
                        put(at, x, top + 1, z, fence);
                    }
                } else {
                    put(at, x, top + 1, z, ConquestContent.MARKER.get().defaultBlockState());
                }
            }
        }
        for (int y = DECK + 1; y <= top; y++) {
            if (y < top) {
                put(at, 0, y, zc - 3, post); // a middle post on the tower's back, for the ladder to hang on
            }
            put(at, 0, y, zc - 4, ladder); // facing aft; up it, over the fence's gap onto the platform
        }
        return List.copyOf(at.values());
    }

    /**
     * A card's guns and crew posts, from its hull: each gun block's engine on the side it stands (right -x, left
     * +x, both on the centreline; one laid over the bow, ahead), and its hull file's Character Markers, each a post for as many soldiers as it says;
     * a post beside a gun block (within a block) works that gun, the first post there only.
     */
    private static void fit(SeaLanes.Size size) {
        List<Part> parts = hull(size, false);
        List<String> right = new ArrayList<>();
        List<String> left = new ArrayList<>();
        List<String> bow = new ArrayList<>();
        // shortcut: no engine block yet, so steam only by the card's "propulsion"; read an engine block in stage 4.
        size.propulsion = size.propulsionSet != null ? size.propulsionSet
                : parts.stream().anyMatch(p -> p.state().getBlock() instanceof OarBlock) ? SeaLanes.Propulsion.OARS : SeaLanes.Propulsion.SAIL;
        for (Part p : parts) {
            String gun = gunOf(p.state());
            if (gun != null && p.state().getValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) == Direction.SOUTH) {
                bow.add(gun); // laid over the bow (+z): it fires ahead, not in a broadside
            } else if (gun != null) {
                if (p.x() <= 0) {
                    right.add(gun);
                }
                if (p.x() >= 0) {
                    left.add(gun);
                }
            }
        }
        size.gunsRight = List.copyOf(right);
        size.gunsLeft = List.copyOf(left);
        size.gunsBow = List.copyOf(bow);
        List<SeaLanes.Size.Post> posts = new ArrayList<>();
        Set<Part> manned = new HashSet<>();
        for (SeaLanes.Size.Post post : POSTS.getOrDefault(size.hull, List.of())) {
            String gun = "";
            for (Part p : parts) {
                if (gunOf(p.state()) != null && !manned.contains(p)
                        && Math.abs(p.x() - post.x()) <= 1 && Math.abs(p.z() - post.z()) <= 1 && Math.abs(p.y() - post.y()) <= 1) {
                    gun = gunOf(p.state());
                    manned.add(p);
                    break;
                }
            }
            posts.add(new SeaLanes.Size.Post(post.x(), post.y(), post.z(), gun));
        }
        size.posts = List.copyOf(posts);
    }

    /** The engine a gun block holds, by id (the Medium Cannon: ships carry only our own guns); null for any other block. */
    @org.jetbrains.annotations.Nullable
    public static String gunOf(BlockState state) {
        return state.getBlock() instanceof rafasconquest.content.MediumCannonBlock gun ? gun.machine().id : null; // an engine block's own card
    }

    private static void fitAll() {
        for (SeaLanes.Size size : SeaLanes.Size.values()) {
            fit(size);
        }
    }

    /**
     * Reads every card's hull files in {@code dir}, first writing the built-in cards' made hulls where theirs are missing or
     * still as an older jar wrote them ({@link rafasconquest.data.BuiltInFiles}). A file that can't be read is left out (logged): its card sails a plain hull of its size.
     */
    public static void load(Path dir) {
        clear();
        rafasconquest.data.BuiltInFiles kept = rafasconquest.data.BuiltInFiles.in(dir);
        for (SeaLanes.Size size : SeaLanes.Size.values()) {
            for (boolean merchant : new boolean[]{false, true}) {
                String name = merchant ? size.merchant : size.hull;
                if (name.isBlank() || FILES.containsKey(name)) {
                    continue;
                }
                Path file = dir.resolve(name);
                try {
                    if (size.builtIn()) { // written, or brought up to this jar's hull unless edited
                        java.io.ByteArrayOutputStream bytes = new java.io.ByteArrayOutputStream();
                        NbtIo.writeCompressed(structure(made(size, merchant)), bytes);
                        kept.keep(name, bytes.toByteArray());
                    }
                    if (Files.exists(file)) {
                        put(name, Files.readAllBytes(file));
                    } else {
                        rafasconquest.RafasConquest.LOGGER.warn("Ship cards: {} has no hull file {}; it sails a plain hull", size.id, file);
                    }
                } catch (IOException | RuntimeException e) {
                    rafasconquest.RafasConquest.LOGGER.warn("Ship cards: couldn't read {}; {} sails a plain hull", file, size.id, e);
                }
            }
        }
        kept.save();
        fitAll();
    }

    /** The hull files read, {"hulls", file name, base64 of its bytes}, to send a client. */
    public static List<String[]> texts() {
        List<String[]> out = new ArrayList<>();
        BYTES.forEach((name, bytes) -> out.add(new String[]{"hulls", name, Base64.getEncoder().encodeToString(bytes)}));
        return out;
    }

    /** A server's hull files ({@link #texts}) in place of this game's own. */
    public static void replace(List<String[]> files) {
        clear();
        for (String[] f : files) {
            try {
                put(f[1], Base64.getDecoder().decode(f[2]));
            } catch (IOException | RuntimeException e) {
                rafasconquest.RafasConquest.LOGGER.warn("Ship cards: the server's hull {} couldn't be read", f[1], e);
            }
        }
        fitAll();
    }

    private static void clear() {
        FILES.clear();
        POSTS.clear();
        BYTES.clear();
        MADE.clear();
        BATTLE.clear();
    }

    private static void put(String name, byte[] bytes) throws IOException {
        List<SeaLanes.Size.Post> posts = new ArrayList<>();
        FILES.put(name, read(NbtIo.readCompressed(new ByteArrayInputStream(bytes), NbtAccounter.unlimitedHeap()), posts));
        POSTS.put(name, List.copyOf(posts));
        BYTES.put(name, bytes);
    }

    /**
     * A structure's solid blocks, turned so its longer side runs along z. Its Character Markers aren't built: each is a
     * crew post (into {@code posts}), as many as the soldiers it is set for.
     */
    private static List<Part> read(CompoundTag tag, List<SeaLanes.Size.Post> posts) {
        List<BlockState> palette = new ArrayList<>();
        for (Tag t : tag.getList("palette", Tag.TAG_COMPOUND)) {
            palette.add(NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), (CompoundTag) t));
        }
        ListTag size = tag.getList("size", Tag.TAG_INT);
        int sx = size.getInt(0);
        int sz = size.getInt(2);
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
            if (state.getBlock() instanceof MarkerBlock) {
                int count = b.contains("nbt") ? MarkerSpec.load(b.getCompound("nbt").getCompound("marker")).count() : 1;
                for (int i = 0; i < Math.max(1, count); i++) {
                    posts.add(turn ? new SeaLanes.Size.Post(-z, pos.getInt(1), x, "") : new SeaLanes.Size.Post(x, pos.getInt(1), z, ""));
                }
                continue;
            }
            // A quarter turn (not a mirror), so stairs, artillery and other facing blocks turn with the hull.
            parts.add(turn ? new Part(-z, pos.getInt(1), x, state.rotate(net.minecraft.world.level.block.Rotation.CLOCKWISE_90))
                    : new Part(x, pos.getInt(1), z, state));
        }
        return parts.isEmpty() ? made(SeaLanes.Size.MEDIUM, true) : List.copyOf(parts);
    }

    /** Parts as a structure file's tag, the keel at y 0, centred as {@link #read} centres them back. */
    static CompoundTag structure(List<Part> parts) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
        for (Part p : parts) {
            minX = Math.min(minX, p.x());
            minY = Math.min(minY, p.y());
            minZ = Math.min(minZ, p.z());
            maxX = Math.max(maxX, p.x());
            maxY = Math.max(maxY, p.y());
            maxZ = Math.max(maxZ, p.z());
        }
        // read() takes size / 2 off: keep the hull's middle there (its parts are centred on 0, odd beams and lengths).
        int sx = Math.max(maxX, -minX) * 2 + 1;
        int sz = Math.max(maxZ, -minZ) * 2 + 1;
        List<BlockState> palette = new ArrayList<>();
        ListTag blocks = new ListTag();
        for (Part p : parts) {
            int i = palette.indexOf(p.state());
            if (i < 0) {
                i = palette.size();
                palette.add(p.state());
            }
            CompoundTag b = new CompoundTag();
            b.put("pos", ints(p.x() + sx / 2, p.y() - minY, p.z() + sz / 2));
            b.putInt("state", i);
            blocks.add(b);
        }
        ListTag paletteTag = new ListTag();
        palette.forEach(s -> paletteTag.add(NbtUtils.writeBlockState(s)));
        CompoundTag tag = new CompoundTag();
        tag.put("size", ints(sx, maxY - minY + 1, sz));
        tag.put("palette", paletteTag);
        tag.put("blocks", blocks);
        tag.put("entities", new ListTag());
        tag.putInt("DataVersion", net.minecraft.SharedConstants.getCurrentVersion().getDataVersion().getVersion());
        return tag;
    }

    private static ListTag ints(int... values) {
        ListTag list = new ListTag();
        for (int v : values) {
            list.add(IntTag.valueOf(v));
        }
        return list;
    }

    /**
     * A battle ship's hull: its card's hull, its sails in its side's flag: {@code sail}'s dye with a band of {@code stripe}'s
     * across each sheet's middle row (a column's own run of sail blocks, so stacked sails and mast flags each get one). Each kind is built once and the same list handed out after, so the client can keep its shape by the list.
     */
    public static List<Part> battle(SeaLanes.Size size, DyeColor sail, DyeColor stripe) {
        return BATTLE.computeIfAbsent(new BattleKind(size, sail, stripe), k -> buildBattle(k.size(), k.sail(), k.stripe()));
    }

    private record BattleKind(SeaLanes.Size size, DyeColor sail, DyeColor stripe) {
    }

    private static final Map<BattleKind, List<Part>> BATTLE = new ConcurrentHashMap<>();

    private static List<Part> buildBattle(SeaLanes.Size size, DyeColor sail, DyeColor stripe) {
        List<Part> hull = hull(size, false);
        Set<Long> sails = new HashSet<>();
        for (Part p : hull) {
            if (p.state().getBlock() instanceof SailBlock) {
                sails.add(net.minecraft.core.BlockPos.asLong(p.x(), p.y(), p.z()));
            }
        }
        List<Part> parts = new ArrayList<>();
        for (Part p : hull) {
            if (!(p.state().getBlock() instanceof SailBlock && p.state().hasProperty(SailBlock.COLOR))) {
                parts.add(p);
                continue;
            }
            int low = p.y();
            int high = p.y();
            while (sails.contains(net.minecraft.core.BlockPos.asLong(p.x(), low - 1, p.z()))) {
                low--;
            }
            while (sails.contains(net.minecraft.core.BlockPos.asLong(p.x(), high + 1, p.z()))) {
                high++;
            }
            parts.add(new Part(p.x(), p.y(), p.z(), p.state().setValue(SailBlock.COLOR, p.y() == (low + high) / 2 ? stripe : sail)));
        }
        return List.copyOf(parts);
    }

    /**
     * A warship made a gunship: over its full-beam middle, every third plank of the deck rail down both sides a Medium
     * Cannon facing out, and a Character Marker
     * inboard of each for its gunner. The card's {@code gun_block} names the gun block (a Medium Cannon).
     */
    private static List<Part> gunned(List<Part> rowed, SeaLanes.Size size) {
        int beam = size.width / 2;
        int half = size.length / 2;
        BlockState gun = gunBlock(size);
        Set<Long> guns = new java.util.LinkedHashSet<>(); // in order, so the file and its posts come out the same each time
        Integer first = null;
        for (int z = -half; z <= half; z++) {
            // plain()'s beam at z: full only over the middle.
            if (Math.min(beam, (half - Math.abs(z)) * 2 * beam / Math.max(1, half / 2)) < beam) {
                continue;
            }
            first = first == null ? z : first;
            if ((z - first) % 3 == 0) { // two rail planks between guns: cover for their gunners
                guns.add(net.minecraft.core.BlockPos.asLong(-beam, DECK + 1, z));
                guns.add(net.minecraft.core.BlockPos.asLong(beam, DECK + 1, z));
            }
        }
        List<Part> parts = new ArrayList<>();
        for (Part p : rowed) {
            if (!guns.contains(net.minecraft.core.BlockPos.asLong(p.x(), p.y(), p.z()))) {
                parts.add(p);
            }
        }
        for (long g : guns) {
            int x = net.minecraft.core.BlockPos.getX(g);
            int z = net.minecraft.core.BlockPos.getZ(g);
            parts.add(new Part(x, DECK + 1, z, gun
                    .setValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING, x < 0 ? Direction.WEST : Direction.EAST)));
            parts.add(new Part(x - Integer.signum(x), DECK + 1, z, ConquestContent.MARKER.get().defaultBlockState()));
        }
        return List.copyOf(parts);
    }

    /** The card's {@code gun_block}, if it names a gun block; else the Medium Cannon. */
    private static BlockState gunBlock(SeaLanes.Size size) {
        net.minecraft.world.level.block.Block named = net.minecraft.core.registries.BuiltInRegistries.BLOCK
                .get(net.minecraft.resources.ResourceLocation.tryParse(size.gunBlock));
        return (gunOf(named.defaultBlockState()) != null ? named : ConquestContent.MEDIUM_CANNON.get()).defaultBlockState();
    }

    /**
     * The Man O' War, a three-decker: a dark hull, a pale band along each gun row, a rounded bow and a square stern, its
     * bottom curving up to both, cabin windows along the quarterdeck's sides;
     * two closed gun decks under its open weather deck ({@code size.deck}), each deck a row of guns down both sides over
     * the middle at gunned()'s spacing, a gunner's Character Marker inboard of each. A forecastle, a raised
     * quarterdeck with the Helm, a bowsprit, and three masts (the main four blocks taller than the fore and mizzen), each
     * with three sails under yards and a medium flag at its head, held on the mast's aft side and flying astern.
     */
    private static List<Part> manOWar(SeaLanes.Size size, int sideGuns) {
        BlockState keel = Blocks.SPRUCE_PLANKS.defaultBlockState();
        BlockState side = Blocks.DARK_OAK_PLANKS.defaultBlockState();
        BlockState band = Blocks.BIRCH_PLANKS.defaultBlockState();
        BlockState floor = Blocks.OAK_PLANKS.defaultBlockState();
        BlockState mast = Blocks.SPRUCE_LOG.defaultBlockState();
        BlockState yard = mast.setValue(net.minecraft.world.level.block.RotatedPillarBlock.AXIS, Direction.Axis.X);
        BlockState sprit = mast.setValue(net.minecraft.world.level.block.RotatedPillarBlock.AXIS, Direction.Axis.Z);
        BlockState sail = ConquestContent.SAIL.get().defaultBlockState().setValue(SailBlock.FACING, Direction.SOUTH);
        BlockState flag = ConquestContent.FLAGS.get(1).get().defaultBlockState() // medium_flag
                .setValue(rafasconquest.content.FlagBlock.FACING, Direction.NORTH);
        BlockState gun = gunBlock(size);
        BlockState marker = ConquestContent.MARKER.get().defaultBlockState();
        BlockState window = Blocks.GLASS_PANE.defaultBlockState();
        int beam = size.width / 2;
        int half = size.length / 2;
        int top = size.deck;
        int gap = top / 3; // the bigger rates' decks stand further apart
        int[] decks = {top - 2 * gap, top - gap, top}; // the lower and upper gun decks, then the weather deck
        // Each deck a row of a third of the side's guns, rounded up, the weather deck's foremost left out for the rest:
        // 15/15/15 the Man O' War, 17/17/16 the 3rd Rate, 20s the 2nd, 24/24/22 the 1st, 27/27/26 the Flagship.
        int row = (sideGuns + 2) / 3;
        int topRow = sideGuns - 2 * row;
        int firstGun = -(3 * (row - 1)) / 2; // the rows centred, two planks between guns as gunned()'s
        int grow = beam - 8; // masts and sails grow with the beam over the Man O' War's
        Map<Long, Part> at = new java.util.LinkedHashMap<>(); // a later block replaces an earlier one in its spot
        for (int z = -half; z <= half; z++) {
            int end = half - Math.abs(z);
            // From above: a rounded bow and a square stern with rounded corners (BOW_BEAM, STERN_BEAM).
            int[] plan = z > 0 ? BOW_BEAM : STERN_BEAM;
            // past the plan, a block wider each step to the full beam (the Man O' War's reaches it at once)
            int w = Math.min(beam, end < plan.length ? plan[end] : plan[plan.length - 1] + end - plan.length + 1);
            boolean forecastle = z > 0 && end <= 9;
            boolean quarterdeck = z < 0 && end <= 10; // both clear of the middle, where the guns are
            int rail = top + 1 + (forecastle ? 3 : quarterdeck ? 4 : 0);
            // The bottom curves up to bow and stern (BOW, STERN: its height by blocks from the end).
            int[] curve = z > 0 ? BOW : STERN;
            int bottom = end < curve.length ? curve[end] : 0;
            for (int x = -w; x <= w; x++) {
                put(at, x, bottom, z, keel);
                for (int d : decks) {
                    if (d > bottom) {
                        put(at, x, d, z, floor);
                    }
                }
                if (rail > top + 1) {
                    put(at, x, rail - 1, z, floor);
                }
                if (Math.abs(x) == w || end == 0) {
                    for (int y = bottom + 1; y <= rail; y++) {
                        put(at, x, y, z, y == decks[0] + 1 || y == decks[1] + 1 || y == decks[2] + 1 ? band : side);
                    }
                }
                if (quarterdeck && Math.abs(x) == w && w > 1 && z % 2 == 0) {
                    put(at, x, top + 2, z, window); // the captain's cabin's windows, under the quarterdeck
                }
            }
            int gunAt = z - firstGun;
            if (gunAt >= 0 && gunAt % 3 == 0 && gunAt / 3 < row) {
                for (int d : decks) {
                    if (d == top && gunAt / 3 >= topRow) {
                        continue;
                    }
                    put(at, -beam, d + 1, z, gun.setValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING, Direction.WEST));
                    put(at, beam, d + 1, z, gun.setValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING, Direction.EAST));
                    put(at, -beam + 1, d + 1, z, marker);
                    put(at, beam - 1, d + 1, z, marker);
                }
            }
        }
        // Fore, main, mizzen: where each stands, then its course's, topsail's and topgallant's heights.
        // On a bigger rate each mast stands further out and every sail is taller, the topgallant by half as much.
        int out = (half * 3 + 2) / 5; // the Man O' War's 20
        int[][] masts = {{out, 5 + grow, 5 + grow, 3 + grow / 2}, {0, 7 + grow, 7 + grow, 3 + grow / 2},
                {-out, 5 + grow, 5 + grow, 3 + grow / 2}};
        for (int[] m : masts) {
            int z = m[0];
            int y = top + 5; // the course's foot, clear of the crews on deck
            for (int i = 0; i < 3; i++) {
                int w = beam - 2 * i;
                for (int x = -w; x <= w; x++) {
                    for (int r = 0; r < m[i + 1]; r++) {
                        put(at, x, y + r, z + 1, sail); // hung before the mast, as plain()'s
                    }
                    put(at, x, y + m[i + 1], z + 1, yard); // and parts it from the sail above: three sheets, not one
                }
                y += m[i + 1] + 1;
            }
            for (int h = 1; h <= y; h++) {
                put(at, 0, h, z, mast);
            }
            put(at, 0, y, z - 1, flag);
        }
        for (int z = half; z <= half + 5 + grow; z++) {
            put(at, 0, top + 3 + (z - half) / 3, z, sprit);
        }
        put(at, 0, top + 5, -half + 2, ConquestContent.HELM.get().defaultBlockState().setValue(HelmBlock.FACING, Direction.SOUTH));
        return List.copyOf(at.values());
    }

    /** The Man O' War's bottom above its keel line, by blocks back from the stem: aft to fore, run 2 up 1, run 2 up 1, run 1 up 1, run 1 up 1, run 1 up 2. */
    private static final int[] BOW = {7, 5, 4, 3, 2, 2, 1, 1};
    /** The same by blocks forward from the stern: fore to aft, run 1 up 2, run 1 up 2, run 1 up 1, run 1 up 1, run 2 up 1. */
    private static final int[] STERN = {7, 6, 6, 5, 4, 2};
    /** Its half-beam by blocks back from the stem, and by blocks forward from the stern; full past these. */
    private static final int[] BOW_BEAM = {1, 3, 4, 5, 6, 6, 7, 7, 7, 7};
    private static final int[] STERN_BEAM = {5, 6, 7, 7, 7, 7};
    /** The made three-deckers and their guns a side; their lengths (the cards') keep the gun rows 12+ blocks from either end, clear of forecastle and quarterdeck. */
    private static final Map<SeaLanes.Size, Integer> SIDE_GUNS = Map.of(SeaLanes.Size.MAN_O_WAR, 45, SeaLanes.Size.THIRD_RATE, 50,
            SeaLanes.Size.SECOND_RATE, 60, SeaLanes.Size.FIRST_RATE, 70, SeaLanes.Size.GRAND_FLAGSHIP, 80);

    private static void put(Map<Long, Part> at, int x, int y, int z, BlockState state) {
        at.put(net.minecraft.core.BlockPos.asLong(x, y, z), new Part(x, y, z, state));
    }

    /**
     * A plain hull made a warship: a Helm at the back middle of its deck facing the bow, and oars along both flat sides,
     * every other side plank of the row above the keel, right oars on the right (-x, the bow being +z), left on the left.
     */
    private static List<Part> rowed(List<Part> plain, int length, int width) {
        int beam = width / 2;
        BlockState left = ConquestContent.OAR_LEFT.get().defaultBlockState().setValue(OarBlock.FACING, Direction.SOUTH);
        BlockState right = ConquestContent.OAR_RIGHT.get().defaultBlockState().setValue(OarBlock.FACING, Direction.SOUTH);
        List<Part> parts = new ArrayList<>();
        for (Part p : plain) {
            boolean oar = p.y() == 1 && Math.abs(p.x()) == beam && p.z() % 2 == 0 && p.state().is(Blocks.SPRUCE_PLANKS);
            parts.add(oar ? new Part(p.x(), p.y(), p.z(), p.x() < 0 ? right : left) : p);
        }
        // plain()'s stern row is only the rail; one in, the deck's middle block is open between the rails.
        parts.add(new Part(0, DECK + 1, -(length / 2) + 1,
                ConquestContent.HELM.get().defaultBlockState().setValue(HelmBlock.FACING, Direction.SOUTH)));
        return List.copyOf(parts);
    }

    /**
     * A plain flat-topped boat, six blocks tall: a keel, planked sides up to a flat deck narrowing to bow and stern, a rail one block high round the
     * deck's edge, and one mast with a sail stepped at row {@code mastZ} (0 amidships, a galley's; near the stern, a merchant's).
     */
    private static List<Part> plain(int length, int width, int mastZ) {
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
        int top = DECK + 2 + length / 2;
        for (int y = DECK + 1; y <= top; y++) {
            parts.add(new Part(0, y, mastZ, mast));
        }
        int sailHalf = width / 2 + 1;
        for (int y = DECK + 4; y < top; y++) {
            for (int x = -sailHalf; x <= sailHalf; x++) {
                parts.add(new Part(x, y, mastZ + 1, sail)); // hung before the mast, so it is one sheet
            }
        }
        return List.copyOf(parts);
    }
}
