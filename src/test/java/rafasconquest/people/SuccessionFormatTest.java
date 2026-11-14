package rafasconquest.people;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/** The succession's saved bytes: read back as written, old saves still read, and unreadable ones start anew instead of throwing. */
class SuccessionFormatTest {
    private static byte[] sample() {
        Succession s = new Succession();
        s.hook(1, 2, Hook.Kind.FAVOUR);
        s.hook(3, 4, Hook.Kind.SECRET);
        return s.encode();
    }

    private static final byte[] EMPTY = new Succession().encode();

    @Test
    void readsBackWhatItWrote() {
        assertArrayEquals(sample(), Succession.decode(sample()).encode());
    }

    @Test
    void readsASaveFromBeforeTheFormat() {
        byte[] legacy = Arrays.copyOfRange(sample(), 3, sample().length); // without the mark and format byte
        assertArrayEquals(sample(), Succession.decode(legacy).encode());
    }

    @Test
    void anotherFormatStartsAnew() {
        byte[] other = sample();
        other[2] = (byte) (Succession.FORMAT + 1);
        assertArrayEquals(EMPTY, Succession.decode(other).encode());
    }

    @Test
    void cutShortStartsAnew() {
        byte[] cut = Arrays.copyOf(sample(), sample().length - 2);
        assertArrayEquals(EMPTY, Succession.decode(cut).encode());
    }
}
