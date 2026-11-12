package rafasconquest.client;

import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.FormationSnapshot;

/**
 * A soldier's 3D level of detail: little upright boxes, in the soldier's own frame ({@code right} along its rank,
 * {@code back} away from the way it faces, {@code up} from the feet). Built once per {@link Shape} into a model the
 * graphics card keeps ({@link FigureBatches}); each soldier then only says where it stands, which way it faces
 * ({@link FigureMotion}) and its {@link Palette}.
 */
final class SoldierFigure {
    /** Past this many blocks (flat) a soldier is a sprite, not a 3D figure. Ships switch further out ({@link DistantShips#SPRITE_PAST}). */
    static final double SPRITE_PAST = 500;
    static final int SKIN = 0xBD8E6E;
    static final int GOLD = 0xF9D84A;
    static final int POLE = 0x6B4A2B;
    static final int HOOVES = 0x3B2A1C;
    /** A colour with this bit names a {@link Palette} slot (its low 3 bits) rather than being one. */
    static final int SLOT = 0x1000000;

    enum Stuff { CLOTH, METAL, WOOD }

    /** Takes each box, in the soldier's own frame. */
    interface Boxes {
        void box(double right, double back, double halfWidth, double halfDepth, double bottom, double top, int rgb, Stuff stuff);
    }

    /** What a figure is made of, apart from its colours: soldiers of the same shape share one model. */
    record Shape(Body body, boolean leader, boolean armed, boolean shield, FigureGear.Weapon weapon, boolean flat) {
        static Shape of(FormationSnapshot f, @Nullable FigureGear gear, boolean leader, boolean flat) {
            Body body = switch (f.kind()) {
                case CAVALRY, HEAVY_CAVALRY -> Body.RIDER;
                case GENERAL -> Body.GENERAL;
                case BEARER -> Body.BEARER;
                default -> Body.FOOT;
            };
            return gear == null ? new Shape(body, leader, false, false, FigureGear.Weapon.NONE, flat)
                    : new Shape(body, leader, true, gear.shield(), gear.weapon(), flat);
        }
    }

    enum Body { FOOT, RIDER, GENERAL, BEARER }

    /** A figure's colours: its soldiers' gear where known, else the army's. Slot order as {@link #SLOTS}. */
    record Palette(int chest, int legs, int cap, int mark, int horse, int off, int main, int helm) {
        /** Every slot named, not filled in: what a model is built with. */
        static final Palette SLOTS = new Palette(SLOT, SLOT | 1, SLOT | 2, SLOT | 3, SLOT | 4, SLOT | 5, SLOT | 6, SLOT | 7);

        static Palette of(FormationSnapshot f, @Nullable FigureGear gear) {
            int uniform = f.rgb();
            return new Palette(or(gear, FigureGear::chest, uniform), or(gear, FigureGear::legs, or(gear, FigureGear::feet, darker(uniform))),
                    or(gear, FigureGear::head, uniform), f.mark(), or(gear, FigureGear::horse, uniform), or(gear, FigureGear::off, uniform),
                    or(gear, FigureGear::main, POLE), or(gear, FigureGear::head, GOLD));
        }

        int[] slots() {
            return new int[]{chest, legs, cap, mark, horse, off, main, helm};
        }
    }

    private final Shape shape;
    private final Palette p;
    private final Boxes out;

    private SoldierFigure(Shape shape, Palette p, Boxes out) {
        this.shape = shape;
        this.p = p;
        this.out = out;
    }

    static void build(Shape shape, Palette palette, Boxes out) {
        new SoldierFigure(shape, palette, out).build();
    }

    /** The colour of a piece of gear, or {@code otherwise} when none is worn there (or the gear isn't known). */
    static int or(@Nullable FigureGear gear, java.util.function.ToIntFunction<FigureGear> piece, int otherwise) {
        int rgb = gear == null ? -1 : piece.applyAsInt(gear);
        return rgb < 0 ? otherwise : rgb;
    }

