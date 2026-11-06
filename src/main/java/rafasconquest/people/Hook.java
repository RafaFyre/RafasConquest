package rafasconquest.people;

/**
 * A hold one character has over another: a favour owed them, or a secret they know (the stronger). Spent to make the
 * other do as they ask (such as giving them a hold), and gone then; worth nothing once either has died.
 */
public record Hook(int holder, int target, Kind kind) {
    public enum Kind {
        FAVOUR("a favour owed"), SECRET("a secret known");

        private final String words;

        Kind(String words) {
            this.words = words;
        }

        public String words() {
            return words;
        }
    }
}
