package com.iread.spider;

import com.iread.bean.Book;
import com.iread.bean.BookPreview;
import com.iread.bean.Category;
import com.iread.bean.Storable;
import com.iread.conf.ConfMan;
import com.iread.util.GzipUtil;
import com.iread.util.HttpClientVM;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

/**
 * Created by liu on 16/9/18.
 */
public abstract class Spider {
    private static Logger logger = Logger.getLogger(Spider.class);

    public static HttpClientVM clientVM = new HttpClientVM();
    public static ConfMan conf = new ConfMan();

    public abstract ArrayList<Category> fetchCategorys();
    public abstract ArrayList<BookPreview> fetchBookPreviews(Category category);
    public abstract Book fetchBook(BookPreview bookPreview) throws IOException;

    protected static Document fetchDocument(String url) {
        String content = clientVM.get(url);
        return Jsoup.parse(content);
    }

    public static Document fetchDocument(Storable storable) throws IOException {
        return fetchDocumentCompress(storable);
    }

    private static Document fetchDocumentCompress(Storable storable) throws IOException {
        String decomPath = conf.getWarehouse(storable.getSpecies()) + "/" + storable.getStoreFilename();
        String comPath = decomPath + GzipUtil.EXT;
        File comF = new File(comPath);
        String html = null;
        // 文件不存在或已过期
        if(!comF.exists() || expired(comF)) {
            html = clientVM.get(storable.getUrl());
            File decomF = new File(decomPath);
            FileUtils.write(decomF, html, false);
            if(decomF.length() < storable.getMinSize()) {
                logger.fatal("url request failed, file empty, " + storable.toString());
                return null;
            }
            GzipUtil.compress(decomF);
        } else {
            html = GzipUtil.readCompress(comF);
        }
        return Jsoup.parse(html);
    }

    public static String getCompPath(Storable storable) {
        String decomPath = conf.getWarehouse(storable.getSpecies()) + "/" + storable.getStoreFilename();
        return decomPath + GzipUtil.EXT;
    }

    private static Document fetchDocumentDecompress(Storable storable) throws IOException {
        String path = conf.getWarehouse(storable.getSpecies()) + "/" + storable.getStoreFilename();
        File storeFile = new File(path);
        // 文件不存在或已过期
        if(!storeFile.exists() || expired(storeFile)) {
            String html = clientVM.get(storable.getUrl());
            FileUtils.write(storeFile, html, false);
            return Jsoup.parse(html);
        }
        return Jsoup.parse(storeFile, "UTF-8");
    }

    protected static boolean expired(File file) {
        return (System.currentTimeMillis() - file.lastModified()) / 86400000 >= conf.getShelflife();
    }

    protected static void sleep(long seconds) {
        try {
            logger.info("spider sleep for " + seconds + " seconds");
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
