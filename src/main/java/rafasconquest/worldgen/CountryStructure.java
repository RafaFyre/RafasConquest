package rafasconquest.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import rafasconquest.core.Countryside;

import java.util.Optional;

/**
 * A settlement of the countryside, built where the world map planned it: a hamlet; a small village of 3 to 6 hamlets
 * set round a well, each with its pen toward it; a watchtower; or a castle. Each building stands on its own ground's
 * height, from the noise terrain, so a village follows the land.
 */
public class CountryStructure extends Structure {
    public static final Codec<CountryStructure> CODEC = simpleCodec(CountryStructure::new);
    /** How far a village's hamlets stand from its well. */
    private static final int VILLAGE_RING = 18;

    public CountryStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext ctx) {
        return ConquestRuntime.countryside().inChunk(ctx.chunkPos().x, ctx.chunkPos().z).map(s -> {
            BlockPos pos = new BlockPos(s.x(), ground(ctx, s.x(), s.z()), s.z());
            return new GenerationStub(pos, pieces -> build(ctx, s, pieces));
        });
    }

    private static void build(GenerationContext ctx, Countryside.Settlement s, StructurePiecesBuilder pieces) {
        int turns = Math.floorMod(s.x() * 31 + s.z() * 17, 4);
        // A structure from the style folders, where its slot has one; else built as it always has been.
        String styled = styled(ctx, s);
        if (styled != null) {
            java.util.List<rafasconquest.style.StylePiece> styledPieces = rafasconquest.style.StylePiece.of(styled, s.x(), ground(ctx, s.x(), s.z()) - 1, s.z(), turns);
            if (!styledPieces.isEmpty()) {
                styledPieces.forEach(pieces::addPiece);
                return;
            }
        }
        switch (s.kind()) {
            case HAMLET -> pieces.addPiece(new CountryPiece(CountryPiece.Build.HAMLET, s.x(), ground(ctx, s.x(), s.z()), s.z(), turns));
            case WATCHTOWER -> pieces.addPiece(new CountryPiece(CountryPiece.Build.TOWER, s.x(), ground(ctx, s.x(), s.z()), s.z(), turns));
            case CASTLE -> pieces.addPiece(new CountryPiece(CountryPiece.Build.CASTLE, s.x(), ground(ctx, s.x(), s.z()), s.z(), turns));
            case RUIN -> pieces.addPiece(new CountryPiece(CountryPiece.Build.RUIN, s.x(), ground(ctx, s.x(), s.z()), s.z(), turns));
            case VILLAGE -> {
                pieces.addPiece(new CountryPiece(CountryPiece.Build.WELL, s.x(), ground(ctx, s.x(), s.z()), s.z(), 0));
                for (int i = 0; i < s.hamlets(); i++) {
                    double angle = 2 * Math.PI * i / s.hamlets() + turns;
                    int x = s.x() + (int) Math.round(Math.cos(angle) * VILLAGE_RING);
                    int z = s.z() + (int) Math.round(Math.sin(angle) * VILLAGE_RING);
                    // The pen (each hamlet's +x side) toward the well: the quarter turn nearest the way back.
                    int face = Math.floorMod((int) Math.round((angle + Math.PI) / (Math.PI / 2)), 4);
                    pieces.addPiece(new CountryPiece(CountryPiece.Build.HAMLET, x, ground(ctx, x, z), z, face));
                }
            }
        }
    }

    /**
     * The style folders' structure for a settlement, if its slot has one: a castle of its size (by whose seat it is) in
     * its holder's style; a hamlet in its holder's style, or the wild's of its land if no one holds it; a ruin of the
     * wild's. Null for anything else, or an empty slot.
     */
    @org.jetbrains.annotations.Nullable
    private static String styled(GenerationContext ctx, Countryside.Settlement s) {
        rafasconquest.map.BaseMap map = ConquestRuntime.map();
        int province = map == null ? -1 : map.provinceIndexAt(s.x(), s.z());
        int nation = province < 0 ? -1 : map.holders()[province];
        long seed = (long) s.x() * 341873128712L + (long) s.z() * 132897987541L;
        rafasconquest.people.Homeland land = nation >= 0 ? rafasconquest.gear.Armoury.homeland(map, map.nations().get(nation).id())
                : rafasconquest.map.BaseMapGenerator.homeland(ctx.biomeSource().getNoiseBiome(net.minecraft.core.QuartPos.fromBlock(s.x()),
                        net.minecraft.core.QuartPos.fromBlock(64), net.minecraft.core.QuartPos.fromBlock(s.z()), ctx.randomState().sampler()));
        return switch (s.kind()) {
            case CASTLE -> {
                String size = "small";
                for (rafasconquest.map.BaseMap.Nation n : map == null ? java.util.List.<rafasconquest.map.BaseMap.Nation>of() : map.nations()) {
                    if (n.seat() == province) {
                        size = switch (n.tier()) {
                            case EMPIRE, KINGDOM -> "grand";
                            case DUCHY -> "large";
                            case COUNTY -> "medium";
                        };
                    }
                }
                java.util.List<String> sizes = new java.util.ArrayList<>(java.util.List.of(size, "medium", "large", "small", "grand"));
                String found = null;
                for (String z : sizes) {
                    found = found != null ? found : rafasconquest.style.StyleLibrary.pick(land, "lone/castles/" + z, seed);
                }
                yield found;
            }
            case HAMLET -> nation >= 0 ? rafasconquest.style.StyleLibrary.pick(land, "lone/hamlets", seed)
                    : rafasconquest.style.StyleLibrary.pickWorld(land, "hamlets", seed);
            case RUIN -> rafasconquest.style.StyleLibrary.pickWorld(land, "ruins", seed);
            default -> null;
        };
    }

    /** The first air above the noise terrain at (x, z): where a building's floor goes. */
    private static int ground(GenerationContext ctx, int x, int z) {
        return CityGridBuilder.ground(ctx, x, z).y() + 1;
    }

    @Override
    public StructureType<?> type() {
        return ConquestWorldgen.COUNTRY_STRUCTURE.get();
    }
}
