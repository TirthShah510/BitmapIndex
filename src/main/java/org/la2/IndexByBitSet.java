package org.la2;

import java.io.*;

public class IndexByBitSet {
    private static final long BUFFER_SPACE_IN_BYTES = 2 * 1000; // 2kb

    public static void main(String args[]) throws IOException {
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Total Memory: " + runtime.totalMemory());
        System.out.println("Max Memory: " + Runtime.getRuntime().maxMemory());
        System.out.println("Used Memory: " + usedMemory);
        System.out.println("\n=====================================================================================\n");
        //BitmapCreation.callCreateBitmapIndexMethod();
        String compressedDatasetFileName = DatasetCompressor.createCompressedDataset();
        GenderBitmapCreation.createUncompressedIndex(compressedDatasetFileName);
    }

    public static int numberOfTuplesPossibleToProcessAtOnce(long sizeOfEachTupleInBytes, long processingMemoryBytesRequiredForEachTuple) {
        long freeMemoryBytes = Runtime.getRuntime().freeMemory() - BUFFER_SPACE_IN_BYTES;
        return (int) (freeMemoryBytes / sizeOfEachTupleInBytes / processingMemoryBytesRequiredForEachTuple);
    }
}