    private void build() {
        switch (shape.body()) {
            case RIDER -> {
                box(0, 0, 0.2, 0.25, 0, 0.7, HOOVES, Stuff.CLOTH);
                box(0, 0, 0.3, 0.6, 0.7, 1.3, p.horse(), Stuff.CLOTH); // the horse, in its barding
                box(0, -0.75, 0.15, 0.2, 1.1, 1.7, p.horse(), Stuff.CLOTH); // its head, forward
                box(0, 0, 0.25, 0.15, 1.3, 1.65, p.legs(), Stuff.CLOTH);
                box(0, 0, 0.3, 0.2, 1.65, 2.3, p.chest(), Stuff.CLOTH);
                head(2.3);
                arms(0.9);
            }
            case GENERAL -> {
                body();
                box(0, 0, 0.22, 0.22, 1.45, 1.7, SKIN, Stuff.CLOTH);
                box(0, 0, 0.27, 0.27, 1.7, 2.0, p.helm(), Stuff.METAL);
                box(0, 0.3, 0.04, 0.04, 1.0, 3.3, POLE, Stuff.WOOD); // the banner on the back
                box(0.3, 0.3, 0.27, 0.04, 2.4, 3.25, p.mark(), Stuff.CLOTH);
            }
            case BEARER -> {
                body();
                head(1.45);
                box(0.45, 0, 0.04, 0.04, 0.3, 3.3, POLE, Stuff.WOOD);
                box(0.77, 0, 0.28, 0.04, 2.3, 3.25, p.mark(), Stuff.CLOTH);
            }
            case FOOT -> {
                body();
                head(1.45);
                arms(0);
            }
        }
    }

    /** What the soldier carries: a shield on the left arm, a weapon in the right hand; {@code lift} up, for a rider. */
    private void arms(double lift) {
        if (!shape.armed()) {
            return;
        }
        if (shape.shield()) {
            box(-0.32, -0.2, 0.25, 0.04, lift + 0.55, lift + 1.35, p.off(), Stuff.WOOD);
        }
        switch (shape.weapon()) {
            case SWORD -> box(0.38, -0.1, 0.04, 0.04, lift + 0.85, lift + 1.85, p.main(), Stuff.METAL);
            case GREAT -> box(0.38, -0.1, 0.05, 0.05, lift + 0.7, lift + 2.6, p.main(), Stuff.METAL);
            case LONG -> box(0.38, -0.1, 0.03, 0.03, lift + 0.3, lift + 3.1, p.main(), Stuff.WOOD);
            case BOW -> box(0.38, -0.15, 0.03, 0.1, lift + 0.55, lift + 1.85, p.main(), Stuff.WOOD);
            case NONE -> {
            }
        }
    }

    private void body() {
        box(0, 0, 0.25, 0.13, 0, 0.75, p.legs(), Stuff.CLOTH);
        box(0, 0, 0.3, 0.15, 0.75, 1.45, p.chest(), Stuff.CLOTH);
    }

    /** A face and a leather cap; a leader wears the faction banner instead, standing tall on the head. */
    private void head(double bottom) {
        box(0, 0, 0.22, 0.22, bottom, bottom + 0.3, SKIN, Stuff.CLOTH);
        if (shape.leader()) {
            box(0, 0, 0.04, 0.04, bottom + 0.3, bottom + 0.55, POLE, Stuff.WOOD);
            box(0, 0, 0.28, 0.04, bottom + 0.55, bottom + 1.45, p.mark(), Stuff.CLOTH);
        } else {
            box(0, 0, 0.25, 0.25, bottom + 0.3, bottom + 0.48, p.cap(), Stuff.CLOTH);
        }
    }

    private void box(double right, double back, double halfWidth, double halfDepth, double bottom, double top, int rgb, Stuff stuff) {
        out.box(right, back, halfWidth, halfDepth, bottom, top, rgb, stuff);
    }

    static int darker(int rgb) {
        return (int) ((rgb >> 16 & 255) * 0.7) << 16 | (int) ((rgb >> 8 & 255) * 0.7) << 8 | (int) ((rgb & 255) * 0.7);
    }
}
