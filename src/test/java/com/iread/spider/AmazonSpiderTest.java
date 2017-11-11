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
    public void testFetchBook_paperback_noSto() throws IOException {
        String url = "https://www.amazon.cn/%E7%BA%B8%E9%95%87-%E7%BA%A6%E7%BF%B0%E2%80%A2%E6%A0%BC%E6%9E%97/dp/B005V7H898/ref=sr_1_793_twi_pap_2?s=books&ie=UTF8&qid=1479055570&sr=1-793";
        BookPreview bookPreview = new BookPreview(Species.AMAZON, 8198, "ASIN_test", "TITLE_test_纸镇", "2016年4月", null, url, null, 10000f, 10f, 100);
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
    public void testFetchBook_paperback_commentnull() throws IOException {
        String url = "https://www.amazon.cn/%E9%A9%AC%E5%85%8B%C2%B7%E5%90%90%E6%B8%A9%E7%9F%AD%E7%AF%87%E5%B0%8F%E8%AF%B4%E7%B2%BE%E9%80%89-%E9%A9%AC%E5%85%8B%C2%B7%E5%90%90%E6%B8%A9/dp/B00K6WPILS/ref=sr_1_180_twi_kin_1?s=books&ie=UTF8&qid=1478104375&sr=1-180";
        BookPreview bookPreview = new BookPreview(Species.AMAZON, 8198, "ASIN_test", "TITLE_test_我是猫", "2016年4月", null, url, null, 10000f, 10f, 100);
        Book book = amazonSpider.fetchBook(bookPreview);
        System.out.println(book.toString());
        Assert.assertNotNull(book);
        Assert.assertTrue(book.getAsin().length() > 5);
    }

    @Test
    public void testFetchBook_paperback_alsobuynull() throws IOException {
        String url = "https://www.amazon.cn/%E5%82%B2%E6%85%A2%E4%B8%8E%E5%81%8F%E8%A7%81-%E7%AE%80%C2%B7%E5%A5%A5%E6%96%AF%E6%B1%80/dp/B01HERFL1E/ref=sr_1_223_twi_pap_1?s=books&ie=UTF8&qid=1478104377&sr=1-223";
        BookPreview bookPreview = new BookPreview(Species.AMAZON, 8198, "ASIN_test", "TITLE_test_傲慢与偏见", "2016年4月", null, url, null, 10000f, 10f, 100);
        Book book = amazonSpider.fetchBook(bookPreview);
        System.out.println(book.toString());
        Assert.assertNotNull(book);
        Assert.assertTrue(book.getAsin().length() > 5);
    }

    @Test
    public void testFetchBook_paperback_noimg() throws IOException {
        String url = "https://www.amazon.cn/%E6%B5%81%E5%8A%A8%E7%9A%84%E7%9B%9B%E5%AE%B4-%E6%B5%B7%E6%98%8E%E5%A8%81-%E6%B5%B7%E6%98%8E%E5%A8%81/dp/B01G9PVB20/ref=sr_1_316_twi_pap_1?s=books&ie=UTF8&qid=1478104380&sr=1-316";
        BookPreview bookPreview = new BookPreview(Species.AMAZON, 8198, "ASIN_test", "TITLE_test_流动的盛宴", "2016年4月", null, url, null, 10000f, 10f, 100);
        Book book = amazonSpider.fetchBookNoretry(bookPreview);
        System.out.println(book.toString());
        Assert.assertNotNull(book);
        Assert.assertTrue(book.getAsin().length() > 5);
    }

    @Test
    public void testFetchBook_paperback_rankAllnull() throws IOException {
        String url = "https://www.amazon.cn/Oliver-Twist-Dickens-Charles/dp/7532751295/ref=sr_1_304_twi_pap_2?s=books&ie=UTF8&qid=1478104380&sr=1-304";
        BookPreview bookPreview = new BookPreview(Species.AMAZON, 8198, "ASIN_test", "TITLE_test_雾都孤儿", "2016年4月", null, url, null, 10000f, 10f, 100);
        Book book = amazonSpider.fetchBookNoretry(bookPreview);
        System.out.println(book.toString());
        Assert.assertNotNull(book);
        Assert.assertTrue(book.getAsin().length() > 5);
    }

    @Test
    public void testFetchBook_paperback_commentnull2() throws IOException {
        String url = "https://www.amazon.cn/%E6%B5%81%E6%98%9F%E4%B9%8B%E7%BB%8A-%E4%B8%9C%E9%87%8E%E5%9C%AD%E5%90%BE/dp/B01GDJZSLM/ref=lp_658495051_1_39_twi_har_2/455-4020582-1799843?s=books&ie=UTF8&qid=1479055546&sr=1-39";
        BookPreview bookPreview = new BookPreview(Species.AMAZON, 8198, "ASIN_test", "TITLE_test_流星之绊", "2016年4月", null, url, null, 10000f, 10f, 100);
        Book book = amazonSpider.fetchBookNoretry(bookPreview);
        System.out.println(book.toString());
        Assert.assertNotNull(book);
        Assert.assertTrue(book.getAsin().length() > 5);
    }

    @Test
    public void testFetchBook_kindle_imgsnull() throws IOException {
        String url = "https://www.amazon.cn/%E9%A9%AC%E5%85%8B%C2%B7%E5%90%90%E6%B8%A9%E7%9F%AD%E7%AF%87%E5%B0%8F%E8%AF%B4%E7%B2%BE%E9%80%89-%E9%A9%AC%E5%85%8B%C2%B7%E5%90%90%E6%B8%A9/dp/B00K6WPILS/ref=sr_1_180_twi_kin_1?s=books&ie=UTF8&qid=1478104375&sr=1-180";
        BookPreview bookPreview = new BookPreview(Species.AMAZON, 8198, "ASIN_test", "TITLE_test_马克·吐温短篇小说精选", "2016年4月", url, null, null, 10000f, 10f, 100);
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

    @Test
    public void testFetchBook_exception_20171108() throws IOException {
        String url = "https://www.amazon.cn/%E5%8C%97%E6%B4%8B%E5%A4%9C%E8%A1%8C%E8%AE%B0-%E9%87%91%E9%86%89/dp/B0766DYZ2F/ref=lp_658495051_1_1_twi_pap_1/460-7445851-5211369?s=books&ie=UTF8&qid=1509938924&sr=1-1";
        BookPreview bookPreview = new BookPreview(Species.AMAZON, 8198, "B0766DYZ2F", "北洋夜行记(亚马逊独家亲笔签名版)(附语音名片)", "2016年4月", null, url, null, 10000f, 10f, 100);
        Book book = amazonSpider.fetchBookNoretry(bookPreview);
        System.out.println(book.toString());
        Assert.assertNotNull(book);
        Assert.assertTrue(book.getAsin().length() > 5);
    }
}
