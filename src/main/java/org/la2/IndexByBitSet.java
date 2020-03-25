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



public class IndexByBitSet {

    public static void main(String args[]) throws IOException, NoSuchFieldException, IllegalAccessException {

        /*System.gc();
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println(usedMemory);
        System.out.println(Runtime.getRuntime().maxMemory());*/

        BitmapCreation.callCreateBitmapIndexMethod();

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


}
