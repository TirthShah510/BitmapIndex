package org.la2;

import java.io.File;

import tpmms.DuplicateHandler;
import tpmms.PhaseOne;
import tpmms.TwoPhaseMultiwayMergeSort;

public class IndexByBitSet {
	private static final long BUFFER_SPACE_IN_BYTES = 2 * 1000; // 2kb

	public static void main(String args[]) throws Exception {
		Runtime runtime = Runtime.getRuntime();
		long usedMemory = runtime.totalMemory() - runtime.freeMemory();
		System.out.println("Total Memory: " + runtime.totalMemory());
		System.out.println("Max Memory: " + Runtime.getRuntime().maxMemory());
		System.out.println("Used Memory: " + usedMemory);
		long startTime = System.currentTimeMillis();
		String[] sortedFileNames = new String[2];
		for (int fileNumber = 1; fileNumber <= 2; fileNumber++) {
			// Step-1: create compressed dataset
			String compressedDatasetFileName = DatasetCompressor.createCompressedDataset(Configuration.INPUT_FILE_NAME,
					fileNumber);

			// Step-2: create gender index
			GenderBitmapCreation.createUncompressedAndCompressedIndex(Configuration.GENDER_BITMAP_FILE_NAME,
					fileNumber);

			// Step-3: generate employeeId index
			EmployeeIdBitmapCreation.createUncompressedAndCompressedIndex(compressedDatasetFileName,
					Configuration.EMPID_BITMAP_FILE_NAME, fileNumber);

			// Step-4: sort compressed-dataset on department --> generate department index
			DepartmentBitmapCreation.createUncompressedAndCompressedIndex(compressedDatasetFileName,
					Configuration.DEPT_BITMAP_FILE_NAME, fileNumber);

			// Step-5: remove duplicates using indexes
			// TODO: implement Step-5
			sortedFileNames[fileNumber - 1] = GenerateSortedOutputFile.generateOutputFile(
					Configuration.POSITION_FILE_FOR_TUPLE, Configuration.SORTED_OUTPUT_FILE_NAME, fileNumber);

			new File(Configuration.FILE_PATH + compressedDatasetFileName).delete(); // delete compressed dataset file
		}

		String mergedFilePath = PhaseOne.mergeInputFiles(sortedFileNames[0], sortedFileNames[1]);
		TwoPhaseMultiwayMergeSort twoPhaseMultiwayMergeSort = new TwoPhaseMultiwayMergeSort(
				DatasetCompressor.getEmployeeIdComparator(), Configuration.OUTPUT_FILE_ID);
		String sortedFilePath = twoPhaseMultiwayMergeSort.start(mergedFilePath, 0);
		DuplicateHandler.removeDuplicateAndWriteOutputFile(sortedFilePath);
		System.out.println("\n Time To Complete Entire Process: " + (System.currentTimeMillis() - startTime) + " Ms.");
	}

	public static int numberOfTuplesPossibleToProcessAtOnce(long sizeOfEachTupleInBytes,
			long processingMemoryBytesRequiredForEachTuple) {
		long freeMemoryBytes = Runtime.getRuntime().freeMemory() - BUFFER_SPACE_IN_BYTES;
		return (int) ((freeMemoryBytes - sizeOfEachTupleInBytes) / processingMemoryBytesRequiredForEachTuple);
	}
}
