package rafasconquest.client;

import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.FormationSnapshot;
import rafasconquest.battle.Machine;

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

        /** A siege engine's model ({@link FormationSnapshot.Engine#kind}), a tower's bridge or a ladder {@code up} or not; always 3D. */
        static Shape engine(int kind, boolean up) {
            Body body = kind == FormationSnapshot.LADDER ? (up ? Body.LADDER_UP : Body.LADDER) : switch (Machine.values()[kind]) {
                case CULVERIN, SERPENTINE -> Body.CANNON;
                case MONS_MEG -> Body.BOMBARD;
                case HWACHA -> Body.HWACHA;
                case ARCBALLISTA -> Body.BALLISTA;
                case ONAGER -> Body.ONAGER;
                case TREBUCHET -> Body.TREBUCHET;
                case TOWER_CROSSBOW -> Body.TOWER_CROSSBOW;
                case BATTERING_RAM -> Body.RAM;
                case SIEGE_TOWER -> up ? Body.TOWER_UP : Body.TOWER;
                case MANTLET -> Body.MANTLET;
            };
            return new Shape(body, false, false, false, FigureGear.Weapon.NONE, false);
        }
    }

    /** How much bigger than its model an engine is drawn: the serpentine is a culverin's bigger sister. */
    static float engineSize(int kind) {
        return kind != FormationSnapshot.LADDER && Machine.values()[kind] == Machine.SERPENTINE ? 1.3F : 1;
    }

    enum Body {
        FOOT, RIDER, GENERAL, BEARER,
        TOWER, TOWER_UP, RAM, LADDER, LADDER_UP, MANTLET, ONAGER, TREBUCHET, BALLISTA, TOWER_CROSSBOW, CANNON, BOMBARD, HWACHA
    }

    static final int WOOD = 0x6B4A2B;
    static final int DARK_WOOD = 0x45301D;
    static final int PALE_WOOD = 0x9C7A4E;
    static final int HIDE = 0x7A5534;
    static final int IRON = 0x56585C;
    static final int BRONZE = 0x8C6A3A;

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
            default -> engine();
        }
    }

    /**
     * A siege engine, about the size Siegeworks makes it, facing forward (its front toward the wall or its target), in
     * blocks from the ground up. Wood and iron, with the army's banner on a tower.
     */
    private void engine() {
        switch (shape.body()) {
            case TOWER, TOWER_UP -> {
                wheels(2.2, 2.4, 0.6);
                box(0, 0, 2.4, 2.8, 0.6, 14, WOOD, Stuff.WOOD);
                box(0, -2.85, 2.4, 0.08, 0.6, 14, HIDE, Stuff.CLOTH); // hides over its front against fire
                for (double floor : new double[]{4.8, 9.4}) {
                    box(0, 0, 2.5, 2.9, floor, floor + 0.3, DARK_WOOD, Stuff.WOOD);
                }
                box(0, 0, 2.6, 3.0, 14, 14.6, DARK_WOOD, Stuff.WOOD); // its top's parapet
                box(0, 2.6, 0.06, 0.06, 14.6, 18, POLE, Stuff.WOOD);
                box(0.65, 2.6, 0.6, 0.04, 16.4, 17.9, p.mark(), Stuff.CLOTH);
                if (shape.body() == Body.TOWER_UP) {
                    box(0, -4.6, 2.0, 1.6, 13.5, 13.8, PALE_WOOD, Stuff.WOOD); // its bridge down onto the wall
                } else {
                    box(0, -3.0, 2.0, 0.12, 10.5, 14, PALE_WOOD, Stuff.WOOD); // its bridge up, closing its top floor
                }
            }
            case RAM -> {
                wheels(1.2, 2.2, 0.45);
                box(0, 0, 1.2, 3.0, 0.5, 2.3, WOOD, Stuff.WOOD);
                box(0, 0, 1.4, 3.1, 2.3, 2.7, HIDE, Stuff.CLOTH); // its roof of hides
                box(0, -3.3, 0.25, 0.4, 1.0, 1.5, IRON, Stuff.METAL); // the iron head
            }
            case LADDER -> {
                // On its carriers' shoulders, lying along the way they go.
                box(-0.3, 0, 0.05, 4.0, 1.55, 1.65, PALE_WOOD, Stuff.WOOD);
                box(0.3, 0, 0.05, 4.0, 1.55, 1.65, PALE_WOOD, Stuff.WOOD);
                for (int k = 0; k < 8; k++) {
                    box(0, -3.5 + k, 0.3, 0.04, 1.56, 1.64, PALE_WOOD, Stuff.WOOD);
                }
            }
            case LADDER_UP -> {
                box(-0.3, 0, 0.05, 0.05, 0, 10, PALE_WOOD, Stuff.WOOD);
                box(0.3, 0, 0.05, 0.05, 0, 10, PALE_WOOD, Stuff.WOOD);
                for (int k = 1; k < 10; k++) {
                    box(0, 0, 0.3, 0.04, k, k + 0.08, PALE_WOOD, Stuff.WOOD);
                }
            }
            case MANTLET -> {
                wheels(1.2, 0.3, 0.35);
                box(0, 0, 1.5, 0.12, 0.3, 2.4, WOOD, Stuff.WOOD);
                box(0, 0.6, 0.1, 0.5, 0, 1.6, DARK_WOOD, Stuff.WOOD); // its prop behind
            }
            case ONAGER -> {
                wheels(0.85, 1.1, 0.35);
                box(0, 0, 0.8, 1.5, 0.3, 0.8, WOOD, Stuff.WOOD);
                box(-0.65, -0.4, 0.1, 0.1, 0.8, 2.0, WOOD, Stuff.WOOD);
                box(0.65, -0.4, 0.1, 0.1, 0.8, 2.0, WOOD, Stuff.WOOD);
                box(0, -0.4, 0.75, 0.12, 1.9, 2.1, DARK_WOOD, Stuff.WOOD);
                box(0, 0.4, 0.08, 1.0, 0.85, 1.05, PALE_WOOD, Stuff.WOOD); // its arm, cocked back
                box(0, 1.35, 0.22, 0.22, 0.85, 1.2, HIDE, Stuff.CLOTH);
            }
            case TREBUCHET -> {
                box(0, 0, 1.5, 2.0, 0, 0.6, DARK_WOOD, Stuff.WOOD);
                box(-1.2, 0, 0.15, 0.15, 0.6, 6.5, WOOD, Stuff.WOOD);
                box(1.2, 0, 0.15, 0.15, 0.6, 6.5, WOOD, Stuff.WOOD);
                box(0, 0, 1.3, 0.15, 6.3, 6.6, DARK_WOOD, Stuff.WOOD); // the axle
                box(0, 1.6, 0.12, 2.6, 6.3, 6.6, PALE_WOOD, Stuff.WOOD); // its arm, cocked back
                box(0, -1.0, 0.7, 0.7, 3.6, 5.4, IRON, Stuff.METAL); // the counterweight
            }
            case BALLISTA -> {
                box(0, 0, 0.7, 0.7, 0, 0.15, DARK_WOOD, Stuff.WOOD);
                box(0, 0, 0.12, 0.12, 0.15, 1.1, WOOD, Stuff.WOOD);
                box(0, 0, 0.15, 1.0, 1.1, 1.35, WOOD, Stuff.WOOD);
                box(0, -0.8, 1.0, 0.08, 1.15, 1.35, DARK_WOOD, Stuff.WOOD); // its bow
            }
            case TOWER_CROSSBOW -> {
                box(0, 0, 0.6, 0.6, 0, 0.8, WOOD, Stuff.WOOD);
                box(0, 0, 0.2, 1.4, 0.8, 1.15, WOOD, Stuff.WOOD);
                box(0, -1.2, 1.5, 0.1, 0.85, 1.1, DARK_WOOD, Stuff.WOOD);
            }
            case CANNON -> {
                box(0, 0.3, 0.45, 1.0, 0.4, 0.9, WOOD, Stuff.WOOD);
                box(-0.55, 0, 0.1, 0.5, 0, 1.0, DARK_WOOD, Stuff.WOOD);
                box(0.55, 0, 0.1, 0.5, 0, 1.0, DARK_WOOD, Stuff.WOOD);
                box(0, -0.3, 0.18, 1.4, 0.9, 1.25, BRONZE, Stuff.METAL);
            }
            case BOMBARD -> {
                box(0, 0, 0.7, 1.6, 0, 0.6, WOOD, Stuff.WOOD);
                box(0, -0.3, 0.6, 2.2, 0.6, 1.8, IRON, Stuff.METAL);
            }
            case HWACHA -> {
                box(-0.7, 0.3, 0.08, 0.45, 0, 0.9, DARK_WOOD, Stuff.WOOD);
                box(0.7, 0.3, 0.08, 0.45, 0, 0.9, DARK_WOOD, Stuff.WOOD);
                box(0, 0.2, 0.6, 0.9, 0.5, 0.8, WOOD, Stuff.WOOD);
                box(0, -0.1, 0.55, 0.35, 0.8, 1.8, DARK_WOOD, Stuff.WOOD); // its rack of rockets
                box(0, -0.45, 0.5, 0.05, 0.9, 1.7, IRON, Stuff.METAL);
            }
            default -> {
            }
        }
    }

    /** Four wheels at the corners, {@code right} out and {@code back} along, {@code radius} high. */
    private void wheels(double right, double back, double radius) {
        for (int i = 0; i < 4; i++) {
            box(i % 2 == 0 ? -right : right, i < 2 ? -back : back, 0.12, radius, 0, radius * 2, DARK_WOOD, Stuff.WOOD);
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
