package org.la2;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) throws IOException {
//        System.gc();
//        Runtime runtime = Runtime.getRuntime();
//        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
//        System.out.println("Memory "+ usedMemory);
        HashMap<String, String> empIDBitMap = new HashMap<>();
        HashMap<String, String> deptBitMap = new HashMap<>();
        HashMap<Character, String> genderBitMap = new HashMap<>();
        File input = new File(Configuration.FILE_PATH, Configuration.INPUT_FILE_NAME);
        BufferedReader bufferedReader = null;
        bufferedReader = new BufferedReader(new FileReader(input));
        String line;
        int counter = 0;
        long startTime = System.currentTimeMillis();

        int numOfLines;
        Path p1 = Paths.get(Configuration.FILE_PATH+File.separator+Configuration.INPUT_FILE_NAME);
        try (Stream<String> lines = Files.lines(p1, Charset.defaultCharset())) {
            numOfLines = (int) lines.count();
        }

        while ((line = bufferedReader.readLine()) != null) {
//            Character gender = line.charAt(43);
//            String dept = line.substring(44,47);
            String empID = line.substring(0, 8);
            if (!empIDBitMap.containsKey(empID)) {
                empIDBitMap.put(empID, StringUtils.repeat("0", numOfLines));
            }
//            if (!deptBitMap.containsKey(dept)) {
//                deptBitMap.put(dept, StringUtils.repeat("0", numOfLines));
//            }
//            if (!genderBitMap.containsKey(gender)) {
//                genderBitMap.put(gender, StringUtils.repeat("0", numOfLines));
//            }

            StringBuilder empVal = new StringBuilder(empIDBitMap.get(empID));
            empVal.setCharAt(counter, '1');
//            StringBuilder deptVal = new StringBuilder(deptBitMap.get(dept));
//            deptVal.setCharAt(counter, '1');
//            StringBuilder genderVal = new StringBuilder(genderBitMap.get(gender));
//            genderVal.setCharAt(counter, '1');
            empIDBitMap.put(empID, empVal.toString());
//            deptBitMap.put(dept, deptVal.toString());
//            genderBitMap.put(gender, genderVal.toString());

            counter++;
            System.out.println(counter);

        }

        /*for (int key: empIDBitMap.keySet()) {

            if(empIDBitMap.get(key).length() != counter){
                empIDBitMap.put(key, StringUtils.leftPad(empIDBitMap.get(key), counter, '0'));
            }

        }*/
        System.out.println(System.currentTimeMillis() - startTime);
        /*System.out.println(empIDBitMap);
        System.out.println(deptBitMap);
        System.out.println(genderBitMap);*/

    }
}
