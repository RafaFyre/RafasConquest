package rafasconquest.core;

/**
 * The ages a world's nations research through, in order. A world has an unbroken run of them ({@link Range}), chosen
 * when it is made; the Age of Bronze is off by default.
 */
public enum Age {
    BRONZE("Bronze", "The Age of Bronze"),
    STEEL("Steel", "The Age of Steel"),
    GUNPOWDER("Gunpowder", "The Age of Gunpowder"),
    STEAM("Steam", "The Age of Steam");

    public final String shortName;
    public final String title;

    Age(String shortName, String title) {
        this.shortName = shortName;
        this.title = title;
    }

    /** An age by its name, or the fallback for a missing or unknown one. */
    public static Age parse(String name, Age fallback) {
        for (Age age : values()) {
            if (age.name().equalsIgnoreCase(name == null ? "" : name.trim())) {
                return age;
            }
        }
        return fallback;
    }

    /** The ages a world has, first to last with none skipped, so ages that are on are always adjacent. */
    public record Range(Age first, Age last) {
        public static final Range DEFAULT = new Range(STEEL, STEAM);

        public Range {
            if (first.compareTo(last) > 0) {
                Age swap = first;
                first = last;
                last = swap;
            }
        }

        public boolean has(Age age) {
            return age.compareTo(first) >= 0 && age.compareTo(last) <= 0;
        }

        /** Whether an age can be ticked or unticked: only an end can go (never the last one left), only a neighbour can join. */
        public boolean canToggle(Age age) {
            if (has(age)) {
                return first != last && (age == first || age == last);
            }
            return age.ordinal() == first.ordinal() - 1 || age.ordinal() == last.ordinal() + 1;
        }

        /** The range with an age ticked or unticked; unchanged if that would leave a gap or no age at all. */
        public Range toggle(Age age) {
            if (!canToggle(age)) {
                return this;
            }
            if (!has(age)) {
                return age.compareTo(first) < 0 ? new Range(age, last) : new Range(first, age);
            }
            return age == first ? new Range(values()[first.ordinal() + 1], last) : new Range(first, values()[last.ordinal() - 1]);
        }
    }
}
