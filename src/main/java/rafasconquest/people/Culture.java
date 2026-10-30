package rafasconquest.people;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A people's culture: its name, and the sounds its names are made of. Each culture draws its own few beginnings and
 * endings from the common stock, so its people's names sound alike and unlike a neighbour's. Every character of a
 * nation has the nation's culture.
 */
public record Culture(int id, String name, List<String> starts, List<String> middles, List<String> maleEnds,
                      List<String> femaleEnds, List<String> familyEnds) {
    private static final String[] STARTS = {
            "Al", "Bar", "Cal", "Dor", "El", "Fen", "Gal", "Har", "Ist", "Jor", "Kar", "Lor", "Mar", "Nor", "Os", "Pel",
            "Quar", "Ros", "Sar", "Tal", "Ul", "Val", "Wes", "Xan", "Yr", "Zan", "Ash", "Bel", "Cor", "Dun", "Ev", "Gor",
            "Hel", "Kel", "Mor", "Thal", "Vor", "Ar", "Bran", "Drak", "Ed", "Fal", "Hod", "Ing", "Leo", "Os", "Rag", "Sig",
            "Tan", "Wil", "Aud", "Bryn", "Cyn", "Dag", "Eir", "Frey", "Gud", "Hild", "Ivo", "Kat"};
    private static final String[] MIDDLES = {"", "", "", "a", "e", "i", "o", "an", "en", "or", "ar", "il", "un", "ri", "lo"};
    private static final String[] MALE_ENDS = {
            "ric", "mund", "bert", "win", "old", "ald", "ard", "ulf", "o", "an", "us", "ias", "ek", "or", "ir", "ain",
            "ald", "rad", "fred", "gar"};
    private static final String[] FEMALE_ENDS = {
            "a", "ia", "wyn", "hild", "eth", "ine", "ra", "isa", "una", "elle", "id", "gard", "is", "ora", "ena"};
    private static final String[] FAMILY_ENDS = {
            "ford", "wood", "more", "hart", "holt", "ridge", "brook", "wick", "helm", "worth", "bridge", "thorne",
            "court", "lock", "ward", "stone", "vale", "mark", "dane", "crest", "sson", "ovic", "ez", "elli", "anu"};
    private static final String[] ADJECTIVE_ENDS = {"ish", "ian", "ic", "an", "ese", "i", "ite"};

    /** A new culture, its sounds chosen from the common stock by {@code random}. */
    static Culture make(int id, Random random) {
        List<String> starts = pick(STARTS, 9, random);
        String name = starts.get(0) + pick(MIDDLES, 1, random).get(0) + ADJECTIVE_ENDS[random.nextInt(ADJECTIVE_ENDS.length)];
        return new Culture(id, name, starts, pick(MIDDLES, 5, random), pick(MALE_ENDS, 5, random), pick(FEMALE_ENDS, 4, random),
                pick(FAMILY_ENDS, 5, random));
    }

    private static List<String> pick(String[] stock, int n, Random random) {
        List<String> all = new ArrayList<>(List.of(stock));
        java.util.Collections.shuffle(all, random);
        return List.copyOf(all.subList(0, Math.min(n, all.size())));
    }

    /** A given name of this culture. */
    String givenName(boolean female, Random random) {
        String start = starts.get(random.nextInt(starts.size()));
        String middle = middles.get(random.nextInt(middles.size()));
        List<String> ends = female ? femaleEnds : maleEnds;
        return start + (middle.length() > 1 ? "" : middle) + ends.get(random.nextInt(ends.size()));
    }

    /** A family name of this culture. */
    String familyName(Random random) {
        String start = starts.get(random.nextInt(starts.size()));
        return start + middles.get(random.nextInt(middles.size())) + familyEnds.get(random.nextInt(familyEnds.size()));
    }
}
