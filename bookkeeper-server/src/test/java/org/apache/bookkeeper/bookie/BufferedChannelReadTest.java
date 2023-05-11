package org.apache.bookkeeper.bookie;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.when;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collection;


import org.apache.bookkeeper.util.ZeroBuffer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class BufferedChannelReadTest {

    private static File file;
    private static FileChannel fileChannel;

    private final int pos;
    private final int destLen;
    private final byte[] expected;

    public BufferedChannelReadTest(int pos, int destLen, byte[] expected) {
        this.pos = pos;
        this.destLen = destLen;
        this.expected = expected;
    }

    @BeforeClass
    public static void setUp() throws Exception {
        file = File.createTempFile("test", null);
        fileChannel = mock(FileChannel.class);
        when(fileChannel.size()).thenReturn((long) 1024);

        byte[] data = new byte[1024];
        Arrays.fill(data, (byte) 42);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        when(fileChannel.read(buffer)).thenReturn(1024);
        when(fileChannel.position()).thenReturn((long) 0);
        fileChannel.force(true);
        fileChannel.position(0);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        Files.deleteIfExists(file.toPath());
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { 
            { 0, 100, new byte[100] }, // read the first 100 bytes
            { 1024, 0, new byte[0] }, // read after the end of file, expect empty buffer
            { -1, 1, new byte[1] },
            { -1, -1, new byte[0] },
            
            
            
        });
    }

    @Test
    public void testRead() throws Exception {

    	try {
            ByteBuffer dest = ByteBuffer.allocate(destLen);

        } catch (IllegalArgumentException e ) {
            if (pos < 0 || destLen < 0) {
                // If pos is negative, then it's okay to throw IllegalArgumentException
                return;
            } else {
                // If pos is positive, then we shouldn't be throwing IllegalArgumentException
                fail("Unexpected IllegalArgumentException thrown");
            } }

        ByteBuffer dest = ByteBuffer.allocate(destLen);
        ZeroBuffer.put(dest);
        when(fileChannel.read(dest, pos)).thenReturn(destLen);
        BufferedChannel channel = new BufferedChannel(fileChannel, 1024);
        channel.read(dest, pos);
        byte[] actual = new byte[destLen];
        dest.flip();
        dest.get(actual);
        assertEquals("Mismatch in position " + pos, Arrays.toString(expected), Arrays.toString(actual));
    }

    

    
    
    
    
    
    

    


}
    
    


    

    





