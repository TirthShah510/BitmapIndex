package tpmms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.la2.Configuration;

public class PhaseOne {

	public static String mergeInputFiles(String fileName1, String fileName2) {
		try {
			System.out.println("\n===============Merging Input Files=================\n");
			long startTime = System.currentTimeMillis();
			File input1 = new File(fileName1);
			File input2 = new File(fileName2);
			File mergedFile = new File(Configuration.FILE_PATH + File.separator + Configuration.MERGED_FILE
					+ Configuration.FILE_EXTENSION);
			mergedFile.createNewFile();
			BufferedReader bufferedReader = null;
			FileWriter fileWriter = null;
			bufferedReader = new BufferedReader(new FileReader(input1));
			fileWriter = new FileWriter(mergedFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				bufferedWriter.write(line);
				bufferedWriter.newLine();
			}
			bufferedReader.close();
			bufferedReader = new BufferedReader(new FileReader(input2));
			while ((line = bufferedReader.readLine()) != null) {
				bufferedWriter.write(line);
				bufferedWriter.newLine();
			}
			bufferedReader.close();
			bufferedWriter.close();
			System.out.println("Time to merge Sorted Files: " + (System.currentTimeMillis() - startTime) + " Ms.");
			System.gc();
			return mergedFile.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}