package org.la2;

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

        int indexEmpid=0;
        int indexGender=0;
        int indexDept=0;
        int counter=0;
        while ((line = bufferedReader.readLine()) != null) {

            String gender = ""+line.charAt(43);
            String dept = line.substring(44,47);
            String empID = line.substring(0, 8);
            indexEmpid = getIndexDept(numOfLines, arrayOfBitSetForEmpid, indexOfEmpid, indexEmpid, counter, empID);

            indexGender = getIndexDept(numOfLines, arrayOfBitSetForGender, indexOfGender, indexGender, counter, gender);

           /* if(indexOfGender.containsKey(gender)){
                int key = indexOfGender.get(gender);
                BitSet bitSet = arrayOfBitSetForGender.get(key);
                bitSet.set(counter);
            }else{
                arrayOfBitSetForGender.add(new BitSet(numOfLines));
                BitSet bitSet = arrayOfBitSetForGender.get(arrayOfBitSetForGender.size()-1);
                bitSet.set(counter);
                indexOfGender.put(gender, indexGender);
                indexGender++;
            }*/

            indexDept = getIndexDept(numOfLines, arrayOfBitSetForDept, indexOfDept, indexDept, counter, dept);
            /*for (int key : indexOfEmpid.keySet()) {
                if(indexOfEmpid.get(key).equals(empID)){
                    BitSet bitSet = arrayOfBitSetForEmpid.get(key);
                    bitSet.set(counter);
                    newEmpid = true;
                    break;
                }
            }*/
            /*if (!newEmpid){
                arrayOfBitSetForEmpid.add(new BitSet(numOfLines));
                BitSet bitSet = arrayOfBitSetForEmpid.get(arrayOfBitSetForEmpid.size()-1);
                bitSet.set(counter);
                indexOfEmpid.put(index, empID);
                index++;
            }*/
            counter++;
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

        System.out.println(System.currentTimeMillis() - startTime);

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

    }

    private static int getIndexDept(int numOfLines, ArrayList<BitSet> arrayOfBitSetForDept, HashMap<String, Integer> indexOfDept, int indexDept, int counter, String dept) {
        if(indexOfDept.containsKey(dept)){
            int key = indexOfDept.get(dept);
            BitSet bitSet = arrayOfBitSetForDept.get(key);
            bitSet.set(counter);
        }else{
            arrayOfBitSetForDept.add(new BitSet(numOfLines));
            BitSet bitSet = arrayOfBitSetForDept.get(arrayOfBitSetForDept.size()-1);
            bitSet.set(counter);
            indexOfDept.put(dept, indexDept);
            indexDept++;
        }
        return indexDept;
    }
}
