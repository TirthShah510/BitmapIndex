package org.la2;

import tpmms.TwoPhaseMultiwayMergeSort;

import java.io.*;

public class IndexByBitSet {
    private static final long BUFFER_SPACE_IN_BYTES = 2 * 1000; // 2kb

    public static void main(String args[]) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Total Memory: " + runtime.totalMemory());
        System.out.println("Max Memory: " + Runtime.getRuntime().maxMemory());
        System.out.println("Used Memory: " + usedMemory);

        // Step-1: create compressed dataset
        String compressedDatasetFileName = DatasetCompressor.createCompressedDataset();

        // Step-2: create gender index
        GenderBitmapCreation.createUncompressedIndex(compressedDatasetFileName);

        // Step-3: generate employeeId index
        EmployeeIdBitmapCreation.createUncompressedIndex(compressedDatasetFileName);

        // Step-4: sort compressed-dataset on department --> generate department index
        DepartmentBitmapCreation.createUncompressedIndex(compressedDatasetFileName);

        // Step-5: remove duplicates using indexes
        //  TODO: implement Step-5

        new File(Configuration.FILE_PATH + compressedDatasetFileName).delete(); // delete compressed dataset file
    }

    public static int numberOfTuplesPossibleToProcessAtOnce(long sizeOfEachTupleInBytes, long processingMemoryBytesRequiredForEachTuple) {
        long freeMemoryBytes = Runtime.getRuntime().freeMemory() - BUFFER_SPACE_IN_BYTES;
        return (int) ((freeMemoryBytes - sizeOfEachTupleInBytes) / processingMemoryBytesRequiredForEachTuple);
    }
}
