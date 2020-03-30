package org.la2;

import java.io.*;
import java.util.LinkedList;

public class DatasetCompressor {
    public static String createCompressedDataset() throws IOException {
        long startTime = System.currentTimeMillis();

        System.out.println("\n=========================================== Creating Compressed Dataset ==========================================\n");

        String compressedDatasetFileName = "compressed_" + Configuration.INPUT_FILE_NAME;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(Configuration.FILE_PATH, Configuration.INPUT_FILE_NAME)));
        FileWriter fw = new FileWriter(Configuration.FILE_PATH + compressedDatasetFileName);
        FileWriter tempGenderIndexFile = new FileWriter(Configuration.FILE_PATH + getTempGenderIndexFile());

        boolean readingCompleted = false;

        short tupleSizeInBytes = 101;
        int chunkSize = IndexByBitSet.numberOfTuplesPossibleToProcessAtOnce(tupleSizeInBytes, tupleSizeInBytes);
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

            // +2 writing
            while (!compressedRecords.isEmpty()) {
                String compressedRecord = compressedRecords.removeFirst();
                fw.write(compressedRecord + "\n");
                tempGenderIndexFile.write(getGenderFromCompressedRecord(compressedRecord) == '0' ? "1" : "0");
            }
            writes += 2;
        }
        bufferedReader.close();
        bufferedReader = null;
        tempGenderIndexFile.flush();
        tempGenderIndexFile.close();
        fw.flush();
        fw.close();

        System.out.println("\nOverall Stats:");
        System.out.println("\tTime Elapsed: " + (System.currentTimeMillis() - startTime) + " Ms.");
        System.out.println("\tReads=" + reads + "\n\tWrites=" + writes);

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

    public static String getTempGenderIndexFile() {
        return "temp_gender_index.txt";
    }
}
