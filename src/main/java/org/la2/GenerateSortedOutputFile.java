package org.la2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class GenerateSortedOutputFile {

    public static void generateOutputFile() throws IOException {
        
        File inputFile = new File(Configuration.FILE_PATH, Configuration.POSITION_FILE_FOR_TUPLE);
        File outputFile = new File(Configuration.FILE_PATH + File.separator + Configuration.OUTPUT_FILE_NAME);

        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));

        String line;
        while((line = bufferedReader.readLine()) != null){
            int lineNumber = Integer.parseInt(line.substring(11).trim());
            System.out.println(lineNumber);
            String lineFromDataset;
            try (Stream<String> lines = Files.lines(Paths.get(Configuration.FILE_PATH + File.separator + Configuration.INPUT_FILE_NAME))) {
                lineFromDataset = lines.skip(lineNumber-1).findFirst().get();
            }
            bufferedWriter.write(lineFromDataset);
            bufferedWriter.write("\n");
        }
        bufferedWriter.close();
        bufferedReader.close();
    }
}
