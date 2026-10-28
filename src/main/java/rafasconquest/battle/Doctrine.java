package rafasconquest.battle;

import java.util.List;

/**
 * A school of war a faction follows: the formations and strategies its generals draw on when they plan a battle
 * ({@link WarPlan}).
 */
public enum Doctrine {
    SPEAR_AND_SHIELD("Spear and Shield",
            List.of(Formation.GREAT_SPEAR, Formation.GREAT_SHIELD),
            List.of(Strategy.DECAPITATE, Strategy.MASS_CASUALTIES, Strategy.CAVALRY_FLANK)),
    CHARGE_AND_DECAPITATE("Charge and Decapitate",
            List.of(),
            List.of(Strategy.UNRELENTING_CHARGE, Strategy.PRECISION_STRIKES, Strategy.DECAPITATING_CHARGE)),
    BAIT_AND_PINCER("Bait and Pincer",
            List.of(),
            List.of(Strategy.BAIT, Strategy.PINCER));

    private final String title;
    private final List<Formation> formations;
    private final List<Strategy> strategies;

    Doctrine(String title, List<Formation> formations, List<Strategy> strategies) {
        this.title = title;
        this.formations = formations;
        this.strategies = strategies;
    }

    public String title() {
        return title;
    }

    public List<Formation> formations() {
        return formations;
    }

    public List<Strategy> strategies() {
        return strategies;
    }

    /** How an army stands. */
    public enum Formation {
        /**
         * All the cavalry stands together in one great wedge, far behind the lines, and goes in as one to break the
         * enemy line; its riders can gather twice the momentum ({@link FieldRules#GREAT_SPEAR_MOMENTUM}).
         */
        GREAT_SPEAR("Great Spear"),
        /**
         * The front line's infantry bear shields, and each front file is three blocks deep, each block re-formed
         * {@link BattlePlan#SHIELD_FILES} wide and five deep: fifteen ranks.
         */
        GREAT_SHIELD("Great Shield");

        private final String title;

        Formation(String title) {
            this.title = title;
        }

        public String title() {
            return title;
        }
    }

    /** How a general means to win. */
    public enum Strategy {
        /** Spear and Shield: the cavalry goes for the enemy general. */
        DECAPITATE("Decapitate"),
        /** Spear and Shield: everyone goes for the most enemy soldiers they can reach. */
        MASS_CASUALTIES("Mass Casualties"),
        /** Spear and Shield: cavalry rides around to strike enemy blocks in the flank, where no wall stands. */
        CAVALRY_FLANK("Cavalry Flank"),
        /**
         * Charge and Decapitate: the general leads all the cavalry in a wedge, in two halves; when the leading half is
         * spent the other goes in, and back again. Those near the general take less harm while he leads.
         */
        UNRELENTING_CHARGE("Unrelenting Charge"),
        /** Charge and Decapitate: the cavalry strikes exposed enemy blocks: too far forward, open flanks, or worn. */
        PRECISION_STRIKES("Precision Strikes"),
        /** Charge and Decapitate: once the enemy general stands exposed, the unrelenting charge goes for him. */
        DECAPITATING_CHARGE("Decapitate"),
        /** Bait and Pincer: a block runs ahead to draw the enemy on, then falls back fighting into a pocket. */
        BAIT("Bait"),
        /**
         * Bait and Pincer: an enemy block drawn in by the bait is struck: by a waiting cavalry charge, surrounded by
         * infantry, or hit in the flank by infantry.
         */
        PINCER("Pincer");

        private final String title;

        Strategy(String title) {
            this.title = title;
        }

        public String title() {
            return title;
        }
    }
}
