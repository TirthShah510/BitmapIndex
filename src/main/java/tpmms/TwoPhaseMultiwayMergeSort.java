package tpmms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import org.la2.Configuration;
import org.la2.DatasetCompressor;
import org.la2.IndexByBitSet;

public class TwoPhaseMultiwayMergeSort {
	private String attributeToSortOn;
	private Comparator<String> comparator;

	public TwoPhaseMultiwayMergeSort(Comparator<String> comparator, String attribute) {
		this.comparator = comparator;
		this.attributeToSortOn = attribute;
	}

	public String start(String compressedDatasetFile, int fileNumber) throws IOException {
		File mergedFile = new File(compressedDatasetFile);
		String sortedFilePath = readFileAndDivideIntoBlocks(mergedFile, fileNumber);
		return sortedFilePath;
	}

	private Comparator<String> getComparator() {
		return comparator;
	}

	private String readFileAndDivideIntoBlocks(File file, int fileNumber) throws IOException {
		System.out.println("Creating Blocks");
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		LinkedList<String> blockFiles = new LinkedList<>();

		short chunkCount = 0;
		boolean readingCompleted = false;
		int chunkSize = IndexByBitSet.numberOfTuplesPossibleToProcessAtOnce(DatasetCompressor.getCompressedTupleSize(),
				DatasetCompressor.getCompressedTupleSize() * 15);
		while (!readingCompleted) {
			LinkedList<String> blockData = new LinkedList<>();
			for (int i = 0; i < chunkSize; i++) {
				String tuple = bufferedReader.readLine();
				if (tuple == null) {
					readingCompleted = true;
					break;
				}
				blockData.add(tuple);
			}
			if (!blockData.isEmpty()) {
				blockData.sort(getComparator());
				String savedBlockFilePath = saveBlockFile(blockData, chunkCount, fileNumber);
				blockFiles.add(savedBlockFilePath);
				chunkCount++;
			}
		}
		bufferedReader.close();

		System.out.println("Total Number Of Blocks: " + chunkCount);
		System.out.println("Total Number Of Disk I/O's for TPMMS: " + (4 * chunkCount));
		return createBufferReaderObjectsOfBlocks(blockFiles, fileNumber);
	}

	private String saveBlockFile(LinkedList<String> blockData, int index, int fileNumber) throws IOException {
		File dir = new File(Configuration.FILE_PATH + File.separator + attributeToSortOn + fileNumber);
		File BLOCK_FILE_PATH = new File(
				Configuration.FILE_PATH + File.separator + attributeToSortOn + fileNumber + File.separator + "chunks");

		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (!BLOCK_FILE_PATH.exists()) {
			BLOCK_FILE_PATH.mkdir();
		}

		String blockFileAbsolutePath = BLOCK_FILE_PATH + File.separator + ("block" + index + ".txt");
		File tempBlock = new File(blockFileAbsolutePath);
		tempBlock.createNewFile();

		FileWriter fileWriter = new FileWriter(tempBlock);
		for (String line : blockData) {
			fileWriter.write(line + "\n");
		}
		fileWriter.flush();
		fileWriter.close();

		return blockFileAbsolutePath;
	}

	private String createBufferReaderObjectsOfBlocks(LinkedList<String> blockFilesPath, int fileNumber)
			throws IOException {
		String outputFilePath = merge(blockFilesPath, fileNumber);
		return outputFilePath;
	}

	private String merge(LinkedList<String> blockFilesPath, int fileNumber) throws IOException {
		System.out.println("Merging the Blocks");
		String outputFilePathAndName = Configuration.FILE_PATH + File.separator + attributeToSortOn + fileNumber
				+ File.separator + "sorted_" + attributeToSortOn + ".txt";
		File outputFile = new File(outputFilePathAndName);

		LinkedList<FileReaderHandler> fileFileReaderHandlers = new LinkedList<>();
		for (String blockFile : blockFilesPath) {
			fileFileReaderHandlers.add(new FileReaderHandler(blockFile));
		}

		PriorityQueue<FileReaderHandler> priorityQueue = new PriorityQueue<>(new Comparator<FileReaderHandler>() {
			@Override
			public int compare(FileReaderHandler o1, FileReaderHandler o2) {
				return getComparator().compare(o1.getCurrentLine(), o2.getCurrentLine());
			}
		});

		for (FileReaderHandler bufferedBlock : fileFileReaderHandlers) {
			if (!bufferedBlock.isEmpty()) {
				priorityQueue.add(bufferedBlock);
			}
		}

		FileWriter fileWriter = new FileWriter(outputFile);
		while (priorityQueue.size() > 0) {
			FileReaderHandler block = priorityQueue.poll();
			fileWriter.write(block.readAndRemoveFromFile() + "\n");
			if (!block.isEmpty()) {
				priorityQueue.add(block);
			}
		}
		fileWriter.flush();
		fileWriter.close();

		for (FileReaderHandler fileReaderHandler : fileFileReaderHandlers) {
			fileReaderHandler.close();
		}
		fileFileReaderHandlers.clear();

		return outputFilePathAndName;
	}
}