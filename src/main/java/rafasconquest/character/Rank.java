package rafasconquest.character;

/**
 * A military rank, and how many soldiers someone of that rank leads in a battle. Ranks 1 to 4 are enlisted, who take
 * specific orders; 5 to 8 are officers, who take broad objectives. Every culture is feudal for now, with these titles;
 * other cultures will name them otherwise.
 */
public enum Rank {
    SOLDIER(1, "Soldier", 1),
    CORPORAL(2, "Corporal", 5),
    SERJEANT(3, "Serjeant", 20),
    CAPTAIN(4, "Captain", 100),
    MAJOR(5, "Major", 500),
    COLONEL(6, "Colonel", 1000),
    MARSHAL(7, "Marshal", 3000),
    GRAND_MARSHAL(8, "Grand Marshal", 10_000);

    private final int number;
    private final String title;
    private final int leads;

    Rank(int number, String title, int leads) {
        this.number = number;
        this.title = title;
        this.leads = leads;
    }

    public int number() {
        return number;
    }

    /** The feudal title. */
    public String title() {
        return title;
    }

    /** How many soldiers someone of this rank leads in a battle (themselves included). */
    public int leads() {
        return leads;
    }

    /** An officer (rank 5 and up), who takes broad objectives rather than specific orders. */
    public boolean officer() {
        return number >= 5;
    }

    public static Rank of(int number) {
        for (Rank r : values()) {
            if (r.number == number) {
                return r;
            }
        }
        return number < 1 ? SOLDIER : GRAND_MARSHAL;
    }
}
