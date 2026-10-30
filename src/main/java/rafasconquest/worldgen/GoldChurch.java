package rafasconquest.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import rafasconquest.core.CityLayout;

import java.util.List;
import java.util.Optional;

/**
 * Where a city's gold church stands on its plot: vanilla's plains temple, turned so its entrance faces west (the road
 * beside the plot), centred on the plot, on the city's plate. Shared by the piece that builds it and by the setting of
 * its lords inside, so both agree.
 */
public final class GoldChurch {
    static final ResourceLocation TEMPLATE = new ResourceLocation("minecraft", "village/plains/houses/plains_temple_4");

    /** The template's turn, worked out once from where its entrance (the jigsaw) is. */
    private static volatile Rotation turn;

    public record Layout(StructureTemplate template, Rotation rotation, BlockPos origin, BoundingBox box) {
        public StructurePlaceSettings settings() {
            return new StructurePlaceSettings().setRotation(rotation);
        }

        /** Where the hold's ruler stands, and the city's lord beside them: the middle of the nave, facing the door. */
        public int[] ruler() {
            return new int[]{(box.minX() + box.maxX()) / 2 + 1, (box.minZ() + box.maxZ()) / 2};
        }

        public int[] lord() {
            return new int[]{(box.minX() + box.maxX()) / 2 - 1, (box.minZ() + box.maxZ()) / 2};
        }
    }

    private GoldChurch() {
    }

    /** The church on the plot whose north-west corner is (plotX, plotZ), its floor at {@code floor}; empty if vanilla's template is missing. */
    public static Optional<Layout> layout(StructureTemplateManager templates, int plotX, int plotZ, int floor) {
        Optional<StructureTemplate> found = templates.get(TEMPLATE);
        if (found.isEmpty()) {
            return Optional.empty();
        }
        StructureTemplate template = found.get();
        Rotation rotation = rotation(template);
        BoundingBox at0 = template.getBoundingBox(new StructurePlaceSettings().setRotation(rotation), BlockPos.ZERO);
        int minX = plotX + Math.max(0, (CityLayout.PLOT_SIZE - at0.getXSpan()) / 2);
        int minZ = plotZ + Math.max(0, (CityLayout.PLOT_SIZE - at0.getZSpan()) / 2);
        BlockPos origin = new BlockPos(minX - at0.minX(), floor, minZ - at0.minZ());
        return Optional.of(new Layout(template, rotation, origin,
                template.getBoundingBox(new StructurePlaceSettings().setRotation(rotation), origin)));
    }

    /** The turn that puts the template's entrance on its west side. */
    private static Rotation rotation(StructureTemplate template) {
        Rotation known = turn;
        if (known != null) {
            return known;
        }
        Rotation best = Rotation.NONE;
        int bestOffset = Integer.MAX_VALUE;
        for (Rotation r : Rotation.values()) {
            StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(r);
            BoundingBox box = template.getBoundingBox(settings, BlockPos.ZERO);
            List<StructureTemplate.StructureBlockInfo> jigsaws = template.filterBlocks(BlockPos.ZERO, settings, Blocks.JIGSAW);
            for (StructureTemplate.StructureBlockInfo j : jigsaws) {
                int offset = j.pos().getX() - box.minX();
                if (offset < bestOffset) {
                    bestOffset = offset;
                    best = r;
                }
            }
        }
        turn = best;
        return best;
    }
}
