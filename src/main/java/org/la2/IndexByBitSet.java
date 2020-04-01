package org.la2;

import java.io.*;

public class IndexByBitSet {
    private static final long BUFFER_SPACE_IN_BYTES = 2 * 1000; // 2kb

    public static void main(String args[]) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Total Memory: " + runtime.totalMemory());
        System.out.println("Max Memory: " + Runtime.getRuntime().maxMemory());
        System.out.println("Used Memory: " + usedMemory);
        long startTime = System.currentTimeMillis();
        for(int fileNumber=1;fileNumber <= 2; fileNumber++ ) {
        	// Step-1: create compressed dataset
	        String compressedDatasetFileName = DatasetCompressor.createCompressedDataset(Configuration.INPUT_FILE_NAME, fileNumber);
	
	        // Step-2: create gender index
	        GenderBitmapCreation.createUncompressedAndCompressedIndex(Configuration.GENDER_BITMAP_FILE_NAME, fileNumber);
	
	        // Step-3: generate employeeId index
	        EmployeeIdBitmapCreation.createUncompressedAndCompressedIndex(compressedDatasetFileName, Configuration.EMPID_BITMAP_FILE_NAME, fileNumber);
	
	        // Step-4: sort compressed-dataset on department --> generate department index
	        DepartmentBitmapCreation.createUncompressedAndCompressedIndex(compressedDatasetFileName, Configuration.DEPT_BITMAP_FILE_NAME, fileNumber);
	
	        // Step-5: remove duplicates using indexes
	        //  TODO: implement Step-5
	        GenerateSortedOutputFile.generateOutputFile(Configuration.POSITION_FILE_FOR_TUPLE+fileNumber+Configuration.FILE_EXTENSION, Configuration.SORTED_OUTPUT_FILE_NAME+fileNumber+Configuration.FILE_EXTENSION);
	
	        new File(Configuration.FILE_PATH + compressedDatasetFileName).delete(); // delete compressed dataset file
        }
        System.out.println("\n Time To Complete Entire Process: "+ (System.currentTimeMillis() - startTime) + " Ms.");
    }

    public static int numberOfTuplesPossibleToProcessAtOnce(long sizeOfEachTupleInBytes, long processingMemoryBytesRequiredForEachTuple) {
        long freeMemoryBytes = Runtime.getRuntime().freeMemory() - BUFFER_SPACE_IN_BYTES;
        return (int) ((freeMemoryBytes - sizeOfEachTupleInBytes) / processingMemoryBytesRequiredForEachTuple);
    }
}
