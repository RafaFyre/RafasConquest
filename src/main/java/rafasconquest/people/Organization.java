package rafasconquest.people;

import java.util.List;

/**
 * A body of people across the nations, beside the characters and the nations themselves. For now the sword sanctums:
 * each headed by its Sword God (the sanctum's ruler), with the Sword Kings and Sword Saints trained there (indexes into
 * the realm's people), whatever nation they now serve. Planned by {@link Organizations}.
 */
public record Organization(String name, int nation, int head, List<Integer> kings, List<Integer> saints) {
    /** Every member: the head, then the kings, then the saints. */
    public List<Integer> members() {
        List<Integer> all = new java.util.ArrayList<>();
        if (head >= 0) {
            all.add(head);
        }
        all.addAll(kings);
        all.addAll(saints);
        return all;
    }
}
