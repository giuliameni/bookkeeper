package org.apache.bookkeeper.client;

import  org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.apache.bookkeeper.client.api.LedgerEntry;
import org.apache.bookkeeper.client.impl.LedgerEntriesImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class LedgerEntryGetEntryTest {

    private LedgerEntry mockLedgerEntry1;
    private LedgerEntry mockLedgerEntry2;
    private LedgerEntry mockLedgerEntry3;
    private List<LedgerEntry> entries;
    private LedgerEntriesImpl ledgerEntries;

    private long entryId;
    private boolean expectException;

    public LedgerEntryGetEntryTest(long entryId, boolean expectException) {
        this.entryId = entryId;
        this.expectException = expectException;
    }

    @Before
    public void setUp() {
        // Create mock entries
        mockLedgerEntry1 = mock(LedgerEntry.class);
        mockLedgerEntry2 = mock(LedgerEntry.class);
        mockLedgerEntry3 = mock(LedgerEntry.class);

        // Set entryIds
        when(mockLedgerEntry1.getEntryId()).thenReturn(1L);
        when(mockLedgerEntry2.getEntryId()).thenReturn(2L);
        when(mockLedgerEntry3.getEntryId()).thenReturn(3L);

        // Create entries and LedgerEntriesImpl
        entries = Arrays.asList(mockLedgerEntry1, mockLedgerEntry2, mockLedgerEntry3);
        ledgerEntries = LedgerEntriesImpl.create(entries);
    }

    @After
    public void tearDown() {
        mockLedgerEntry1 = null;
        mockLedgerEntry2 = null;
        mockLedgerEntry3 = null;
        entries = null;
        ledgerEntries = null;
    }

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][] { 
            { 2L, false }, // valid entryId
            { 1L, false }, // valid entryId
            { 3l,false},
            { 0L, true }, // invalid entryId
            { 4L,true},
            { -1L, true } // invalid entryId
        });
    }

    @Test
    public void testGetEntry() {
        if (expectException) {
            try {
                ledgerEntries.getEntry(entryId);
                fail("Expected IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException e) {
                // expected
            }
        } else {
            assertEquals(mockLedgerEntry1, ledgerEntries.getEntry(1L));
            assertEquals(mockLedgerEntry2, ledgerEntries.getEntry(2L));
            assertEquals(mockLedgerEntry3, ledgerEntries.getEntry(3L));
        }
    }

}
