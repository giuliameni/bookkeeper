package org.apache.bookkeeper.bookie;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import org.apache.bookkeeper.bookie.storage.ldb.ReadCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class WriteCachePutTest {

    private final ByteBufAllocator allocator;
    private final long maxCacheSize;
    private final int maxSegmentSize;

    @Parameterized.Parameters
    public static Collection<Object[]> configure() {
        return Arrays.asList(new Object[][]{
                {ByteBufAllocator.DEFAULT, 10 * 1024 * 1024, 1 * 1024 * 1024},
                {ByteBufAllocator.DEFAULT, 100 * 1024 * 1024, 2 * 1024 * 1024},
                {ByteBufAllocator.DEFAULT, 1000 * 1024 * 1024, 4 * 1024 * 1024}
        });
    }

    public WriteCachePutTest(ByteBufAllocator allocator, long maxCacheSize, int maxSegmentSize) {
        this.allocator = allocator;
        this.maxCacheSize = maxCacheSize;
        this.maxSegmentSize = maxSegmentSize;
    }

    @Test
    public void testGet() {
        ReadCache cache = new ReadCache(allocator, maxCacheSize, maxSegmentSize);

        // Add test entries
        long ledgerId1 = 1;
        long entryId1 = 1;
        ByteBuf entry1 = allocator.buffer(1024);
        cache.put(ledgerId1, entryId1, entry1);

        long ledgerId2 = 2;
        long entryId2 = 2;
        ByteBuf entry2 = allocator.buffer(2048);
        cache.put(ledgerId2, entryId2, entry2);

        // Test get() for existing entries
        ByteBuf result1 = cache.get(ledgerId1, entryId1);
        assertNotNull(result1);
        assertEquals(entry1, result1);

        ByteBuf result2 = cache.get(ledgerId2, entryId2);
        assertNotNull(result2);
        assertEquals(entry2, result2);

        // Test get() for non-existing entries
        long nonExistingLedgerId = 3;
        long nonExistingEntryId = 3;
        ByteBuf result3 = cache.get(nonExistingLedgerId, nonExistingEntryId);
        assertNull(result3);

        // Clean up
        entry1.release();
        entry2.release();
        cache.close();
    }
    @Test
    public void testPutWithRollOver() {
        ReadCache cache = new ReadCache(allocator, maxCacheSize, maxSegmentSize);

        // Set up the initial state with a non-empty current segment
        long ledgerId = 1;
        long entryId = 1;
        ByteBuf entry = allocator.buffer(maxSegmentSize);
        cache.put(ledgerId, entryId, entry);

        // Add entry that exceeds the remaining space in the current segment
        entryId = 2;
        entry = allocator.buffer(maxSegmentSize);
        cache.put(ledgerId, entryId, entry);

        // Verify roll-over and entry insertion in the new segment
        assertNotNull(cache.get(ledgerId, entryId));

        // Clean up
        cache.close();
    }
    
    
}


