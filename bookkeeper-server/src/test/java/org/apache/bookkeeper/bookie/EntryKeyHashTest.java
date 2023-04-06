package org.apache.bookkeeper.bookie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class EntryKeyHashTest {

    private EntryKey key1;
    private EntryKey key2;
    private EntryKey key3;

    public EntryKeyHashTest(EntryKey key1, EntryKey key2, EntryKey key3) {
        this.key1 = key1;
        this.key2 = key2;
        this.key3 = key3;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new EntryKey(0, 0), new EntryKey(0, 0), new EntryKey(0, 1)},
                {new EntryKey(Integer.MAX_VALUE, 0), new EntryKey(Integer.MAX_VALUE, 0), new EntryKey(Integer.MIN_VALUE, 1)},
                {new EntryKey(Integer.MIN_VALUE, Integer.MAX_VALUE), new EntryKey(Integer.MIN_VALUE, Integer.MAX_VALUE), new EntryKey(Integer.MAX_VALUE, Integer.MIN_VALUE)},
                {new EntryKey(1, 2), new EntryKey(1, 2), new EntryKey(1, 3)},
                {new EntryKey(2, 3), new EntryKey(2, 3), new EntryKey(3, 1)},
                {new EntryKey(3, 1), new EntryKey(3, 1), new EntryKey(2, 2)}
        });
    }

    @Test
    public void testHashCode() {
        // Two objects that are equal must have the same hashCode
        assertTrue(key1.equals(key2));
        assertEquals(key1.hashCode(), key2.hashCode());

        // Objects that are not equal can have the same hashCode, but this should be rare
        assertNotEquals(key1.hashCode(), key3.hashCode());
    }
}

/*Nel test di testEquals(), ho utilizzato due oggetti con gli stessi valori per ledgerId e entryId, e un oggetto con un valore diverso per entryId, in modo da verificare se il metodo equals() funziona correttamente confrontando solo i valori di ledgerId e entryId.

Nel test di testHashCode(), ho utilizzato gli stessi oggetti utilizzati in testEquals(), in modo da verificare se gli oggetti con gli stessi valori di ledgerId e entryId restituiscono lo stesso hashCode.

Inoltre, ho aggiunto alcuni parametri che verificano la corretta gestione del comparatore in situazioni limite, ad esempio quando i valori di ledgerId e entryId sono uguali ma in ordine diverso o quando i valori sono troppo grandi o troppo piccoli da gestire in modo corretto.*/