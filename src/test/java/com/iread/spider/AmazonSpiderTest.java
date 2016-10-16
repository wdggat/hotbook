package com.iread.spider;

import com.iread.bean.Book;
import com.iread.bean.BookPreview;
import com.iread.bean.Category;
import com.iread.bean.Species;
import com.iread.conf.ConfMan;
import com.iread.util.CategoryHelper;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by liu on 16/9/19.
 */
@Ignore
public class AmazonSpiderTest {
    private static ConfMan conf;
    private static AmazonSpider amazonSpider;
    @BeforeClass
    public static void init() {
        PropertyConfigurator.configure(ConfMan.DEFAULT_CONF_PATH);
        CategoryHelper.init(Species.AMAZON);
        conf = new ConfMan();
        amazonSpider = new AmazonSpider(conf);
    }

    @Test
    public void testFetchSpider() {
        ArrayList<Category> categories = amazonSpider.fetchCategorys();
        for (Category cat : categories) {
            System.out.println(cat.toString());
        }
        Assert.assertTrue(categories.size() > 20);
    }

    @Test
    public void testParseLeftCategorySections() {
//        String path = "/s/ref=lp_658390051_nr_n_0/452-9332480-1010121?fst=as%3Aoff&amp;rh=n%3A658390051%2Cn%3A%21658391051%2Cn%3A658393051&amp;bbn=658391051&amp;ie=UTF8&amp;qid=1474391610&amp;rnid=658391051";
//        String url = AmazonSpider.HOST + path;
        String url = "https://www.amazon.cn/s/ref=lp_658390051_nr_n_0?fst=as%3Aoff&rh=n%3A658390051%2Cn%3A%21658391051%2Cn%3A658393051&bbn=658391051&ie=UTF8&qid=1474107806&rnid=658391051";
        Document document = Spider.fetchDocument(url);
//        System.out.println(document.html());
        ArrayList<Category> categories = amazonSpider.parseLeftCategorySections(document, "小说", 2);
        Assert.assertTrue(categories.size() > 5);
    }

    @Test
    public void testFetchBookPreviews_category() {
        String path = "https://www.amazon.cn/s/ref=lp_658495051_il_ti_stripbooks?rh=n%3A658390051%2Cn%3A%21658391051%2Cn%3A658393051%2Cn%3A658495051&ie=UTF8&qid=1474476399&lo=stripbooks";
        String url = path;
        Category category = new Category(Species.AMAZON, Category.TYPE_NORMAL, "推理小说", "小说", url, 32422, 2);
        ArrayList<BookPreview> previews = amazonSpider.fetchBookPreviews(category);
        Assert.assertTrue(previews.size() > 5);
    }

    @Test
    public void testFetchBookPreviews_url()  throws IOException {
        String url = "https://www.amazon.cn/s/ref=lp_658495051_il_ti_stripbooks?rh=n%3A658390051%2Cn%3A%21658391051%2Cn%3A658393051%2Cn%3A658495051&ie=UTF8&qid=1474476399&lo=stripbooks";
        int orderBegin = (15 - 1) * conf.getAmazonBooknumPerSquareSortPage();
        Category category = new Category();
        category.setUrl(url);
        ArrayList<BookPreview> previews = amazonSpider.fetchBookPreviews(category, 15);
        for(BookPreview bookPreview : previews) {
            System.out.println(bookPreview.toString());
        }
        Assert.assertEquals(conf.getAmazonBooknumPerSquareSortPage(), previews.size());
    }

