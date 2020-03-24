package org.la2;

import org.apache.commons.lang3.StringUtils;

public class CompressedBitMap {

    public static void main(String args[]){

        int zeroCounter = 0;
        String compressedBitMap1;
        String compressedBitMap2;
        String compressedBitMap = "";
        String bitMap = "100000001000";
        for(int i=0; i<bitMap.length(); i++){
            if(bitMap.charAt(i) == '0'){
                zeroCounter++;
            }else{
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
            }
        }
        System.out.println(compressedBitMap);
    }
}
