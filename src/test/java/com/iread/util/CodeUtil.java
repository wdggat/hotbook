package com.iread.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by liu on 16/10/5.
 */
public class CodeUtil {
    @Test
    public void test_decodeBookDesc(){
        String encoded = "%26%23x5728%3B%26%23x4fe1%3B%26%23x606f%3B%26%23x7206%3B%26%23x70b8%3B%26%23x7684%3B%26%23x4eca%3B%26%23x5929%3B%26%23xff0c%3B%26%23x6210%3B%26%23x4e3a%3B%26%23x806a%3B%26%23x660e%3B%26%23x4eba%3B%26%23x610f%3B%26%23x5473%3B%26%23x7740%3B%26%23x8981%3B%26%23x9605%3B%26%23x8bfb%3B%26%23x5927%3B%26%23x91cf%3B%26%23x56fe%3B%26%23x4e66%3B%26%23x5e76%3B%26%23x638c%3B%26%23x63e1%3B%26%23x5927%3B%26%23x91cf%3B%26%23x6982%3B%26%23x5ff5%3B%26%23x3002%3B%26%23x800c%3B%26%23x5728%3B21%26%23x4e16%3B%26%23x7eaa%3B%26%23xff0c%3B%26%23x806a%3B%26%23x660e%3B%26%23x4eba%3B%26%23x5fc5%3B%26%23x987b%3B%26%23x638c%3B%26%23x63e1%3B%26%23x7684%3B%26%23x5143%3B%26%23x6982%3B%26%23x5ff5%3B%26%23x662f%3B%26%23x201c%3B%26%23x7406%3B%26%23x6027%3B%26%23x201d%3B%26%23x3002%3B%26%23x5149%3B%26%23x6709%3B%26%23x667a%3B%26%23x529b%3B%26%23x8fd8%3B%26%23x4e0d%3B%26%23x591f%3B%26%23xff0c%3B%26%23x8fd8%3B%26%23x8981%3B%26%23x6709%3B%26%23x7406%3B%26%23x6027%3B%26%23x3002%3B%26%23x8fd9%3B%26%23x662f%3B%26%23x4e16%3B%26%23x754c%3B%26%23x4e0a%3B%26%23x6700%3B%26%23x597d%3B%26%23x7684%3B%26%23x8ba4%3B%26%23x77e5%3B%26%23x79d1%3B%26%23x5b66%3B%26%23x5bb6%3B%26%23x5199%3B%26%23x5f97%3B%26%23x6700%3B%26%23x597d%3B%26%23x7684%3B%26%23x4e00%3B%26%23x672c%3B%26%23x201c%3B%26%23x7406%3B%26%23x6027%3B%26%23x201d%3B%26%23x8457%3B%26%23x4f5c%3B%26%23xff0c%3B%26%23x5b83%3B%26%23x5c06%3B%26%23x5e2e%3B%26%23x52a9%3B%26%23x4f60%3B%26%23x7406%3B%26%23x89e3%3B%26%23x4eba%3B%26%23x7c7b%3B%26%23x7684%3B%26%23x5fc3%3B%26%23x667a%3B%26%23x67b6%3B%26%23x6784%3B%26%23xff0c%3B%26%23x4ece%3B%26%23x9ad8%3B%26%23x667a%3B%26%23x529b%3B%26%23x5230%3B%26%23x9ad8%3B%26%23x7406%3B%26%23x6027%3B%26%23x3002%3B%3Cbr%20%2F%3E";
        String actual = null;
        try {
            actual = URLDecoder.decode(encoded, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        actual = StringEscapeUtils.unescapeHtml(actual);
        System.out.println(actual);
        Assert.assertTrue(actual.length() > 10);
    }
}
