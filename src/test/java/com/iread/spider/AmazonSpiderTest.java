package com.iread.spider;

import com.iread.bean.Category;
import com.iread.conf.ConfMan;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by liu on 16/9/19.
 */
@Ignore
public class AmazonSpiderTest {
    @BeforeClass
    public static void init() {
        PropertyConfigurator.configure(ConfMan.DEFAULT_CONF_PATH);
    }

    @Test
    public void testFetchSpider() {
        AmazonSpider amazonSpider = new AmazonSpider();
        ArrayList<Category> categories = amazonSpider.fetchCategorys();
        for (Category cat : categories) {
            System.out.println(cat.toString());
        }
        Assert.assertTrue(categories.size() > 20);
    }

    @Test
    public void testParseLeftCategorySections() {
        AmazonSpider amazonSpider = new AmazonSpider();
//        String path = "/s/ref=lp_658390051_nr_n_0/452-9332480-1010121?fst=as%3Aoff&amp;rh=n%3A658390051%2Cn%3A%21658391051%2Cn%3A658393051&amp;bbn=658391051&amp;ie=UTF8&amp;qid=1474391610&amp;rnid=658391051";
//        String url = AmazonSpider.HOST + path;
        String url = "https://www.amazon.cn/s/ref=lp_658390051_nr_n_0?fst=as%3Aoff&rh=n%3A658390051%2Cn%3A%21658391051%2Cn%3A658393051&bbn=658391051&ie=UTF8&qid=1474107806&rnid=658391051";
        Document document = Spider.fetchDocument(url);
//        System.out.println(document.html());
        ArrayList<Category> categories = amazonSpider.parseLeftCategorySections(document, "小说", 2);
        Assert.assertTrue(categories.size() > 5);
    }
}
