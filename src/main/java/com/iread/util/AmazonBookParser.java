package com.iread.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liu on 16/10/1.
 */
public class AmazonBookParser {
    public static Calendar getDate(Element titleEl) {
        Calendar date = null;
        String dateStr = titleEl.getElementsContainingText("年").last().text();
        Pattern pattern = Pattern.compile("([0-9]+)年([0-9]+)月([0-9]+)");
        Matcher matcher = pattern.matcher(dateStr);
        if(matcher.matches()) {
            date = Calendar.getInstance();
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int day = Integer.parseInt(matcher.group(3));
            date.set(year, month, day, 0, 0, 0);
        }
        return date;
    }

    public static ArrayList<String> getAuthors(Document document, String flag) {
        ArrayList<String> authors = new ArrayList<String>();
        Elements authorEls = document.getElementsByClass("author");
        for(Element authorEl : authorEls) {
            String author = authorEl.select("a").first().text();
            Element contributionEl = authorEl.getElementsByClass("contribution").first();
            if(contributionEl.toString().contains(flag)) {
                authors.add(author);
            }
        }
        return authors;
    }

    public static double getPrice(Document document) {
        Element priceEl = document.getElementsByClass("a-color-price").first();
        String priceStr = priceEl.text().replace("￥", "");
        return Double.parseDouble(priceStr.trim());
    }

    public static String getSeller(Document document) {
        Elements sellerEls = document.getElementById("ddmMerchantMessage").select("a");
        if (!sellerEls.isEmpty()) {
            return sellerEls.first().text();
        }
        return "亚马逊";
    }

    public static String getBookDesc(Document document) {
        Element bookDescEl = document.getElementById("bookDesc_iframe");
        Element contentEl = bookDescEl.getElementById("iframeContent");
        return contentEl.text();
    }
}
