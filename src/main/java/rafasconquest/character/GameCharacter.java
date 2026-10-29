package rafasconquest.character;

import rafasconquest.battle.Doctrine;
import rafasconquest.battle.WarPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A notable person of a faction: a king, merchant, warrior, politician, tactician, advisor or general. Only generals
 * act yet: they plan battles from their doctrines. For now characters are made for each battle and not kept.
 */
public record GameCharacter(String name, Role role, List<Doctrine> doctrines, Doctrine.Distribution distribution, double stackShare) {
    public enum Role {
        KING, MERCHANT, WARRIOR, POLITICIAN, TACTICIAN, ADVISOR, GENERAL
    }

    /** A stacked wing takes between this share of the troops and that. */
    private static final double STACK_LEAST = 0.35;
    private static final double STACK_MOST = 0.45;

    private static final String[] GIVEN = {
            "Aldric", "Bertrand", "Cedric", "Dorian", "Edric", "Falk", "Gerold", "Hadrian", "Ivo", "Jorund",
            "Konrad", "Leopold", "Matthias", "Norbert", "Osric", "Percival", "Ragnar", "Sigmund", "Tancred", "Ulric",
            "Wendel", "Adela", "Brunhild", "Cordelia", "Edda", "Gisela", "Hedwig", "Isolde", "Mathilde", "Sibylla"};
    private static final String[] FAMILY = {
            "Ashford", "Blackwood", "Crane", "Dunmore", "Everhart", "Fairholt", "Greaves", "Hollister", "Ironside",
            "Kestrel", "Lockridge", "Marrow", "Northam", "Oakhelm", "Ravensworth", "Stonebridge", "Thorne", "Valcourt",
            "Whitlock", "Wolfe"};

    /**
     * A general for a faction following {@code factionDoctrines}: they follow two of them, chosen at random (all of
     * them if the faction has two or fewer), and one of the four ways to share their troops between the wings
     * ({@link Doctrine.Distribution}), a stacked wing taking 35 to 45 % of them.
     */
    public static GameCharacter general(List<Doctrine> factionDoctrines, Random random) {
        List<Doctrine> pool = new ArrayList<>(factionDoctrines);
        List<Doctrine> chosen = new ArrayList<>();
        while (chosen.size() < 2 && !pool.isEmpty()) {
            chosen.add(pool.remove(random.nextInt(pool.size())));
        }
        chosen.sort(null);
        Doctrine.Distribution[] spreads = Doctrine.Distribution.values();
        Doctrine.Distribution spread = spreads[random.nextInt(spreads.length)];
        double share = STACK_LEAST + random.nextDouble() * (STACK_MOST - STACK_LEAST);
        return new GameCharacter(name(random), Role.GENERAL, List.copyOf(chosen), spread, share);
    }

    public static String name(Random random) {
        return GIVEN[random.nextInt(GIVEN.length)] + " " + FAMILY[random.nextInt(FAMILY.length)];
    }

    public WarPlan plan() {
        return WarPlan.of(doctrines, distribution, stackShare);
    }
}
