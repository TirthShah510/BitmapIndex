package org.la2;

import java.io.*;
import java.util.*;

public class GenderBitmapCreation {
    public static void createUncompressedIndex() throws IOException {
        long startTime = System.currentTimeMillis();

        String tempFile = "temp_gender_index.txt";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(Configuration.FILE_PATH, Configuration.INPUT_FILE_NAME)));
        FileWriter fw = new FileWriter(Configuration.FILE_PATH + tempFile);

        boolean readingCompleted = false;

        int chunkSize = IndexByBitSet.numberOfTuplesPossibleToProcessAtOnce(101, 100);
        System.out.println("\nCan process " + chunkSize + " records at a time");

        short reads = 0;
        short writes = 0;

        short chunks = 0;

        while (!readingCompleted) {
            LinkedList<Boolean> gender = new LinkedList<>();
            int i = 0;

            // +1 reading
            reads++;
            for (; i < chunkSize; i++) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    readingCompleted = true;
                    break;
                }
                gender.addLast(line.charAt(43) == '0');
            }

            // +1 writing
            writes++;
            while (!gender.isEmpty()) {
                fw.write(gender.removeFirst() ? "1" : "0");
            }

            chunks++;
        }
        bufferedReader.close();
        bufferedReader = null;
        fw.flush();
        fw.close();

        System.out.println("\ncreated \"" + tempFile + "\" in " + chunks + " chunks");
        System.out.println("\tReads=" + reads + "\n\tWrites=" + writes);
        System.gc();

        chunks = 0;
        FileReader fr = new FileReader(new File(Configuration.FILE_PATH, tempFile));
        fw = new FileWriter(Configuration.FILE_PATH + "gender_index.txt");

        LinkedList<Byte> genderIndex = new LinkedList<>();
        int i = 0;

        System.out.println("\nProcessing \"" + tempFile + "\" file");
        chunkSize = IndexByBitSet.numberOfTuplesPossibleToProcessAtOnce(2, 20);
        System.out.println("\tRound 1: Can process " + chunkSize + " temp-indexes at a time");

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
                fw.write(genderIndex.removeFirst());
            }

            chunks++;
        }

        fr.close();
        fr = new FileReader(new File(Configuration.FILE_PATH, tempFile));
        fw.write("\n");

        System.out.println("\tRound 1: Completed in " + chunks + " chunks");
        System.gc();

        chunks = 0;
        chunkSize = IndexByBitSet.numberOfTuplesPossibleToProcessAtOnce(2, 20);
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

        new File(Configuration.FILE_PATH, tempFile).delete();
        System.gc();
    }
}
