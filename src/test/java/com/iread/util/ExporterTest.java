package com.iread.util;

import com.iread.bean.Book;
import com.iread.bean.BookPreview;
import com.iread.bean.Category;
import com.iread.bean.Species;
import com.iread.conf.ConfMan;
import com.iread.spider.AmazonSpider;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxiaolong on 16/10/11.
 */
public class ExporterTest {
    @BeforeClass
    public static void setUp() {
        PropertyConfigurator.configure(new ConfMan().DEFAULT_CONF_PATH);
    }
    @Test
    public  void testExportCategorys() throws SQLException {
        String url = "https://www.amazon.cn/s/ref=lp_658495051_il_ti_stripbooks?rh=n%3A658390051%2Cn%3A%21658391051%2Cn%3A658393051%2Cn%3A658495051&ie=UTF8&qid=1474476399&lo=stripbooks";
        Category category = new Category(Species.AMAZON, Category.TYPE_NORMAL, "推理小说", "小说", url, 200, 2);
        category.setCat1name("小说_test");
//        category.setCat2name("");
//        category.setCat3name("");
//        category.setCat2name("小说_test_cat2");
//        category.setCat3name("小说_test_cat3");
        List<Category> categories = new ArrayList<Category>();
        categories.add(category);
        int actual = Exporter.exportCategorys(categories);
        Assert.assertEquals(1, actual);
    }

    @Test
    public void testExportBook() throws Exception {
        AmazonSpider amazonSpider = new AmazonSpider(new ConfMan());
        String url = "https://www.amazon.cn/%E9%AD%94%E9%AC%BC%E7%BB%8F%E6%B5%8E%E5%AD%A6%E7%B3%BB%E5%88%97-%E5%8F%B2%E8%92%82%E8%8A%AC%C2%B7%E5%88%97%E7%BB%B4%E7%89%B9/dp/B01KV0D9OW/ref=sr_1_3?s=books&ie=UTF8&qid=1474475789&sr=1-3";
        BookPreview bookPreview = new BookPreview(Species.AMAZON, 263, "ASIN_test", "TITLE_test_魔鬼经济学", "2016年4月", null, url, null, 10000f, 10f, 100);
        Book book = amazonSpider.fetchBook(bookPreview);
        List<Book> books = new ArrayList<Book>();
        books.add(book);
        int actual = Exporter.exportBooks(books);
        Assert.assertEquals(1, actual);
    }

    @Test
    public void clearDb() throws SQLException {
        MysqlManager mysqlManager = MysqlManager.getInstance();
        Connection conn = mysqlManager.getConnection();
        String sql = "delete from category where cat1name='小说_test';delete from book where title = 'TITLE_test_魔鬼经济学';";
        PreparedStatement ps = conn.prepareStatement(sql);
        Assert.assertTrue(ps.execute());
        mysqlManager.closeConnection(ps, conn);
    }


}
