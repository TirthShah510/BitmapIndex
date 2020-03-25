package org.la2;

import org.apache.commons.lang3.StringUtils;
import org.la2.Configuration;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.stream.Stream;



public class IndexByBitSet {

    public static void main(String args[]) throws IOException, NoSuchFieldException, IllegalAccessException {

        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println(usedMemory);
        System.out.println(Runtime.getRuntime().maxMemory());

        File input = new File(Configuration.FILE_PATH, Configuration.INPUT_FILE_NAME);
        BufferedReader bufferedReader = null;
        bufferedReader = new BufferedReader(new FileReader(input));
        int numOfLines;
        long startTime = System.currentTimeMillis();
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
        HashMap<String, Integer> indexOfEmpid1 = new HashMap<>();
        HashMap<String, Integer> indexOfGender1 = new HashMap<>();
        HashMap<String, Integer> indexOfDept1 = new HashMap<>();

        int indexEmpid=0;
        int indexGender=0;
        int indexDept=0;
        int currentTuple=0;
        while ((line = bufferedReader.readLine()) != null) {

            String gender = ""+line.charAt(43);
            String dept = line.substring(44,47);
            String empID = line.substring(0, 8);
            indexEmpid = getIndexDept(numOfLines, arrayOfBitSetForEmpid, indexOfEmpid, indexEmpid, currentTuple, empID);

            indexGender = getIndexDept(numOfLines, arrayOfBitSetForGender, indexOfGender, indexGender, currentTuple, gender);

            indexDept = getIndexDept(numOfLines, arrayOfBitSetForDept, indexOfDept, indexDept, currentTuple, dept);
            currentTuple++;
        }

        for (BitSet bitset: arrayOfBitSetForEmpid) {
            System.out.println(bitset.get(0)+" "+bitset.get(1)+" "+bitset.get(2)+" "+bitset.get(3)+" "+bitset.get(4));
        }

        for (BitSet bitset: arrayOfBitSetForGender) {
            System.out.println(bitset.get(0)+" "+bitset.get(1)+" "+bitset.get(2)+" "+bitset.get(3)+" "+bitset.get(4));
        }
        for (BitSet bitset: arrayOfBitSetForDept) {
            System.out.println(bitset.get(0)+" "+bitset.get(1)+" "+bitset.get(2)+" "+bitset.get(3)+" "+bitset.get(4));
        }


        /*BitSet bitSet = arrayOfBitSetForEmpid.get(0);
        if(bitSet.get(0) && bitSet.get(1000)){
            System.out.println("true");
        }*/

        /*BitSet bitSet = arrayOfBitSetForDept.get(0);
        if(bitSet.get(0) && bitSet.get(10000) && bitSet.get(20001)){
            System.out.println("true");
        }*/

        System.out.println("Time to create bitmap index: "+(System.currentTimeMillis() - startTime));

        /*Field f1 = ArrayList.class.getDeclaredField("elementData");
        f1.setAccessible(true);
        int capacityEmpid = ((Object[]) f1.get(arrayOfBitSetForEmpid)).length;
        System.out.println("Capacity: "+ capacityEmpid);

        Field f2 = ArrayList.class.getDeclaredField("elementData");
        f2.setAccessible(true);
        int capacityGender = ((Object[]) f2.get(arrayOfBitSetForGender)).length;
        System.out.println("Capacity: "+ capacityGender);

        Field f3 = ArrayList.class.getDeclaredField("elementData");
        f3.setAccessible(true);
        int capacityDept = ((Object[]) f3.get(arrayOfBitSetForDept)).length;
        System.out.println("Capacity: "+ capacityDept);*/


        for (BitSet bitset: arrayOfBitSetForEmpid) {
            int zeroCounter = 0;
            int returnIndex = bitset.nextSetBit(0);
            zeroCounter = returnIndex;
            String compressedBitMap="";
            String compressedBitMap1;
            String compressedBitMap2;

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
            arrayOfBitSetForEmpid1.add(new BitSet(compressedBitMap.length()));
            BitSet bitSet = arrayOfBitSetForEmpid1.get(arrayOfBitSetForEmpid1.size()-1);
            for (int i=0; i<compressedBitMap.length(); i++){
                if(compressedBitMap.charAt(i) == '1'){
                    bitSet.set(i);
                }
            }
            for (BitSet bitset1: arrayOfBitSetForEmpid1) {
                System.out.println(bitset1.get(0)+" "+bitset1.get(1)+" "+bitset1.get(2)+" "+bitset1.get(3)+" "+bitset1.get(4));
            }
            System.out.println(compressedBitMap);
            compressedBitMap = "";

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
            zeroCounter = 0;
        }
        return compressedBitMap;
    }

    private static int getIndexDept(int numOfLines, ArrayList<BitSet> arrayListOfBitsetObjects, HashMap<String, Integer>
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
}
