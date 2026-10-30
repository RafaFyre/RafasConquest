package rafasconquest.people;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The world's people: its cultures (one for each {@link Homeland}) and faiths, every hold's holder ({@link Person}), the
 * bonds between them, and each nation's culture, faith, piety (0, worldly, to 1, devout) and people
 * ({@link Demographics}). Planned with the world map ({@link RealmPlanner}) and kept and sent with it.
 */
public record Realm(List<Culture> cultures, List<Faith> faiths, List<Person> people, List<Bond> bonds,
                    int[] nationCulture, int[] nationFaith, float[] nationPiety, List<Demographics> demographics) {
    public static final Realm EMPTY = new Realm(List.of(), List.of(), List.of(), List.of(), new int[0], new int[0], new float[0], List.of());

    /** A nation's people, or none if it has no record. */
    public Demographics demographics(int nation) {
        return nation >= 0 && nation < demographics.size() ? demographics.get(nation) : Demographics.NONE;
    }

    /** A nation's ruler (the holder of its capital, liege to no one), or -1. */
    public int ruler(int nation) {
        for (int i = 0; i < people.size(); i++) {
            Person p = people.get(i);
            if (p.nation() == nation && p.liege() < 0 && p.hold().kind() == Person.Kind.CAPITAL) {
                return i;
            }
        }
        return -1;
    }

    /** Two characters' bond, either way round: friends or rivals. */
    public record Bond(int a, int b, Kind kind) {
        public enum Kind {
            FRIEND, RIVAL
        }

        public boolean joins(int x, int y) {
            return a == x && b == y || a == y && b == x;
        }
    }

    /** Character {@code a}'s bond with {@code b}, or null. */
    public Bond.Kind bond(int a, int b) {
        for (Bond bond : bonds) {
            if (bond.joins(a, b)) {
                return bond.kind();
            }
        }
        return null;
    }

    /** The characters {@code a} has a bond of this kind with. */
    public List<Integer> bonded(int a, Bond.Kind kind) {
        List<Integer> found = new ArrayList<>();
        for (Bond bond : bonds) {
            if (bond.kind() == kind && (bond.a() == a || bond.b() == a)) {
                found.add(bond.a() == a ? bond.b() : bond.a());
            }
        }
        return found;
    }

    /** The characters whose liege is {@code a}. */
    public List<Integer> vassals(int a) {
        List<Integer> found = new ArrayList<>();
        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).liege() == a) {
                found.add(i);
            }
        }
        return found;
    }

    /** The character keeping the hold in a province, or -1. */
    public int holderOf(int province) {
        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).hold().province() == province) {
                return i;
            }
        }
        return -1;
    }

    /**
     * What {@code a} thinks of {@code b}, -100 to 100: shared traits please and opposed ones grate; a shared faith
     * pleases (the zealous most; the cynical don't care) and another faith grates; so with culture; a liege and vassal
     * are bound to each other; friends and rivals most of all.
     */
    public int opinion(int a, int b) {
        Person pa = people.get(a);
        Person pb = people.get(b);
        int opinion = 0;
        for (Personality t : pa.traits()) {
            if (pb.has(t)) {
                opinion += 10;
            } else if (pb.has(t.opposite())) {
                opinion -= 10;
            }
        }
        int faithWeight = pa.has(Personality.ZEALOUS) ? 2 : pa.has(Personality.CYNICAL) ? 0 : 1;
        opinion += faithWeight * (pa.faith() == pb.faith() ? 10 : -15);
        opinion += pa.culture() == pb.culture() ? 10 : -5;
        if (pa.liege() == b) {
            opinion += 15;
        } else if (pb.liege() == a) {
            opinion += 5;
        }
        Bond.Kind bond = bond(a, b);
        if (bond == Bond.Kind.FRIEND) {
            opinion += 60;
        } else if (bond == Bond.Kind.RIVAL) {
            opinion -= 60;
        }
        return Math.max(-100, Math.min(100, opinion));
    }

    /**
     * What a character is called with their hold: a nation's ruler by its rank (Emperor, King, Count of a City-state),
     * a city's lord a Duke, a town's a Count, a castle's a Lord; "Duchess", "Queen" and so on for women.
     */
    public static String title(Person p, int nationTier) {
        String[] male;
        switch (p.hold().kind()) {
            case CAPITAL -> male = switch (nationTier) {
                case 3 -> new String[]{"Emperor", "Empress"};
                case 2 -> new String[]{"King", "Queen"};
                case 1 -> new String[]{"Duke", "Duchess"};
                default -> new String[]{"Count", "Countess"};
            };
            case CITY -> male = new String[]{"Duke", "Duchess"};
            case TOWN -> male = new String[]{"Count", "Countess"};
            default -> male = new String[]{"Lord", "Lady"};
        }
        return male[p.female() ? 1 : 0];
    }

    public void write(DataOutputStream out) throws IOException {
        out.writeShort(cultures.size());
        for (Culture c : cultures) {
            out.writeUTF(c.name());
            out.writeByte(c.homeland().ordinal());
            writeStrings(out, c.starts());
            writeStrings(out, c.middles());
            writeStrings(out, c.maleEnds());
            writeStrings(out, c.femaleEnds());
            writeStrings(out, c.familyEnds());
        }
        out.writeShort(faiths.size());
        for (Faith f : faiths) {
            out.writeUTF(f.name());
            out.writeBoolean(f.cult());
        }
        out.writeShort(people.size());
        for (Person p : people) {
            out.writeUTF(p.given());
            out.writeUTF(p.family());
            out.writeBoolean(p.female());
            out.writeByte(p.age());
            out.writeShort(p.culture());
            out.writeShort(p.faith());
            out.writeByte(p.race());
            for (int s : p.stats()) {
                out.writeByte(s);
            }
            out.writeByte(p.traits().size());
            for (Personality t : p.traits()) {
                out.writeByte(t.ordinal());
            }
            out.writeByte(p.hold().kind().ordinal());
            out.writeShort(p.hold().province());
            out.writeInt(p.hold().x());
            out.writeInt(p.hold().z());
            out.writeShort(p.liege());
            out.writeShort(p.nation());
            out.writeBoolean(p.away());
        }
        out.writeInt(bonds.size());
        for (Bond b : bonds) {
            out.writeShort(b.a());
            out.writeShort(b.b());
            out.writeByte(b.kind().ordinal());
        }
        out.writeShort(nationCulture.length);
        for (int i = 0; i < nationCulture.length; i++) {
            out.writeShort(nationCulture[i]);
            out.writeShort(nationFaith[i]);
            out.writeFloat(nationPiety[i]);
        }
        out.writeShort(demographics.size());
        for (Demographics d : demographics) {
            out.writeInt(d.population());
            for (int h = 0; h < Homeland.values().length; h++) {
                out.writeFloat(d.cultures()[h]);
                out.writeFloat(d.races()[h]);
            }
        }
    }

    public static Realm read(DataInputStream in) throws IOException {
        int cultureCount = in.readUnsignedShort();
        List<Culture> cultures = new ArrayList<>(cultureCount);
        for (int i = 0; i < cultureCount; i++) {
            cultures.add(new Culture(i, in.readUTF(), Homeland.values()[in.readUnsignedByte()], readStrings(in), readStrings(in), readStrings(in), readStrings(in), readStrings(in)));
        }
        int faithCount = in.readUnsignedShort();
        List<Faith> faiths = new ArrayList<>(faithCount);
        for (int i = 0; i < faithCount; i++) {
            faiths.add(new Faith(i, in.readUTF(), in.readBoolean()));
        }
        int personCount = in.readUnsignedShort();
        List<Person> people = new ArrayList<>(personCount);
        for (int i = 0; i < personCount; i++) {
            String given = in.readUTF();
            String family = in.readUTF();
            boolean female = in.readBoolean();
            int age = in.readUnsignedByte();
            int culture = in.readShort();
            int faith = in.readShort();
            int race = in.readUnsignedByte();
            int[] stats = new int[Stat.values().length];
            for (int s = 0; s < stats.length; s++) {
                stats[s] = in.readUnsignedByte();
            }
            int traitCount = in.readUnsignedByte();
            List<Personality> traits = new ArrayList<>(traitCount);
            for (int t = 0; t < traitCount; t++) {
                traits.add(Personality.values()[in.readUnsignedByte()]);
            }
            Person.Hold hold = new Person.Hold(Person.Kind.values()[in.readUnsignedByte()], in.readShort(), in.readInt(), in.readInt());
            people.add(new Person(given, family, female, age, culture, faith, race, stats, List.copyOf(traits), hold, in.readShort(), in.readShort(),
                    in.readBoolean()));
        }
        int bondCount = in.readInt();
        List<Bond> bonds = new ArrayList<>(bondCount);
        for (int i = 0; i < bondCount; i++) {
            bonds.add(new Bond(in.readShort(), in.readShort(), Bond.Kind.values()[in.readUnsignedByte()]));
        }
        int nations = in.readUnsignedShort();
        int[] nationCulture = new int[nations];
        int[] nationFaith = new int[nations];
        float[] nationPiety = new float[nations];
        for (int i = 0; i < nations; i++) {
            nationCulture[i] = in.readShort();
            nationFaith[i] = in.readShort();
            nationPiety[i] = in.readFloat();
        }
        int counted = in.readUnsignedShort();
        List<Demographics> demographics = new ArrayList<>(counted);
        for (int i = 0; i < counted; i++) {
            int population = in.readInt();
            float[] cultureShares = new float[Homeland.values().length];
            float[] raceShares = new float[Homeland.values().length];
            for (int h = 0; h < cultureShares.length; h++) {
                cultureShares[h] = in.readFloat();
                raceShares[h] = in.readFloat();
            }
            demographics.add(new Demographics(population, cultureShares, raceShares));
        }
        return new Realm(cultures, faiths, people, bonds, nationCulture, nationFaith, nationPiety, demographics);
    }

    private static void writeStrings(DataOutputStream out, List<String> strings) throws IOException {
        out.writeByte(strings.size());
        for (String s : strings) {
            out.writeUTF(s);
        }
    }

    private static List<String> readStrings(DataInputStream in) throws IOException {
        int n = in.readUnsignedByte();
        List<String> strings = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            strings.add(in.readUTF());
        }
        return List.copyOf(strings);
    }
}
