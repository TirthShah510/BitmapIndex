package org.la2;

import tpmms.TwoPhaseMultiwayMergeSort;

import java.io.*;
import java.util.BitSet;
import java.util.LinkedList;

public class EmployeeIdBitmapCreation {
    public static String createUncompressedAndCompressedIndex(String compressedDatasetFileName, String employeeIdIndexFileName, int fileNumber) throws IOException {
    	System.out.println("\n====================== Creating EmpId BitMap Index & Compressed Index For File Number: "+ fileNumber +"====================\n");

        long startTime = System.currentTimeMillis();

        
        // sort compressed dataset by employeeId
        TwoPhaseMultiwayMergeSort twoPhaseMultiwayMergeSort = new TwoPhaseMultiwayMergeSort(DatasetCompressor.getEmployeeIdComparator(), Configuration.EMPLOYEE_ID);
        String sortedFilePath = twoPhaseMultiwayMergeSort.start(Configuration.FILE_PATH + compressedDatasetFileName, fileNumber);
        twoPhaseMultiwayMergeSort = null;

        System.gc();

        String employeeIdIndexFile = createIndexFile(sortedFilePath, employeeIdIndexFileName, fileNumber);

//      duplicateTupleRemoval(Configuration.FILE_PATH + File.separator + "employeeId_index.txt");

        System.out.println("\tTime Elapsed: " + (System.currentTimeMillis() - startTime) + " Ms.");

        System.gc();

        return employeeIdIndexFile;
    }

    private static String createIndexFile(String sortedFilePath, String employeeIdIndexFileName, int fileNumber) throws IOException {
        int reads = 0;
        int writes = 0;

        File sortedFile = new File(sortedFilePath);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(sortedFile));

        /*File tempOutputFile = new File(sortedFile.getParentFile().getAbsolutePath(), "temp_index_" + sortedFile.getName());
        tempOutputFile.createNewFile();
        FileWriter fileWriter = new FileWriter(tempOutputFile);*/

        File outputFile = new File(Configuration.FILE_PATH + File.separator + employeeIdIndexFileName + fileNumber + Configuration.FILE_EXTENSION);
        outputFile.createNewFile();
        FileWriter outputFileWriter = new FileWriter(outputFile);

        File positionOfTupleFile = new File(Configuration.FILE_PATH + File.separator + Configuration.POSITION_FILE_FOR_TUPLE + fileNumber + Configuration.FILE_EXTENSION);
        positionOfTupleFile.createNewFile();
        FileWriter fileWriter = new FileWriter(positionOfTupleFile);

        System.out.println("generating \"" + outputFile.getName() + "\"");

        int bitsetStorageRequiredForEachRecord = DatasetCompressor.getNumOfLines() / 8;
        boolean readingCompleted = false;
        int chunkSize = IndexByBitSet.numberOfTuplesPossibleToProcessAtOnce(DatasetCompressor.getCompressedTupleSize(), (DatasetCompressor.getCompressedTupleSize() * 2) + bitsetStorageRequiredForEachRecord);
        if (chunkSize == 0) {
            throw new RuntimeException("Memory Full. Cannot read chunks");
        }
        System.out.println("Number of records allowed to read:  " + chunkSize);

        String previousEmployeeId = "";
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
                String employeeId = DatasetCompressor.getEmployeeIdFromCompressedRecord(tuple);
                if (employeeId.equals(previousEmployeeId)) {
                    int position = Integer.parseInt(DatasetCompressor.getTuplePosition(tuple));
                    bitSet.set(position - 1);
                } else {
                    if (!previousEmployeeId.equals("")) {
                    	// +2 write to write uncompressed and compressed bitmap
                        writes+=2;

                        outputFileWriter.write(previousEmployeeId + " > ");
                        //fileWriter.write(previousEmployeeId + " > ");
                        for (int i = 0; i < bitSet.length(); i++) {
                            outputFileWriter.write(bitSet.get(i) ? "1" : "0");
                            //fileWriter.write("," + i);
                        }
                        outputFileWriter.write("\n");
                        CompressedBitMap.readBitSetToCreateCompressedBitSetAndWriteToFile(previousEmployeeId, bitSet, Configuration.EMPID_COMPRESSED_BITMAP_FILE_NAME + fileNumber + Configuration.FILE_EXTENSION);
                        //fileWriter.write("\n");
                    }
                    bitSet = new BitSet(DatasetCompressor.getNumOfLines());
                    int position = Integer.parseInt(DatasetCompressor.getTuplePosition(tuple));
                    bitSet.set(position - 1);
                    fileWriter.write(employeeId + " > " + position);
                    fileWriter.write("\n");
                    previousEmployeeId = employeeId;
                }
            }
        }

        // writing last employeeId record
        if (bitSet != null) {
        	// +2 write to write uncompressed and compressed bitmap
            writes+=2;

            outputFileWriter.write(previousEmployeeId + " > ");
            //fileWriter.write(previousEmployeeId + " > ");
            for (int i = 0; i < bitSet.length(); i++) {
                outputFileWriter.write(bitSet.get(i) ? "1" : "0");
                //fileWriter.write("," + i);
            }
            outputFileWriter.write("\n");
            CompressedBitMap.readBitSetToCreateCompressedBitSetAndWriteToFile(previousEmployeeId, bitSet, Configuration.EMPID_COMPRESSED_BITMAP_FILE_NAME + fileNumber + Configuration.FILE_EXTENSION);
            //fileWriter.write("\n");
        }

        outputFileWriter.flush();
        outputFileWriter.close();
        fileWriter.flush();
        fileWriter.close();
        bufferedReader.close();

        System.out.println("\tReads=" + reads + "\n\tWrites=" + writes);

        return outputFile.getAbsolutePath();
    }
}
