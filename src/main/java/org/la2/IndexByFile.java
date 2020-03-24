package org.la2;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.Scanner;
import java.util.stream.Stream;

public class IndexByFile {

    public static void main(String args[]) throws IOException {
        File input = new File(Configuration.FILE_PATH, Configuration.INPUT_FILE_NAME);
        RandomAccessFile file = new RandomAccessFile(Configuration.FILE_PATH + Configuration.INPUT_FILE_NAME, "rw");
        file.seek(2);
        file.write( "Hello World".getBytes("UTF-8"));
        file.close();
        /*BufferedReader bufferedReader = null;
        bufferedReader = new BufferedReader(new FileReader(input));
        String line;
        String line1;
        int counter = 0;

        int numOfLines;
        Path p1 = Paths.get(Configuration.FILE_PATH+File.separator+Configuration.INPUT_FILE_NAME);
        try (Stream<String> lines = Files.lines(p1, Charset.defaultCharset())) {
            numOfLines = (int) lines.count();
        }
        File output = new File(Configuration.FILE_PATH, Configuration.OUTPUT_FILE_NAME);
        output.createNewFile();
        BufferedReader bufferedReader1 = null;
        bufferedReader1 = new BufferedReader(new FileReader(output));
        BufferedWriter bufferedWriter1 = null;
        bufferedWriter1 = new BufferedWriter(new FileWriter(output));

        while ((line = bufferedReader.readLine()) != null) {
            String empID = line.substring(0, 8);
            while ((line1 = bufferedReader1.readLine()) != null){
                if(line1.substring(0,8).equals(empID)){
                    String outEmpIDBitMap = line1.substring(10);
                    StringBuilder empBitMapVal = new StringBuilder(outEmpIDBitMap);
                    empBitMapVal.setCharAt(counter, '1');

                }
            }
            counter++;
        }*/
    }
}
