package com.iread.parser;

import org.apache.commons.lang.StringUtils;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liu on 16/10/2.
 */
public class SpiderParser {
    public static double priceCast(String priceStr) {
        String pStr = priceStr.replaceAll("[￥,]", "");
        if (pStr.contains("-")) {
            pStr = StringUtils.substringAfter(pStr, "-");
        }
        return Double.parseDouble(pStr.trim());
    }

    public static String authorClean(String authorText) {
        return authorText.replace("-", "").trim();
    }

    public static Calendar parseDate(String dateStr) {
        Calendar date = null;
        Pattern pattern = Pattern.compile("([0-9]+)年([0-9]+)月([0-9]+)");
        Matcher matcher = pattern.matcher(dateStr);
        if(matcher.find()) {
            date = Calendar.getInstance();
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int day = Integer.parseInt(matcher.group(3));
            date.set(year, month, day, 0, 0, 0);
            date.set(Calendar.MILLISECOND, 0);
            return date;
        } else {
            return parseMonth(dateStr);
        }
    }

    public static Calendar parseMonth(String dateStr) {
        Calendar date = null;
        Pattern pattern = Pattern.compile("([0-9]+)年([0-9]+)月");
        Matcher matcher = pattern.matcher(dateStr);
        if(matcher.find()) {
            date = Calendar.getInstance();
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            date.set(year, month, 0, 0, 0, 0);
            date.set(Calendar.MILLISECOND, 0);
        }
        return date;
    }

    public static int parseInt(String text) {
        if (StringUtils.isBlank(text)) {
            return 0;
        }
        return Integer.parseInt(text.replace(",", "").trim());
    }
}
