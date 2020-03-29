package org.la2;

import java.io.*;

public class IndexByBitSet {
    public static void main(String args[]) throws IOException, NoSuchFieldException, IllegalAccessException {
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Total Memory: " + runtime.totalMemory());
        System.out.println("Max Memory: " + Runtime.getRuntime().maxMemory());
        System.out.println("Used Memory: " + usedMemory);
        //BitmapCreation.callCreateBitmapIndexMethod();
        GenderBitmapCreation.createUncompressedIndex();
    }

    public static int numberOfTuplesPossibleToProcessAtOnce(long sizeOfEachTupleInBytes, long processingMemoryBytesRequiredForEachTuple) {
        long bufferBytes = 1000;
        long freeMemoryBytes = Runtime.getRuntime().freeMemory() - bufferBytes;
        return (int) (freeMemoryBytes / sizeOfEachTupleInBytes / processingMemoryBytesRequiredForEachTuple);
    }
}