    @Test
    public void testFetchBook_paperback() throws IOException {
        String url = "https://www.amazon.cn/%E9%AD%94%E9%AC%BC%E7%BB%8F%E6%B5%8E%E5%AD%A6%E7%B3%BB%E5%88%97-%E5%8F%B2%E8%92%82%E8%8A%AC%C2%B7%E5%88%97%E7%BB%B4%E7%89%B9/dp/B01KV0D9OW/ref=sr_1_3?s=books&ie=UTF8&qid=1474475789&sr=1-3";
        BookPreview bookPreview = new BookPreview(Species.AMAZON, 263, "ASIN_test", "TITLE_test_魔鬼经济学", "2016年4月", null, url, null, 10000f, 10f, 100);
        Book book = amazonSpider.fetchBook(bookPreview);
        System.out.println(book.toString());
        Assert.assertNotNull(book);
        Assert.assertTrue(book.getAsin().length() > 5);
        Assert.assertTrue(book.getDescription().length() > 5);
        Assert.assertTrue(book.getCatalog().length() > 5);
    }

    @Test
    public void testFetchBook_paperback_noComment() throws IOException {
        String url = "https://www.amazon.cn/%E6%BA%90%E6%B0%8F%E7%89%A9%E8%AF%AD-%E7%B4%AB%E5%BC%8F%E9%83%A8/dp/B013JFQFF6/ref=lp_2130608051_1_2_twi_har_2/454-1759273-4201302?s=books&ie=UTF8&qid=1476546480&sr=1-2";
        BookPreview bookPreview = new BookPreview(Species.AMAZON, 8198, "ASIN_test", "TITLE_test_源氏物语", "2016年4月", null, url, null, 10000f, 10f, 100);
        Book book = amazonSpider.fetchBook(bookPreview);
        System.out.println(book.toString());
        Assert.assertNotNull(book);
        Assert.assertTrue(book.getAsin().length() > 5);
        Assert.assertTrue(book.getDescription().length() > 5);
        Assert.assertTrue(book.getCatalog().length() > 5);
    }

    @Test
    public void testFetchBook_paperback_alsobuyNoprice() throws IOException {
        String url = "https://www.amazon.cn/%E8%8B%8F%E8%8F%B2%E7%9A%84%E4%B8%96%E7%95%8C-%E4%B9%94%E6%96%AF%E5%9D%A6%E2%80%A2%E8%B4%BE%E5%BE%B7/dp/B0011F5QPC/ref=lp_2130608051_1_4_twi_pap_1/454-1759273-4201302?s=books&ie=UTF8&qid=1476546480&sr=1-4";
        BookPreview bookPreview = new BookPreview(Species.AMAZON, 8198, "ASIN_test", "TITLE_test_苏菲的世界", "2016年4月", null, url, null, 10000f, 10f, 100);
        Book book = amazonSpider.fetchBook(bookPreview);
        System.out.println(book.toString());
        Assert.assertNotNull(book);
        Assert.assertTrue(book.getAsin().length() > 5);
        Assert.assertTrue(book.getDescription().length() > 5);
        Assert.assertTrue(book.getCatalog().length() > 5);
    }

    @Test
    public void testFetchBook_paperback_nobuytogether() throws IOException {
        String url = "https://www.amazon.cn/Fathers-and-Children-Turgenev-Ivan/dp/0679405364/ref=lp_2130608051_1_41_twi_har_2/454-1759273-4201302?s=books&ie=UTF8&qid=1476546480&sr=1-41";
        BookPreview bookPreview = new BookPreview(Species.AMAZON, 8198, "ASIN_test", "TITLE_test_父与子", "2016年4月", null, url, null, 10000f, 10f, 100);
        Book book = amazonSpider.fetchBook(bookPreview);
        System.out.println(book.toString());
        Assert.assertNotNull(book);
        Assert.assertTrue(book.getAsin().length() > 5);
    }

    @Test
    public void testFetchBook_kindle() throws IOException {
        String url = "https://www.amazon.cn/dp/B0151E1R06/ref=tmm_kin_swatch_0?_encoding=UTF8&qid=&sr=";
        BookPreview bookPreview = new BookPreview(Species.AMAZON, 263, "ASIN_test", "TITLE_test_超越智商", "2016年4月", url, null, null, 10000f, 10f, 100);
        Book book = amazonSpider.fetchBook(bookPreview);
        System.out.println(book.toString());
        Assert.assertNotNull(book);
        Assert.assertTrue(book.getDescription().length() > 5);
    }
}
