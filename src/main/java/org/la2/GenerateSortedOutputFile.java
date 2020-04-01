package org.la2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class GenerateSortedOutputFile {

    public static String generateOutputFile(String positionForTupleFile, String sortedOutputFileName, int fileNumber) throws IOException {
    	
    	System.out.println("\n====================== Removing Duplicate Tuples ====================\n");
    	long startTime = System.currentTimeMillis();
        File inputFile = new File(Configuration.FILE_PATH + File.separator + positionForTupleFile + fileNumber+Configuration.FILE_EXTENSION);
        File outputFile = new File(Configuration.FILE_PATH + File.separator + sortedOutputFileName +fileNumber+Configuration.FILE_EXTENSION);

        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));

        String line;
        while((line = bufferedReader.readLine()) != null){
            int lineNumber = Integer.parseInt(line.substring(11).trim());
            String lineFromDataset;
            try (Stream<String> lines = Files.lines(Paths.get(Configuration.FILE_PATH + File.separator + Configuration.INPUT_FILE_NAME + fileNumber + Configuration.FILE_EXTENSION))) {
                lineFromDataset = lines.skip(lineNumber-1).findFirst().get();
            }
            bufferedWriter.write(lineFromDataset);
            bufferedWriter.write("\n");
        }
        bufferedWriter.close();
        bufferedReader.close();
        System.out.println("\n Time To Remove Duplicates: "+ (System.currentTimeMillis() - startTime) + " Ms.");
        return outputFile.getAbsolutePath();
    }
}