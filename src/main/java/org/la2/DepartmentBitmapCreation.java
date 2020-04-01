package org.la2;

import tpmms.TwoPhaseMultiwayMergeSort;

import java.io.*;
import java.util.BitSet;
import java.util.LinkedList;

public class DepartmentBitmapCreation {
    public static String createUncompressedAndCompressedIndex(String compressedDatasetFileName) throws IOException {
    	System.out.println("\n====================== Creating Department BitMap Index & Compressed Index ====================\n");

        long startTime = System.currentTimeMillis();


        // sort compressed dataset by department
        TwoPhaseMultiwayMergeSort twoPhaseMultiwayMergeSort = new TwoPhaseMultiwayMergeSort(DatasetCompressor.getDepartmentComparator(), Configuration.DEPARTMENT);
        String sortedFilePath = twoPhaseMultiwayMergeSort.start(Configuration.FILE_PATH + compressedDatasetFileName);
        twoPhaseMultiwayMergeSort = null;

        System.gc();

        String departmentIndexFile = createIndexFile(sortedFilePath);

        System.out.println("\tTime Elapsed: " + (System.currentTimeMillis() - startTime) + " Ms.");

        System.gc();

        return departmentIndexFile;    // TODO: return index file path
    }

    private static String createIndexFile(String sortedFilePath) throws IOException {
        int reads = 0;
        int writes = 0;

        File sortedFile = new File(sortedFilePath);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(sortedFile));

        File outputFile = new File(Configuration.FILE_PATH + File.separator + "department_index.txt");
        outputFile.createNewFile();
        FileWriter outputFileWriter = new FileWriter(outputFile);

        System.out.println("generating \"" + outputFile.getName() + "\"");

        int bitsetStorageRequiredForEachRecord = DatasetCompressor.getNumOfLines() / 8;
        boolean readingCompleted = false;
        int chunkSize = IndexByBitSet.numberOfTuplesPossibleToProcessAtOnce(DatasetCompressor.getCompressedTupleSize(), (DatasetCompressor.getCompressedTupleSize() * 2) + bitsetStorageRequiredForEachRecord);
        if (chunkSize == 0) {
            throw new RuntimeException("Memory Full. Cannot read chunks");
        }
        System.out.println("Number of records allowed to read:  " + chunkSize);

        String previousDepartment = "";
        BitSet bitSet = null;
        
        while (!readingCompleted) {
            LinkedList<String> records = new LinkedList<>();

            // +1 read
            reads++;
            for (int i = 0; i < chunkSize; i++) {
                String tuple = bufferedReader.readLine();
                if (tuple == null) {
                    readingCompleted = true;
                    break;
                }
                records.addLast(tuple);
            }
            while (!records.isEmpty()) {
                String tuple = records.removeFirst();
                String department = DatasetCompressor.getDepartmentNumberFromCompressedRecord(tuple);
                if (department.equals(previousDepartment)) {
                    int position = Integer.parseInt(DatasetCompressor.getTuplePosition(tuple));
                    bitSet.set(position - 1);
                } else {
                    if (!previousDepartment.equals("")) {
                        // +2 write to write uncompressed and compressed bitmap
                        writes+=2;
                        outputFileWriter.write(previousDepartment + " > ");
                        for (int i = 0; i < bitSet.length(); i++) {
                            outputFileWriter.write(bitSet.get(i) ? "1" : "0");
                        }
                        outputFileWriter.write("\n");
                        CompressedBitMap.readBitSetToCreateCompressedBitSetAndWriteToFile(previousDepartment, bitSet, Configuration.DEPT_COMPRESSED_BITMAP_FILE_NAME + Configuration.FILE_EXTENSION);
                    }
                    bitSet = new BitSet(DatasetCompressor.getNumOfLines());
                    int position = Integer.parseInt(DatasetCompressor.getTuplePosition(tuple));
                    bitSet.set(position - 1);
                    previousDepartment = department;
                }
            }
        }

        // writing last department record
        if (bitSet != null) {
            // +2 write to write uncompressed and compressed bitmap
            writes+=2;

            outputFileWriter.write(previousDepartment + " > ");
            for (int i = 0; i < bitSet.length(); i++) {
                outputFileWriter.write(bitSet.get(i) ? "1" : "0");
            }
            outputFileWriter.write("\n");
            CompressedBitMap.readBitSetToCreateCompressedBitSetAndWriteToFile(previousDepartment, bitSet, Configuration.DEPT_COMPRESSED_BITMAP_FILE_NAME + Configuration.FILE_EXTENSION);
        }

        outputFileWriter.flush();
        outputFileWriter.close();
        bufferedReader.close();

        System.out.println("\tReads=" + reads + "\n\tWrites=" + writes);

        return outputFile.getAbsolutePath();
    }

}
