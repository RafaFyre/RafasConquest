package rafasconquest.battle;

import rafasconquest.battle.Doctrine.Distribution;
import rafasconquest.battle.Doctrine.Formation;
import rafasconquest.battle.Doctrine.Strategy;

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * How a general means to fight a battle: every formation and strategy of the doctrines they follow, and how they share
 * their troops between the left wing, centre and right wing ({@code distribution}; a stacked wing takes
 * {@code stackShare} of them).
 */
public record WarPlan(List<Doctrine> doctrines, Set<Formation> formations, Set<Strategy> strategies,
                      Distribution distribution, double stackShare) {
    /** No doctrine: the army fights in plain lines, its troops shared evenly between its wings. */
    public static final WarPlan NONE = new WarPlan(List.of(), EnumSet.noneOf(Formation.class), EnumSet.noneOf(Strategy.class),
            Distribution.EVEN, 1.0 / 3);

    public static WarPlan of(List<Doctrine> doctrines) {
        return of(doctrines, Distribution.EVEN, 1.0 / 3);
    }

    public static WarPlan of(List<Doctrine> doctrines, Distribution distribution, double stackShare) {
        Set<Formation> formations = EnumSet.noneOf(Formation.class);
        Set<Strategy> strategies = EnumSet.noneOf(Strategy.class);
        for (Doctrine d : doctrines) {
            formations.addAll(d.formations());
            strategies.addAll(d.strategies());
        }
        return new WarPlan(List.copyOf(doctrines), formations, strategies, distribution, stackShare);
    }

    public boolean has(Formation formation) {
        return formations.contains(formation);
    }

    public boolean has(Strategy strategy) {
        return strategies.contains(strategy);
    }

    /** Each wing's share of the troops: {left, centre, right}. */
    public double[] wingShares() {
        double rest = (1 - stackShare) / 2;
        return switch (distribution) {
            case EVEN -> new double[]{1.0 / 3, 1.0 / 3, 1.0 / 3};
            case STACKED_LEFT -> new double[]{stackShare, rest, rest};
            case STACKED_CENTRE -> new double[]{rest, stackShare, rest};
            case STACKED_RIGHT -> new double[]{rest, rest, stackShare};
        };
    }

    /** For reports: "Spear and Shield, Bait and Pincer: Great Spear, Great Shield; Decapitate, ...; Stacked Left (41 %)". */
    public String describe() {
        String spread = distribution == Distribution.EVEN ? distribution.title()
                : String.format(Locale.ROOT, "%s (%d %%)", distribution.title(), Math.round(stackShare * 100));
        if (doctrines.isEmpty()) {
            return "no doctrine; " + spread;
        }
        String names = doctrines.stream().map(Doctrine::title).collect(Collectors.joining(", "));
        String forms = formations.stream().map(Formation::title).collect(Collectors.joining(", "));
        String plans = strategies.stream().map(Strategy::title).collect(Collectors.joining(", "));
        return names + ": " + (forms.isEmpty() ? "" : forms + "; ") + plans + "; " + spread;
    }
}
