package com.iread.parser;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by liu on 16/10/5.
 */
public class AmazonKindleParser extends AmazonBookParser {
    public static String getBookDesc(Document document) {
        String bookDescEnc = StringUtils.substringBetween(document.toString(), "bookDescEncodedData = \"", "\",");
        if (bookDescEnc == null) {
            return "";
        }
        String bookDesc = null;
        try {
            bookDesc = URLDecoder.decode(bookDescEnc, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        bookDesc = StringEscapeUtils.unescapeHtml(bookDesc);
        return bookDesc;
    }

    public static ArrayList<String> getImgUrls(Document document) {
        ArrayList<String> imgs = new ArrayList<String>();
        Element imgEl = document.getElementById("ebooksImgBlkFront");
        imgs.add(imgEl.attr("src"));
        return imgs;
    }

}
