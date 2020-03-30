package org.la2;

import java.io.*;
import java.util.*;

public class GenderBitmapCreation {
    public static String createUncompressedIndex(String compressedDatasetFileName) throws IOException {
        System.out.println("\n=========================================== Creating Gender Index ==========================================\n");

        long startTime = System.currentTimeMillis();

        short reads = 0;
        short writes = 0;
        short chunks = 0;
        String genderIndexFileName = "gender_index.txt";
        FileReader fr = new FileReader(new File(Configuration.FILE_PATH, DatasetCompressor.getTempGenderIndexFile()));
        FileWriter fw = new FileWriter(Configuration.FILE_PATH + genderIndexFileName);

        LinkedList<Byte> genderIndex = new LinkedList<>();
        int i = 0;

        System.out.println("Processing \"" + DatasetCompressor.getTempGenderIndexFile() + "\" file");
        int chunkSize = IndexByBitSet.numberOfTuplesPossibleToProcessAtOnce(1, 1 * 30);
        System.out.println("\tRound 1: Can process " + chunkSize + " temp-indexes at a time");

        boolean readingCompleted = false;
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
                fw.write(genderIndex.removeFirst());
            }

            chunks++;
        }

        fr.close();
        fr = new FileReader(new File(Configuration.FILE_PATH, DatasetCompressor.getTempGenderIndexFile()));
        fw.write("\n");

        System.out.println("\tRound 1: Completed in " + chunks + " chunks");
        System.gc();

        chunks = 0;
        chunkSize = IndexByBitSet.numberOfTuplesPossibleToProcessAtOnce(1, 1 * 30);
        System.out.println("\tRound 2: Can process " + chunkSize + " temp-indexes at a time");

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
                fw.write(genderIndex.removeFirst() == (byte) '1' ? '0' : '1');
            }

            chunks++;
        }
        fr.close();
        fw.flush();
        fw.close();
        fr = null;
        fw = null;
        genderIndex = null;

        System.out.println("\tRound 2: Completed in " + chunks + " chunks");

        System.out.println("\nOverall Stats:");
        System.out.println("\tTime Elapsed: " + (System.currentTimeMillis() - startTime) + " Ms.");
        System.out.println("\tReads=" + reads + "\n\tWrites=" + writes);

        new File(Configuration.FILE_PATH, DatasetCompressor.getTempGenderIndexFile()).delete();
        System.gc();

        return genderIndexFileName;
    }
}
