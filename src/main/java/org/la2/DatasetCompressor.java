package org.la2;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

public class DatasetCompressor {
    private static int numOfLines;

    public static String createCompressedDataset(String inputFileName, int fileNumber) throws IOException {
        long startTime = System.currentTimeMillis();

        System.out.println("\n============================= Creating Compressed Dataset For File Number: "+ fileNumber +" =============================\n");

        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(Configuration.FILE_PATH, inputFileName+fileNumber+Configuration.FILE_EXTENSION)));
        FileWriter fw = new FileWriter(Configuration.FILE_PATH + Configuration.COMPRESSED_DATASET_FILE_NAME +inputFileName +fileNumber+ Configuration.FILE_EXTENSION);
        FileWriter tempGenderIndexFile = new FileWriter(Configuration.FILE_PATH + getTempGenderIndexFile());
        boolean readingCompleted = false;

        short tupleSizeInBytes = 101;
        int chunkSize = IndexByBitSet.numberOfTuplesPossibleToProcessAtOnce(tupleSizeInBytes, tupleSizeInBytes * 2);
        System.out.println("\nCan process " + chunkSize + " records at a time");

        short reads = 0;
        short writes = 0;
        int currentTuple = 1;

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

                String compressedRecord = empIdAndDate + gender + deptNumber + currentTuple;
                compressedRecords.add(compressedRecord);
                currentTuple++;
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

        numOfLines = currentTuple;

        System.out.println("\nOverall Stats:");
        System.out.println("\tTime Elapsed: " + (System.currentTimeMillis() - startTime) + " Ms.");
        System.out.println("\tReads=" + reads + "\n\tWrites=" + writes);

        System.gc();

        return Configuration.COMPRESSED_DATASET_FILE_NAME +inputFileName+ fileNumber + Configuration.FILE_EXTENSION;
    }

    public static int getNumOfLines() {
        return numOfLines;
    }

    public static int getCompressedTupleSize() {
        return 31;
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

    public static String getTuplePosition(String compressedRecord) {
        return compressedRecord.substring(22);
    }

    public static Comparator<String> getEmployeeIdComparator() {
        return new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int idCompare = DatasetCompressor.getEmployeeIdFromCompressedRecord(o1).compareTo(DatasetCompressor.getEmployeeIdFromCompressedRecord(o2));
                if (idCompare != 0) {
                    return idCompare;
                }

                DateFormat format = new SimpleDateFormat("yyyy-mm-dd");

                try {
                    Date date1 = format.parse(DatasetCompressor.getDateFromCompressedRecord(o1));
                    Date date2 = format.parse(DatasetCompressor.getDateFromCompressedRecord(o2));
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public static Comparator<String> getDepartmentComparator() {
        return new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return DatasetCompressor.getDepartmentNumberFromCompressedRecord(o1).compareTo(DatasetCompressor.getDepartmentNumberFromCompressedRecord(o2));
            }
        };
    }
}
