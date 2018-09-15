package com.iread.selector;

import com.iread.bean.Book;
import com.iread.bean.Category;
import com.iread.bean.Species;
import com.iread.bean.WrapType;
import com.iread.util.MysqlManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.util.HttpUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by liuxiaolong on 17/12/5.
 */
public class Selector {
    private static final Logger logger = Logger.getLogger(Selector.class);

    public Book selectABook(Species species) {
        MysqlManager mysqlManager = MysqlManager.getInstance();
        try{
            Connection conn = mysqlManager.getConnection();
            String sql = "select * from book where commentNum > 20 and asin not in (select asin from published) and categoryid in (select id from category where fullname not rlike '小说') order by star desc limit 5;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Book book = new Book();
            while(rs.next()){
                book.setSpecies(species);
                book.setTitle(rs.getString("title"));
                book.setWrapType(WrapType.getFromName(rs.getString("wraptype")));
                Date onloadDate = rs.getDate("onloadDate");
                Calendar c = Calendar.getInstance();
                c.setTime(onloadDate);
                book.setOnloadDate(c);
                book.setAuthor(toList(rs.getString("author"), ","));
                book.setTranslator(toList(rs.getString("translator"), ","));
                book.setStar(rs.getDouble("star"));
                book.setCommentNum(rs.getInt("commentNum"));
                book.setSeller(rs.getString("seller"));
                book.setPrice(rs.getFloat("price"));
                book.setDescription(rs.getString("description"));
                book.setPosterUrl(rs.getString("posterUrl"));
                book.setImgUrls(toList(rs.getString("imgUrls"), "jpg,"));
                book.setPublisher(rs.getString("publisher"));
                book.setPageNum(rs.getInt("pagenum"));
                book.setLanguage(rs.getString("language"));
                book.setSize(rs.getInt("size"));
                book.setIsbn(rs.getString("isbn"));
                book.setBarcode(rs.getString("barcode"));
                book.setLength(rs.getDouble("length"));
                book.setHeight(rs.getDouble("height"));
                book.setWeight(rs.getDouble("weight"));
                book.setBrand(rs.getString("brand"));
                book.setAsin(rs.getString("asin"));
                book.setEditorSuggest(rs.getString("editorSuggest"));
                book.setCelebritySuggest(rs.getString("celebritySuggest"));
                book.setMediaSuggest(rs.getString("mediaSuggest"));
                book.setAuthorIntro(rs.getString("authorIntro"));
                book.setCatalog(rs.getString("catalog"));
                book.setPreface(rs.getString("preface"));
                book.setDigest(rs.getString("digest"));
                book.setStarGroups(toListNums(rs.getString("starGroups")));
                book.setUrl(rs.getString("url"));
            }
            return book;
        } catch (SQLException e){
            logger.error("failed to read categorys of " + species.toString(), e);
            return null;
        }
    }

    public boolean dumpToFile(Book book, String usedDir) {
        ArrayList<String> lines = new ArrayList<String>();
        lines.add("ASIN: " + book.getAsin());
        lines.add("TITLE: " + book.getTitle());
        lines.add("AUTHOR: " + book.getAuthor());
        lines.add("URL: " + book.getUrl());
        lines.add("EDITSUGG: " + book.getEditorSuggest());
        lines.add("CELESUGG: " + book.getCelebritySuggest());
        lines.add("MEDIASUGG: " + book.getMediaSuggest());
        lines.add("AUTHORINFO: " + book.getAuthorIntro());
        lines.add("DESCRIPTION: " + book.getDescription());
        lines.add("CATALOG: " + book.getCatalog());
        lines.add("PREFACE: " + book.getPreface());
        lines.add("DIGEST: " + book.getDigest());
        try {
            final int TIMEOUT = 20 * 1000;
            String dirname = usedDir + "/" + book.getAsin() + "_" + book.getTitle();
            String detailsFilepath = dirname + "/" + book.getAsin() + "_" + book.getTitle() + ".txt";
            FileUtils.writeLines(new File(detailsFilepath), lines);
            String postalFilepath = usedDir + "/" + book.getAsin() + "_" + book.getTitle() + "_postal.jpg";
            if (StringUtils.isNotBlank(book.getPosterUrl())) {
                logger.info("got poster: " + book.getPosterUrl() + ", dest: " + postalFilepath);
                FileUtils.copyURLToFile(new URL(book.getPosterUrl()), new File(postalFilepath), TIMEOUT, TIMEOUT);
            }
            for (int i = 0; i < book.getImgUrls().size(); i++) {
                String jpgFilepath = dirname + "/" + book.getAsin() + "_" + book.getTitle() + "_cover_" + i + ".jpg";
                logger.info("got a pic: " + book.getImgUrls().get(i) + ", dest: " + jpgFilepath);
                FileUtils.copyURLToFile(new URL(book.getImgUrls().get(i)), new File(jpgFilepath), TIMEOUT, TIMEOUT);
            }
        } catch (IOException e) {
            logger.fatal("Error dump book to dir, " + book.toJsonStr(), e);
            return false;
        }
        return true;
    }

    private ArrayList<String> toList(String src, String sep) {
        ArrayList<String> ret = new ArrayList<String>();
        CollectionUtils.addAll(ret, src.split(sep));
        return ret;
    }

    private ArrayList<Integer> toListNums(String src) {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (String item : src.split(",")) {
            ret.add(Integer.parseInt(item));
        }
        return ret;
    }
}
