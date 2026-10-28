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
public record GameCharacter(String name, Role role, List<Doctrine> doctrines) {
    public enum Role {
        KING, MERCHANT, WARRIOR, POLITICIAN, TACTICIAN, ADVISOR, GENERAL
    }

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
     * them if the faction has two or fewer).
     */
    public static GameCharacter general(List<Doctrine> factionDoctrines, Random random) {
        List<Doctrine> pool = new ArrayList<>(factionDoctrines);
        List<Doctrine> chosen = new ArrayList<>();
        while (chosen.size() < 2 && !pool.isEmpty()) {
            chosen.add(pool.remove(random.nextInt(pool.size())));
        }
        chosen.sort(null);
        return new GameCharacter(name(random), Role.GENERAL, List.copyOf(chosen));
    }

    public static String name(Random random) {
        return GIVEN[random.nextInt(GIVEN.length)] + " " + FAMILY[random.nextInt(FAMILY.length)];
    }

    public WarPlan plan() {
        return WarPlan.of(doctrines);
    }
}
