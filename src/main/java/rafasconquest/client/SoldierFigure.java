package rafasconquest.client;

import rafasconquest.battle.FormationSnapshot;

/**
 * A soldier's 3D level of detail: little upright boxes, in the soldier's own frame ({@code right} along its rank,
 * {@code back} away from the enemy, {@code up} from the feet), drawn by {@link DistantArmyRenderer}, each box turned the
 * way its formation faces.
 */
final class SoldierFigure {
    /** Past this many blocks (flat) a soldier is a sprite, not a 3D figure. Ships switch further out ({@link DistantShips#SPRITE_PAST}). */
    static final double SPRITE_PAST = 500;
    static final int SKIN = 0xBD8E6E;
    static final int GOLD = 0xF9D84A;
    static final int POLE = 0x6B4A2B;
    static final int HOOVES = 0x3B2A1C;

    enum Stuff { CLOTH, METAL, WOOD }

    /**
     * Takes each box, from the soldier's feet (x and z in blocks east and south, y up): centred at ({@code cx},
     * {@code cz}), reaching ({@code rx}, {@code rz}) to its right and ({@code fx}, {@code fz}) to its front either way,
     * from {@code bottom} to {@code top}.
     */
    interface Boxes {
        void box(double cx, double cz, double rx, double rz, double fx, double fz, double bottom, double top, int rgb, Stuff stuff);
    }

    private final FormationSnapshot f;
    private final boolean leader;
    private final Boxes out;

    private SoldierFigure(FormationSnapshot f, boolean leader, Boxes out) {
        this.f = f;
        this.leader = leader;
        this.out = out;
    }

    static void build(FormationSnapshot f, boolean leader, Boxes out) {
        new SoldierFigure(f, leader, out).build();
    }

    private void build() {
        int uniform = f.rgb();
        int shade = darker(uniform);
        int mark = f.mark();
        switch (f.kind()) {
            case CAVALRY -> {
                box(0, 0, 0.2, 0.25, 0, 0.7, HOOVES, Stuff.CLOTH);
                box(0, 0, 0.3, 0.6, 0.7, 1.3, uniform, Stuff.CLOTH); // the horse, in leather armour
                box(0, -0.75, 0.15, 0.2, 1.1, 1.7, uniform, Stuff.CLOTH); // its head, forward
                box(0, 0, 0.25, 0.15, 1.3, 1.65, shade, Stuff.CLOTH);
                box(0, 0, 0.3, 0.2, 1.65, 2.3, uniform, Stuff.CLOTH);
                head(2.3, uniform, mark);
            }
            case GENERAL -> {
                body(uniform, shade);
                box(0, 0, 0.22, 0.22, 1.45, 1.7, SKIN, Stuff.CLOTH);
                box(0, 0, 0.27, 0.27, 1.7, 2.0, GOLD, Stuff.METAL);
                box(0, 0.3, 0.04, 0.04, 1.0, 3.3, POLE, Stuff.WOOD); // the banner on the back
                box(0.3, 0.3, 0.27, 0.04, 2.4, 3.25, mark, Stuff.CLOTH);
            }
            case BEARER -> {
                body(uniform, shade);
                head(1.45, uniform, mark);
                box(0.45, 0, 0.04, 0.04, 0.3, 3.3, POLE, Stuff.WOOD);
                box(0.77, 0, 0.28, 0.04, 2.3, 3.25, mark, Stuff.CLOTH);
            }
            default -> {
                body(uniform, shade);
                head(1.45, uniform, mark);
            }
        }
    }

    private void body(int uniform, int shade) {
        box(0, 0, 0.25, 0.13, 0, 0.75, shade, Stuff.CLOTH);
        box(0, 0, 0.3, 0.15, 0.75, 1.45, uniform, Stuff.CLOTH);
    }

    /** A face and a leather cap; a leader wears the faction banner instead, standing tall on the head. */
    private void head(double bottom, int cap, int mark) {
        box(0, 0, 0.22, 0.22, bottom, bottom + 0.3, SKIN, Stuff.CLOTH);
        if (leader) {
            box(0, 0, 0.04, 0.04, bottom + 0.3, bottom + 0.55, POLE, Stuff.WOOD);
            box(0, 0, 0.28, 0.04, bottom + 0.55, bottom + 1.45, mark, Stuff.CLOTH);
        } else {
            box(0, 0, 0.25, 0.25, bottom + 0.3, bottom + 0.48, cap, Stuff.CLOTH);
        }
    }

    /**
     * A box centred {@code right} to the soldier's right and {@code back} behind it, {@code halfWidth} to each side
     * along the rank and {@code halfDepth} front and back, from {@code bottom} to {@code top}.
     */
    private void box(double right, double back, double halfWidth, double halfDepth, double bottom, double top,
                     int rgb, Stuff stuff) {
        double rightX = -f.facingZ();
        double rightZ = f.facingX();
        out.box(rightX * right - f.facingX() * back, rightZ * right - f.facingZ() * back, rightX * halfWidth, rightZ * halfWidth,
                f.facingX() * halfDepth, f.facingZ() * halfDepth, bottom, top, rgb, stuff);
    }

    static int darker(int rgb) {
        return (int) ((rgb >> 16 & 255) * 0.7) << 16 | (int) ((rgb >> 8 & 255) * 0.7) << 8 | (int) ((rgb & 255) * 0.7);
    }
}
