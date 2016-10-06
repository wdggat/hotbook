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
}
