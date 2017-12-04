package com.iread.spider;

import com.iread.bean.*;
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
    public abstract Book fetchBook(BookPreview bookPreview) throws Exception;

    protected static Document fetchDocument(String url) {
        String content = clientVM.get(url);
        return Jsoup.parse(content);
    }

    /**
     *
     * @param storable
     * @param delOldFile 是否判断过期时间，删除旧文件
     * @return
     * @throws IOException
     */
    public static FetchResponse fetchDocument(Storable storable, boolean delOldFile) throws IOException {
        return fetchDocumentCompress(storable, delOldFile);
    }

    private static FetchResponse fetchDocumentCompress(Storable storable, Boolean delOldFile) throws IOException {
        String decomPath = conf.getWarehouse(storable.getSpecies()) + "/" + storable.getStoreFilename();
        String comPath = decomPath + GzipUtil.EXT;
        File comF = new File(comPath);
        String html = null;
        // 文件不存在或已过期
        if(!comF.exists() || (expired(comF) && delOldFile) || isFileEmpty(comF)) {
            html = clientVM.get(storable.getUrl());
            if (StringUtils.contains(html, "您输入的网址在我们的网站上无法正常显示网页")) {
                return new FetchResponse(FetchResponse.RetCode.NONEXIST, null);
            }
            File decomF = new File(decomPath);
            if (html.length() > storable.getMinSize()) {
                FileUtils.write(decomF, html, false);
            }
            if(decomF.length() < storable.getMinSize()) {
                logger.fatal("url request failed, html.length()=" + html.length() + ", file empty, " + storable.toString());
                logger.fatal(html);
                return new FetchResponse(FetchResponse.RetCode.REFUSED, null);
            }
            GzipUtil.compress(decomF);
        } else {
            html = GzipUtil.readCompress(comF);
        }
        return new FetchResponse(FetchResponse.RetCode.OK, Jsoup.parse(html));
    }

    public static String getCompPath(Storable storable) {
        String decomPath = conf.getWarehouse(storable.getSpecies()) + "/" + storable.getStoreFilename();
        return decomPath + GzipUtil.EXT;
    }

/*    private static Document fetchDocumentDecompress(Storable storable) throws IOException {
        String path = conf.getWarehouse(storable.getSpecies()) + "/" + storable.getStoreFilename();
        File storeFile = new File(path);
        // 文件不存在或已过期
        if(!storeFile.exists() || expired(storeFile)) {
            String html = clientVM.get(storable.getUrl());
            FileUtils.write(storeFile, html, false);
            return Jsoup.parse(html);
        }
        return Jsoup.parse(storeFile, "UTF-8");
    }*/

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

    private static boolean isFileEmpty(File file) {
        return FileUtils.sizeOf(file) < 1024;
    }

}
