package org.la2;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.BitSet;

import org.apache.commons.lang3.StringUtils;

public class CompressedBitMap {

	public static void readBitSetToCreateCompressedBitSetAndWriteToFile(String key, BitSet bitset, String fileName)
			throws IOException {
		File f = new File(Configuration.FILE_PATH + File.separator + fileName);
		f.createNewFile();
		PrintWriter pw = new PrintWriter(
				Files.newBufferedWriter(Paths.get(Configuration.FILE_PATH + fileName), StandardOpenOption.APPEND));
		pw.write(key + " > ");
		int zeroCounter = 0;
		int returnIndex = bitset.nextSetBit(0);
		zeroCounter = returnIndex;

		String compressedBitMap = createCompressedBitmap(zeroCounter);
		pw.write(compressedBitMap);
		int lengthCounter = 0;
		// long beginTime = System.currentTimeMillis();
		while (returnIndex >= 0 && lengthCounter <= (bitset.length() - 2)) {
			int previousSetBitIndex = returnIndex;
			returnIndex = bitset.nextSetBit(returnIndex + 1);
			if (returnIndex >= 0) {
				zeroCounter = returnIndex - previousSetBitIndex - 1;
				compressedBitMap = createCompressedBitmap(zeroCounter);
				pw.write(compressedBitMap);
			}
			lengthCounter++;
		}
		pw.write("\n");
		pw.close();
		System.gc();
	}

	public static String createCompressedBitmap(int zeroCounter) {
		String compressedBitMap = "";
		String compressedBitMap1;
		String compressedBitMap2;
		if (zeroCounter == 1) {
			compressedBitMap += "01";
		} else if (zeroCounter == 0) {
			compressedBitMap += "00";
		} else {
			compressedBitMap1 = Integer.toBinaryString(zeroCounter);
			int j = (int) Math.ceil(Math.log(zeroCounter) / Math.log(2));
			compressedBitMap2 = StringUtils.leftPad("0", j, "1");
			compressedBitMap = compressedBitMap2 + compressedBitMap1;
		}
		return compressedBitMap;
	}
}