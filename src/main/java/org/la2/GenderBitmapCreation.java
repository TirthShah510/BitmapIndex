package org.la2;

import java.io.*;
import java.util.*;
import org.la2.CompressedBitMap;

public class GenderBitmapCreation {
    public static String createUncompressedAndCompressedIndex(String genderIndexFileName, int fileNumber) throws IOException {
        System.out.println("\n====================== Creating Gender BitMap Index & Compressed Index For File Number: "+ fileNumber +" ====================\n");
        long startTime = System.currentTimeMillis();

        short reads = 0;
        short writes = 0;
        short chunks = 0;
        
        FileReader fr = new FileReader(new File(Configuration.FILE_PATH, DatasetCompressor.getTempGenderIndexFile()));
        FileWriter fw = new FileWriter(Configuration.FILE_PATH + genderIndexFileName+fileNumber+Configuration.FILE_EXTENSION);

        LinkedList<Byte> genderIndex = new LinkedList<>();
        int i = 0;

        System.out.println("Processing \"" + DatasetCompressor.getTempGenderIndexFile() + "\" file");
        int chunkSize = IndexByBitSet.numberOfTuplesPossibleToProcessAtOnce(1, 1 * 30);
        System.out.println("\tRound 1: Can process " + chunkSize + " temp-indexes at a time");

        boolean readingCompleted = false;
        BitSet bitset = new BitSet();
        int bitsetSetIndexCounter = 0;
        while (!readingCompleted) {
            i = 0;

            // +1 reading
            reads++;
            for (; i < chunkSize; i++) {
                byte gender = (byte) fr.read();
                if (gender == -1) {
                    readingCompleted = true;
                    break;
                }
                genderIndex.addLast(gender);
            }

            // +1 writing
            writes++;
            while (!genderIndex.isEmpty()) {
            	Byte bitmapValue = genderIndex.removeFirst();
                fw.write(bitmapValue);
                if(bitmapValue == (byte) '1') {
                	bitset.set(bitsetSetIndexCounter);
                }
                bitsetSetIndexCounter++;
            }
            
            chunks++;
        }
        // +1 write to write compressed bitmap
        writes++;
        CompressedBitMap.readBitSetToCreateCompressedBitSetAndWriteToFile("0", bitset, Configuration.GENDER_COMPRESSED_BITMAP_FILE_NAME + fileNumber + Configuration.FILE_EXTENSION);
        fr.close();
        fr = new FileReader(new File(Configuration.FILE_PATH, DatasetCompressor.getTempGenderIndexFile()));
        fw.write("\n");

        System.out.println("\tRound 1: Completed in " + chunks + " chunks");
        System.gc();

        chunks = 0;
        chunkSize = IndexByBitSet.numberOfTuplesPossibleToProcessAtOnce(1, 1 * 30);
        System.out.println("\tRound 2: Can process " + chunkSize + " temp-indexes at a time");

        bitset.clear();
        bitset=null;
        bitset = new BitSet();
        bitsetSetIndexCounter = 0;
        readingCompleted = false;
        while (!readingCompleted) {
            i = 0;

            // +1 reading
            reads++;
            for (; i < chunkSize; i++) {
                byte gender = (byte) fr.read();
                if (gender == -1) {
                    readingCompleted = true;
                    break;
                }
                genderIndex.addLast(gender);
            }

            // +1 writing
            writes++;
            while (!genderIndex.isEmpty()) {
            	Byte bitMapValue = genderIndex.removeFirst();
                fw.write(bitMapValue == (byte) '1' ? '0' : '1');
                if(bitMapValue == (byte) '0') {
                	bitset.set(bitsetSetIndexCounter);
                }
                bitsetSetIndexCounter++;
            }

            chunks++;
        }
        // +1 write to write compressed bitmap
        writes++;
        CompressedBitMap.readBitSetToCreateCompressedBitSetAndWriteToFile("1", bitset, Configuration.GENDER_COMPRESSED_BITMAP_FILE_NAME + fileNumber + Configuration.FILE_EXTENSION);
        fr.close();
        fw.flush();
        fw.close();
        fr = null;
        fw = null;
        genderIndex = null;
        bitset = null;

        System.out.println("\tRound 2: Completed in " + chunks + " chunks");

        System.out.println("\nOverall Stats:");
        System.out.println("\tTime Elapsed: " + (System.currentTimeMillis() - startTime) + " Ms.");
        System.out.println("\tReads=" + reads + "\n\tWrites=" + writes);

        new File(Configuration.FILE_PATH, DatasetCompressor.getTempGenderIndexFile()).delete();
        System.gc();

        return genderIndexFileName + fileNumber + Configuration.FILE_EXTENSION;
    }
}
