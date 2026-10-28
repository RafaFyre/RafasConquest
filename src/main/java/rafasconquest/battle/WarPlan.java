package rafasconquest.battle;

import rafasconquest.battle.Doctrine.Formation;
import rafasconquest.battle.Doctrine.Strategy;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** How a general means to fight a battle: every formation and strategy of the doctrines they follow. */
public record WarPlan(List<Doctrine> doctrines, Set<Formation> formations, Set<Strategy> strategies) {
    /** No doctrine: the army fights in plain lines, as before doctrines. */
    public static final WarPlan NONE = new WarPlan(List.of(), EnumSet.noneOf(Formation.class), EnumSet.noneOf(Strategy.class));

    public static WarPlan of(List<Doctrine> doctrines) {
        Set<Formation> formations = EnumSet.noneOf(Formation.class);
        Set<Strategy> strategies = EnumSet.noneOf(Strategy.class);
        for (Doctrine d : doctrines) {
            formations.addAll(d.formations());
            strategies.addAll(d.strategies());
        }
        return new WarPlan(List.copyOf(doctrines), formations, strategies);
    }

    public boolean has(Formation formation) {
        return formations.contains(formation);
    }

    public boolean has(Strategy strategy) {
        return strategies.contains(strategy);
    }

    /** For reports: "Spear and Shield, Bait and Pincer: Great Spear, Great Shield; Decapitate, ...". */
    public String describe() {
        if (doctrines.isEmpty()) {
            return "no doctrine";
        }
        String names = doctrines.stream().map(Doctrine::title).collect(Collectors.joining(", "));
        String forms = formations.stream().map(Formation::title).collect(Collectors.joining(", "));
        String plans = strategies.stream().map(Strategy::title).collect(Collectors.joining(", "));
        return names + ": " + (forms.isEmpty() ? "" : forms + "; ") + plans;
    }
}
