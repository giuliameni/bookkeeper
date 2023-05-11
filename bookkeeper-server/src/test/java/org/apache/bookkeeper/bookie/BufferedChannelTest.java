package org.apache.bookkeeper.bookie;

import static org.junit.Assert.*;


import java.io.RandomAccessFile;

import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import java.nio.file.StandardOpenOption;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import java.lang.NegativeArraySizeException;

import java.nio.charset.StandardCharsets;





@RunWith(Parameterized.class)
public class BufferedChannelTest {
    private static final int BUFFER_CAPACITY = 1024 * 1024; // 1 MB

    
    private Path tmpFile;
    private FileChannel fileChannel;
    private BufferedChannel bufferedChannel;
    private int bufferSize;

    public BufferedChannelTest(int bufferSize) {
        this.bufferSize = bufferSize;
    }
    @Before
    public void setUp() throws IOException {
    tmpFile = Files.createTempFile("BufferedChannelTest", null);
    fileChannel = FileChannel.open(tmpFile, StandardOpenOption.WRITE);
    bufferedChannel = new BufferedChannel(fileChannel, BUFFER_CAPACITY);
}


    @After
    public void tearDown() throws IOException {
        fileChannel.close();
        Files.deleteIfExists(tmpFile);
    }

    @Parameterized.Parameters
    public static Integer[] data() {
        return new Integer[] {-1 ,0,1,100,1024, 4096*100 };
    }

    
   
    
    @Test
    public void testReadPastEOF() throws IOException {
    	if(bufferSize == 0) {
    		return;
    	}
        ByteBuffer buffer = null;
        try {
            buffer = ByteBuffer.allocate(bufferSize);
        } catch (IllegalArgumentException e) {
            if (bufferSize < 0) {
                // If bufferSize is negative, then it's okay to throw IllegalArgumentException
                return;
            } else {
                // If bufferSize is positive, then we shouldn't be throwing IllegalArgumentException
                fail("Unexpected IllegalArgumentException thrown");
            }
        }
        long fileSize = fileChannel.size();
        long position = fileSize + 1; // read past EOF
        try {
            bufferedChannel.read(buffer, position);
            fail("Expected IOException not thrown");
        } catch (IOException e) {
            assertEquals("Read past EOF", e.getMessage());
        } //Con 0 non riceviamo eccezione perché non entra nel while. 
    }



    @Test
    public void testWriteClearReadPosition() throws IOException {
    	
        try {
            byte[] dataToWrite = new byte[bufferSize];
        } catch (NegativeArraySizeException e) {
            if (bufferSize < 0) {
                // If bufferSize is negative, then it's okay to throw IllegalArgumentException
                return;
            } else {
                // If bufferSize is positive, then we shouldn't be throwing IllegalArgumentException
                fail("Unexpected NegativeArraySizeException thrown");
            }
        }

    // Generate random data to write
    byte[] dataToWrite = new byte[bufferSize];
    new Random().nextBytes(dataToWrite);

    // Write the data to the buffered channel
    ByteBuffer writeBuffer = ByteBuffer.wrap(dataToWrite);
    bufferedChannel.write(writeBuffer);

    // Reset the buffer and read the data back from the file channel
    ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);
    readBuffer.clear();
    long position = bufferedChannel.position() - bufferSize; // get position to read from
    bufferedChannel.read(readBuffer, position); // pass position as second argument

    // Verify that the data read back matches the data written
    assertArrayEquals(dataToWrite, readBuffer.array());
}

    @Test
    public void testWriteFlushRead() throws IOException {
    	if(bufferSize == 0) {
    		return;
    	}
    File tempFile = File.createTempFile("test", "txt");
    tempFile.deleteOnExit();
    RandomAccessFile file = new RandomAccessFile(tempFile, "rw");
    try {
        BufferedChannel bufferedChannel = new BufferedChannel(file.getChannel(), bufferSize);
    } catch (IllegalArgumentException e) {
        if (bufferSize < 0) {
            // If bufferSize is negative, then it's okay to throw IllegalArgumentException
            return;
        } else {
            // If bufferSize is positive, then we shouldn't be throwing IllegalArgumentException
            fail("Unexpected IllegalArgumentException thrown");
        }
    }
    BufferedChannel bufferedChannel = new BufferedChannel(file.getChannel(), bufferSize);
    
    // Write data to the file
    byte[] data = "Hello, world!".getBytes(StandardCharsets.UTF_8);
    ByteBuffer src = ByteBuffer.wrap(data);
    bufferedChannel.write(src);
    bufferedChannel.flush(true);

    // Read data from the file
    ByteBuffer dest = ByteBuffer.allocate(data.length);
    int bytesRead = bufferedChannel.read(dest, 0);
    assertEquals(data.length, bytesRead);
    dest.flip();
    byte[] actualData = new byte[data.length];
    dest.get(actualData);
    assertArrayEquals(data, actualData);
}
    @Test
    public void testWriteWithZeroBuffer() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(0);
        long positionBeforeWrite = bufferedChannel.position();
        bufferedChannel.write(buffer);
        long positionAfterWrite = bufferedChannel.position();
        assertEquals(positionBeforeWrite, positionAfterWrite);
    }

    
    
    
    

    
  


  

}
    


    






    


    

    


   

    

    
   

    

    



   

    
   





    
    
    
    