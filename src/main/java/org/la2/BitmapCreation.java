package org.la2;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.stream.Stream;

public class BitmapCreation {

    public static void callCreateBitmapIndexMethod() throws IOException {

        long startTime = System.currentTimeMillis(); // start time for creating uncompressed bitmap index

        File input = new File(Configuration.FILE_PATH, Configuration.INPUT_FILE_NAME);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(input));

        int numOfLines;
        Path p1 = Paths.get(Configuration.FILE_PATH + File.separator + Configuration.INPUT_FILE_NAME);
        try (Stream<String> lines = Files.lines(p1, Charset.defaultCharset())) {
            numOfLines = (int) lines.count();
        }
        String line;

        HashMap<String, BitSet> indexOfEmpid = new HashMap<>();
        HashMap<String, BitSet> indexOfGender = new HashMap<>();
        HashMap<String, BitSet> indexOfDept = new HashMap<>();

        ArrayList<BitSet> arrayOfCompressedBitSetForEmpId = new ArrayList<>();
        ArrayList<BitSet> arrayOfCompressedBitSetForGender = new ArrayList<>();
        ArrayList<BitSet> arrayOfCompressedBitSetForDept = new ArrayList<>();

        int currentTuple = 0;
        while ((line = bufferedReader.readLine()) != null) {
            String gender = "" + line.charAt(43);
            String dept = line.substring(44, 47);
            String empId = line.substring(0, 8);
            createBitmapIndex(numOfLines, indexOfEmpid, currentTuple, empId);
            createBitmapIndex(numOfLines, indexOfGender, currentTuple, gender);
            createBitmapIndex(numOfLines, indexOfDept, currentTuple, dept);
            currentTuple++;
        }
        bufferedReader.close();
        bufferedReader = null;

        System.out.println("Time to create bitmap index: " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis(); // start time for creating compressed bitmap index

        readAllBitmapIndexObjectToCreateCompressed(indexOfEmpid, arrayOfCompressedBitSetForEmpId);
        System.out.println("Employee Id BitMap Created");
        readAllBitmapIndexObjectToCreateCompressed(indexOfGender, arrayOfCompressedBitSetForGender);
        System.out.println("Gender BitMap Created");
        readAllBitmapIndexObjectToCreateCompressed(indexOfDept, arrayOfCompressedBitSetForDept);
        System.out.println("Dept BitMap Created");

        System.out.println("Compressed EmpId BitMap MemoryUsage: ");
        getObjectMemoryUsage(arrayOfCompressedBitSetForEmpId);
        System.out.println("Compressed Gender BitMap MemoryUsage: ");
        getObjectMemoryUsage(arrayOfCompressedBitSetForGender);
        System.out.println("Compressed Dept BitMap MemoryUsage: ");
        getObjectMemoryUsage(arrayOfCompressedBitSetForDept);

        System.out.println("Time to create compressed bitmap index: " + (System.currentTimeMillis() - startTime));
    }

    private static void createBitmapIndex(int numOfLines, HashMap<String, BitSet> hmapForBitsetObject, int currentTuple,
                                          String bitmapKey) {
        BitSet bitSet;
        if (hmapForBitsetObject.containsKey(bitmapKey)) {
            bitSet = hmapForBitsetObject.get(bitmapKey);
        } else {
            bitSet = new BitSet(numOfLines);
            hmapForBitsetObject.put(bitmapKey, bitSet);
        }
        bitSet.set(currentTuple);
    }

    private static void readAllBitmapIndexObjectToCreateCompressed(HashMap<String, BitSet> idBitsetHashmap, ArrayList<BitSet> arrayOfBitSetForCompressedIndexKey) {
        for (BitSet bitset : idBitsetHashmap.values()) {
            readBitSetToCreateCompressed(bitset, arrayOfBitSetForCompressedIndexKey);
        }
    }

    private static void readBitSetToCreateCompressed(BitSet bitset,
                                                     ArrayList<BitSet> arrayOfBitSetForCompressedIndexKey) {
        int zeroCounter = 0;
        int returnIndex = bitset.nextSetBit(0);
        zeroCounter = returnIndex;
        String compressedBitMap = "";

        compressedBitMap = createCompressedBitmap(zeroCounter, compressedBitMap);

        int lengthCounter = 0;
        long beginTime = System.currentTimeMillis();
        while (returnIndex >= 0 && lengthCounter <= (bitset.length() - 2)) {
            int previousSetBitIndex = returnIndex;
            returnIndex = bitset.nextSetBit(returnIndex + 1);
            if (returnIndex >= 0) {
                zeroCounter = returnIndex - previousSetBitIndex - 1;
                compressedBitMap = createCompressedBitmap(zeroCounter, compressedBitMap);
            }
            lengthCounter++;
        }
        System.out.println("Time to create compressed BitMap String: " + (System.currentTimeMillis() - beginTime));
        arrayOfBitSetForCompressedIndexKey.add(new BitSet(compressedBitMap.length()));
        BitSet bitSet = arrayOfBitSetForCompressedIndexKey.get(arrayOfBitSetForCompressedIndexKey.size() - 1);
        for (int i = 0; i < compressedBitMap.length(); i++) {
            if (compressedBitMap.charAt(i) == '1') {
                bitSet.set(i);
            }
        }
    }

    private static String createCompressedBitmap(int zeroCounter, String compressedBitMap) {
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
            compressedBitMap += compressedBitMap2 + compressedBitMap1;
        }
        return compressedBitMap;
    }

    @SuppressWarnings("unused")
    private static void printBitMap(ArrayList<BitSet> bitsets) {
        for (BitSet bitset : bitsets) {
            System.out.println(bitset.get(0) + " " + bitset.get(1) + " " + bitset.get(2) + " " + bitset.get(3) + " " + bitset.get(4));
        }
    }

    private static void getObjectMemoryUsage(ArrayList<BitSet> bitset) {
        Field f1;
        try {
            f1 = ArrayList.class.getDeclaredField("elementData");
            f1.setAccessible(true);
            int capacityEmpid = ((Object[]) f1.get(bitset)).length;
            System.out.println("Capacity: " + capacityEmpid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
