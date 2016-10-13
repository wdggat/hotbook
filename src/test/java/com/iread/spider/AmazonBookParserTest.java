package com.iread.spider;

import com.iread.parser.AmazonBookParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

/**
 * Created by liu on 16/10/3.
 */
public class AmazonBookParserTest {
    @Test
    public void testParseDate() {
        String dateStr = "2016年9月28日";
        Calendar expected = Calendar.getInstance();
        expected.set(2016, 9, 28, 0, 0, 0);
        expected.set(Calendar.MILLISECOND, 0);
        Calendar actual = AmazonBookParser.parseDate(dateStr);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetAsinFromUrl() {
        String url = "/%E7%A4%BE%E4%BC%9A%E5%BF%83%E7%90%86%E5%AD%A6-%E6%88%B4%E7%BB%B4%C2%B7%E8%BF%88%E5%B0%94%E6%96%AF/dp/B01AJJ6QOK/ref=pd_bxgy_14_img_2?ie=UTF8&psc=1&refRID=YM3YH0H37JCHHKNY5YKZ";
        String expected = "B01AJJ6QOK";
        Assert.assertEquals(expected, AmazonBookParser.getAsinFromUrl(url));
    }
}
