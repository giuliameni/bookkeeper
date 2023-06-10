package org.apache.bookkeeper.bookie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class EntryKeyEqualNewTest {

    private final EntryKey entryKey;
    private final Object other;
    private final boolean expectedResult;

    public EntryKeyEqualNewTest(EntryKey entryKey, Object other, boolean expectedResult) {
        this.entryKey = entryKey;
        this.other = other;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Object[][] parameters() {
        return new Object[][]{
                {new EntryKey(0, 0), new EntryKey(0, 0), true},
                {new EntryKey(0, 0), new EntryKey(0, 1), false},
                {new EntryKey(0, 0), null, false},
                {new EntryKey(0, 0), -1, false},
                {new EntryKey(0, 0), 0, false},
                {new EntryKey(0, 0), Long.MAX_VALUE, false}
        };
    }

    @Test
    public void testEquals() {
        assertEquals(expectedResult, entryKey.equals(other));
    }
    @Test
    public void testDefaultConstructor() {
        EntryKey key1 = new EntryKey(1,2);
        EntryKey key2 = new EntryKey(1,2);
        EntryKey key3 = new EntryKey(3,4);
        assertTrue(key1.equals(key2));
        assertFalse(key1.equals(key3));
    }
}
