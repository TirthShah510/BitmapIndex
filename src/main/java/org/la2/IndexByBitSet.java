package org.la2;
import java.io.*;

public class IndexByBitSet {
    public static void main(String args[]) throws IOException, NoSuchFieldException, IllegalAccessException {
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used Memory: "+usedMemory);
        System.out.println("Total Memory: "+runtime.totalMemory());
        System.out.println("Max Memory: "+ Runtime.getRuntime().maxMemory());
        BitmapCreation.callCreateBitmapIndexMethod();
    }
}
