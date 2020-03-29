package org.la2;

import java.io.*;
import java.util.LinkedList;

public class DatasetCompressor {
    public static String createCompressedDataset() throws IOException {
        long startTime = System.currentTimeMillis();

        System.out.println("Creating Compressed Dataset");

        String compressedDatasetFileName = "compressed_" + Configuration.INPUT_FILE_NAME;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(Configuration.FILE_PATH, Configuration.INPUT_FILE_NAME)));
        FileWriter fw = new FileWriter(Configuration.FILE_PATH + compressedDatasetFileName);

        boolean readingCompleted = false;

        short tupleSizeInBytes = 101;
        int chunkSize = IndexByBitSet.numberOfTuplesPossibleToProcessAtOnce(tupleSizeInBytes, tupleSizeInBytes * 2);
        System.out.println("\nCan process " + chunkSize + " records at a time");

        short reads = 0;
        short writes = 0;


        while (!readingCompleted) {
            LinkedList<String> compressedRecords = new LinkedList<>();
            int i = 0;

            // +1 reading
            reads++;
            for (; i < chunkSize; i++) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    readingCompleted = true;
                    break;
                }
                String empIdAndDate = line.substring(0, 18);
                char gender = line.charAt(43);
                String deptNumber = line.substring(44, 47);
                String compressedRecord = empIdAndDate + gender + deptNumber;
                compressedRecords.add(compressedRecord);
            }

            // +1 writing
            writes++;
            while (!compressedRecords.isEmpty()) {
                fw.write(compressedRecords.removeFirst() + "\n");
            }
        }
        bufferedReader.close();
        bufferedReader = null;
        fw.flush();
        fw.close();

        System.out.println("\nOverall Stats:");
        System.out.println("\tTime Elapsed: " + (System.currentTimeMillis() - startTime) + " Ms.");
        System.out.println("\tReads=" + reads + "\n\tWrites=" + writes);
        System.out.println("\n=====================================================================================\n");

        System.gc();

        return compressedDatasetFileName;
    }

    public static int getCompressedTupleSize() {
        return 23;
    }

    public static String getEmployeeIdFromCompressedRecord(String compressedRecord) {
        return compressedRecord.substring(0, 8);
    }

    public static String getDateFromCompressedRecord(String compressedRecord) {
        return compressedRecord.substring(8, 18);
    }

    public static char getGenderFromCompressedRecord(String compressedRecord) {
        return compressedRecord.charAt(18);
    }

    public static String getDepartmentNumberFromCompressedRecord(String compressedRecord) {
        return compressedRecord.substring(19, 22);
    }
}
