package org.la2;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
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
        BufferedReader bufferedReader = null;
        bufferedReader = new BufferedReader(new FileReader(input));
        int numOfLines;
        Path p1 = Paths.get(Configuration.FILE_PATH+File.separator+Configuration.INPUT_FILE_NAME);
        try (Stream<String> lines = Files.lines(p1, Charset.defaultCharset())) {
            numOfLines = (int) lines.count();
        }
        String line;
        ArrayList<BitSet> arrayOfBitSetForEmpid = new ArrayList<>();
        ArrayList<BitSet> arrayOfBitSetForGender = new ArrayList<>();
        ArrayList<BitSet> arrayOfBitSetForDept = new ArrayList<>();

        HashMap<String, Integer> indexOfEmpid = new HashMap<>();
        HashMap<String, Integer> indexOfGender = new HashMap<>();
        HashMap<String, Integer> indexOfDept = new HashMap<>();

        ArrayList<BitSet> arrayOfBitSetForEmpid1 = new ArrayList<>();
        ArrayList<BitSet> arrayOfBitSetForGender1 = new ArrayList<>();
        ArrayList<BitSet> arrayOfBitSetForDept1 = new ArrayList<>();

        int indexEmpid=0;
        int indexGender=0;
        int indexDept=0;
        int currentTuple=0;
        while ((line = bufferedReader.readLine()) != null) {

            String gender = ""+line.charAt(43);
            String dept = line.substring(44,47);
            String empID = line.substring(0, 8);
            indexEmpid = createBitmapIndex(numOfLines, arrayOfBitSetForEmpid, indexOfEmpid, indexEmpid, currentTuple, empID);

            indexGender = createBitmapIndex(numOfLines, arrayOfBitSetForGender, indexOfGender, indexGender, currentTuple, gender);

            indexDept = createBitmapIndex(numOfLines, arrayOfBitSetForDept, indexOfDept, indexDept, currentTuple, dept);
            currentTuple++;
        }

        System.out.println("Time to create bitmap index: "+(System.currentTimeMillis() - startTime));

        /*for (BitSet bitset: arrayOfBitSetForEmpid) {
            System.out.println(bitset.get(0)+" "+bitset.get(1)+" "+bitset.get(2)+" "+bitset.get(3)+" "+bitset.get(4));
        }

        for (BitSet bitset: arrayOfBitSetForGender) {
            System.out.println(bitset.get(0)+" "+bitset.get(1)+" "+bitset.get(2)+" "+bitset.get(3)+" "+bitset.get(4));
        }
        for (BitSet bitset: arrayOfBitSetForDept) {
            System.out.println(bitset.get(0)+" "+bitset.get(1)+" "+bitset.get(2)+" "+bitset.get(3)+" "+bitset.get(4));
        }*/

        long startTime1 = System.currentTimeMillis(); // start time for creating compressed bitmap index

        readBitmapIndexObjectToCreateCompressed(arrayOfBitSetForEmpid, arrayOfBitSetForEmpid1);
        readBitmapIndexObjectToCreateCompressed(arrayOfBitSetForGender, arrayOfBitSetForGender1);
        readBitmapIndexObjectToCreateCompressed(arrayOfBitSetForDept, arrayOfBitSetForDept1);

        System.out.println("Time to create compressed bitmap index: "+(System.currentTimeMillis() - startTime1));
    }

    private static int createBitmapIndex(int numOfLines, ArrayList<BitSet> arrayListOfBitsetObjects, HashMap<String, Integer>
            hmapForBitsetObject, int indexForArrayList, int currentTuple, String bitmapKey) {
        if(hmapForBitsetObject.containsKey(bitmapKey)){
            int key = hmapForBitsetObject.get(bitmapKey);
            BitSet bitSet = arrayListOfBitsetObjects.get(key);
            bitSet.set(currentTuple);
        }else{
            arrayListOfBitsetObjects.add(new BitSet(numOfLines));
            BitSet bitSet = arrayListOfBitsetObjects.get(arrayListOfBitsetObjects.size()-1);
            bitSet.set(currentTuple);
            hmapForBitsetObject.put(bitmapKey, indexForArrayList);
            indexForArrayList++;
        }
        return indexForArrayList;
    }

    private static void readBitmapIndexObjectToCreateCompressed(ArrayList<BitSet> arrayOfBitSetForIndexKey,
                                                                ArrayList<BitSet> arrayOfBitSetForCompressedIndexKey) {
        for (BitSet bitset: arrayOfBitSetForIndexKey) {
            int zeroCounter = 0;
            int returnIndex = bitset.nextSetBit(0);
            zeroCounter = returnIndex;
            String compressedBitMap="";

            compressedBitMap = createCompressedBitmap(zeroCounter, compressedBitMap);

            int lengthCounter = 0;
            while(returnIndex >=0 && lengthCounter<=(bitset.length()-2)){
                int previousSetBitIndex = returnIndex;
                returnIndex = bitset.nextSetBit(returnIndex+1);
                if(returnIndex >=0) {
                    zeroCounter = returnIndex - previousSetBitIndex - 1;
                    compressedBitMap = createCompressedBitmap(zeroCounter, compressedBitMap);
                }
                lengthCounter++;

            }
            arrayOfBitSetForCompressedIndexKey.add(new BitSet(compressedBitMap.length()));
            BitSet bitSet = arrayOfBitSetForCompressedIndexKey.get(arrayOfBitSetForCompressedIndexKey.size()-1);
            for (int i=0; i<compressedBitMap.length(); i++){
                if(compressedBitMap.charAt(i) == '1'){
                    bitSet.set(i);
                }
            }
            /*for (BitSet bitset1: arrayOfBitSetForCompressedIndexKey) {
                System.out.println(bitset1.get(0)+" "+bitset1.get(1)+" "+bitset1.get(2)+" "+bitset1.get(3)+" "+bitset1.get(4));
            }
            System.out.println(compressedBitMap);*/

        }
    }

    private static String createCompressedBitmap(int zeroCounter, String compressedBitMap) {
        String compressedBitMap1;
        String compressedBitMap2;
        if(zeroCounter==1){
            compressedBitMap += "01";
        }else if(zeroCounter == 0){
            compressedBitMap += "00";
        }else{
            compressedBitMap1 = Integer.toBinaryString(zeroCounter);
            int j = (int) Math.ceil(Math.log(zeroCounter) / Math.log(2));
            compressedBitMap2 = StringUtils.leftPad("0", j, "1");
            compressedBitMap += compressedBitMap2 + compressedBitMap1;
        }
        return compressedBitMap;
    }

}
