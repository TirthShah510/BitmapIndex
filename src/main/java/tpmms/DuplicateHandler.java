package tpmms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.la2.Configuration;
import org.la2.DatasetCompressor;

public class DuplicateHandler {
	public static void removeDuplicateAndWriteOutputFile(String fileNameAndPath) throws Exception {
		System.out.println("\n================= Removing Duplicates =================\n");
		long startTime = System.currentTimeMillis();
		File duplicateFile = new File(fileNameAndPath);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(duplicateFile));
		File outputFile = new File(Configuration.FILE_PATH + File.separator + Configuration.OUTPUT_FILE_NAME
				+ Configuration.FILE_EXTENSION);
		outputFile.createNewFile();
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
		String line = bufferedReader.readLine();
		bufferedWriter.write(line);
		bufferedWriter.newLine();
		String previousLine = line;
		int numberOfTuples = 0;
		while ((line = bufferedReader.readLine()) != null) {
			if (line.length() > 0) {
				if (!DatasetCompressor.getEmployeeIdFromCompressedRecord(line)
						.equals(DatasetCompressor.getEmployeeIdFromCompressedRecord(previousLine))) {
					bufferedWriter.write(line);
					numberOfTuples++;
					bufferedWriter.newLine();
					previousLine = line;
				}
			}
		}
		System.out.println("Total Number of Unique Tuples: " + (numberOfTuples + 1));
		System.out.println(
				"Time to remove duplicates from Merged Files: " + (System.currentTimeMillis() - startTime) + " Ms.");
		bufferedReader.close();
		bufferedWriter.close();
		duplicateFile.deleteOnExit();
		System.gc();
	}
}