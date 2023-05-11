package org.apache.bookkeeper.bookie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class EntryKeyHashTest {
    private final long ledgerId;
    private final long entryId;

    public EntryKeyHashTest(long ledgerId, long entryId) {
        this.ledgerId = ledgerId;
        this.entryId = entryId;
        
    }

    @Parameters
    public static Object[][] data() {
        return new Object[][]{
        	
        	{-1,-1},
                {0L, 0L},
                {1L, 1L},
                {Long.MAX_VALUE, Long.MAX_VALUE} //28
        };
    }

    @Test
    public void testHashCode() {
        EntryKey key = new EntryKey(ledgerId, entryId);
        int actualHashCode = key.hashCode();
        int expectedHashCode = (int) (ledgerId * 13 ^ entryId * 17);
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void testGetEntryId() {
        EntryKey key = new EntryKey(ledgerId, entryId);
        assertEquals(entryId, key.getEntryId());
    }
    
    @Test
    public void testGetLedgerId() {
        EntryKey key = new EntryKey(ledgerId, entryId);
        assertEquals(ledgerId, key.getLedgerId());
    }
}



