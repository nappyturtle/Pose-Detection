package com.pdst.pdstserver.handlers;

public class SearchUtil {
    public static double searchMatchingPercentage(String[] tokens, String data) {
        int count = 0;
        double total = tokens.length;
        String[] name = data.split(" ");
        double dataLength = name.length;
        if(total / dataLength < 0.5 || total / dataLength > 2) return 0;
        for(int i = 0; i < total; i++) {
            if(data.toLowerCase().contains(tokens[i])) count++;
        }
//        System.out.println("Matching: " + count/dataLength);
        return count/dataLength;
    }
}
