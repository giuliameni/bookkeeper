
package org.apache.bookkeeper.client;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.apache.bookkeeper.client.api.LedgerEntries;
import org.apache.bookkeeper.client.api.LedgerEntry;
import org.apache.bookkeeper.client.impl.LedgerEntriesImpl;
import org.junit.Before;
import org.junit.Test;

import io.netty.util.Recycler;

public class LedgereEntryCloseTest {
private List<LedgerEntry> entries;
private LedgerEntry entry1, entry2, entry3;
private LedgerEntriesImpl ledgerEntries;

@Before
public void setup() {
    entries = new ArrayList<>();
    entry1 = mock(LedgerEntry.class);
    entry2 = mock(LedgerEntry.class);
    entry3 = mock(LedgerEntry.class);
    entries.add(entry1);
    entries.add(entry2);
    entries.add(entry3);
    ledgerEntries = LedgerEntriesImpl.create(entries);
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
    // Create a mock LedgerEntry object
	List<LedgerEntry> entryList = mock(List.class);

	// Create a new LedgerEntriesImpl object with the mock list
	LedgerEntriesImpl ledgerEntries = LedgerEntriesImpl.create(entryList);

	// Call the close method to trigger the releaseByteBuf method
	ledgerEntries.close();

	// Verify that the clear method was called on the entry list
	verify(entryList, times(1)).clear();
}
















}




