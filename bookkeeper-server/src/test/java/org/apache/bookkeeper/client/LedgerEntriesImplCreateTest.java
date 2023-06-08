package org.apache.bookkeeper.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.bookkeeper.client.api.LedgerEntry;
import org.apache.bookkeeper.client.impl.LedgerEntriesImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import io.netty.util.Recycler;

import org.junit.runners.Parameterized;


@RunWith(Parameterized.class)
public class LedgerEntriesImplCreateTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
        	{null, true},
            // empty list of entries
            { Collections.emptyList(), true },
            // list of one entry
            { Arrays.asList(mock(LedgerEntry.class)), false },
        });
    }

    private List<org.apache.bookkeeper.client.api.LedgerEntry> entries;
    private boolean expectException;
    private List<LedgerEntry> entriesClose;
    private LedgerEntry entry1, entry2, entry3;
    private LedgerEntriesImpl ledgerEntries;
    @Before
    public void setup() {
        entriesClose = new ArrayList<>();
        entry1 = mock(LedgerEntry.class);
        entry2 = mock(LedgerEntry.class);
        entry3 = mock(LedgerEntry.class);
        entriesClose.add(entry1);
        entriesClose.add(entry2);
        entriesClose.add(entry3);
        ledgerEntries = LedgerEntriesImpl.create(entriesClose);
    }
    public LedgerEntriesImplCreateTest(List<org.apache.bookkeeper.client.api.LedgerEntry> entries, boolean expectException) {
        this.entries = entries;
        this.expectException = expectException;
    }
    @Test
    public void testClose() {
        ledgerEntries.close();
        verify(entry1).close();
        verify(entry2).close();
        verify(entry3).close();
        
    }
    @Test
    public void testRecyclerHandleOnClose() {
        Recycler.Handle<LedgerEntriesImpl> mockHandle = mock(Recycler.Handle.class);
        LedgerEntriesImpl entries = new LedgerEntriesImpl(mockHandle);
        entries.close();
        verify(mockHandle).recycle(entries);
    }
    @Test
    public void testReleaseByteBuf() {
    	List<LedgerEntry> entryList = mock(List.class);

    	LedgerEntriesImpl ledgerEntries = LedgerEntriesImpl.create(entryList);

    	ledgerEntries.close();

    	verify(entryList, times(1)).clear();
    }

    @Test
    public void testCreate() {
        LedgerEntriesImpl ledgerEntries = null;
    	if(entries == null) {
        	try {
                ledgerEntries = LedgerEntriesImpl.create(null);
        	}catch(NullPointerException e) {}
        }else {
        try {
            ledgerEntries = LedgerEntriesImpl.create(entries);
            int i = 0;
            Iterator<org.apache.bookkeeper.client.api.LedgerEntry> iterator = ledgerEntries.iterator();
            while(iterator.hasNext()) {
            	i ++;
            	iterator.next();
            }

            if (expectException) {
                fail("Expected IllegalArgumentException");
            }
            assertNotNull(ledgerEntries);
        } catch (IllegalArgumentException e) {
            if (!expectException) {
                fail("Unexpected IllegalArgumentException");
            }
        }

    }
    	
    	
       
       
    }
    
}







    




