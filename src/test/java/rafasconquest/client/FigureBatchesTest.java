package rafasconquest.client;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FigureBatchesTest {
    private static final int I = FigureBatches.VERTEX_INTS;

    @Test
    void footSoldierModel() {
        // legs, chest, face, cap, shield, sword: six boxes, each a top and four sides of two triangles
        int[] model = FigureBatches.bake(new SoldierFigure.Shape(SoldierFigure.Body.FOOT, false, true, true, FigureGear.Weapon.SWORD, false));
        assertEquals(6 * 5 * 6, model.length / I);
        Set<Integer> colours = new HashSet<>();
        for (int v = 0; v < model.length; v += I) {
            colours.add(model[v + 6]);
            float shade = Float.intBitsToFloat(model[v + 5]);
            float nr = Float.intBitsToFloat(model[v + 3]);
            float nf = Float.intBitsToFloat(model[v + 4]);
            assertTrue(shade == 1 || shade == 0 && Math.abs(nr) + Math.abs(nf) == 1, "a top, or a side facing one way");
        }
        // gear colours are named slots, filled per army; the face is always skin
        assertTrue(colours.contains(SoldierFigure.SLOT)); // chest
        assertTrue(colours.contains(SoldierFigure.SLOT | 5)); // shield
        assertTrue(colours.contains(SoldierFigure.SLOT | 6)); // sword
        assertTrue(colours.contains(SoldierFigure.SKIN));
    }

    @Test
    void flatFigureFacesTheCamera() {
        int[] model = FigureBatches.bake(new SoldierFigure.Shape(SoldierFigure.Body.RIDER, true, false, false, FigureGear.Weapon.NONE, true));
        for (int v = 0; v < model.length; v += I) {
            assertEquals(0F, Float.intBitsToFloat(model[v + 2]), "all in one plane, square to the camera");
            assertEquals(FigureBatches.FLAT_SHADE, Float.intBitsToFloat(model[v + 5]));
        }
    }

    @Test
    void sidesShadedAsBlocks() {
        assertEquals(0.8F, FigureBatches.sideShade(0, 1), 1e-6); // facing south
        assertEquals(0.6F, FigureBatches.sideShade(1, 0), 1e-6); // facing east
    }
}
