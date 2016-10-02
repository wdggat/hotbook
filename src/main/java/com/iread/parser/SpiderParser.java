package com.iread.parser;

/**
 * Created by liu on 16/10/2.
 */
public class SpiderParser {
    public static double priceCast(String priceStr) {
        String pStr = priceStr.replace("￥", "");
        return Double.parseDouble(pStr.trim());
    }

    public static String authorClean(String authorText) {
        return authorText.replace("-", "").trim();
    }

}
