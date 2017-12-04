package com.iread.util;

import com.iread.bean.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.glassfish.hk2.api.Rank;
import org.glassfish.hk2.classmodel.reflect.ArchiveAdapter;
import org.jsoup.helper.StringUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by liu on 16/9/26.
 */
public class Exporter {
    private static final Logger logger = Logger.getLogger(Exporter.class);

    public static int exportCategorys(Collection<Category> categories) throws SQLException {
        MysqlManager mysqlManager = MysqlManager.getInstance();
        Connection conn = mysqlManager.getConnection();
        String sql = "insert into category (species,type,fullname,url,amount,level,isleaf,cat1name,cat2name,cat3name) " +
                " values (?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE amount=VALUES(amount),url=VALUES(url)";
        PreparedStatement ps = conn.prepareStatement(sql);
        conn.setAutoCommit(false);
        for (Category category : categories) {
            int index = 0;
            ps.setString(++index, category.getSpecies().toString());
            ps.setInt(++index, category.getType());
            ps.setString(++index, category.getCatFullName());
            ps.setString(++index, category.getUrl());
            ps.setInt(++index, category.getAmount());
            ps.setInt(++index, category.getLevel());
            ps.setBoolean(++index, category.isLeaf());
            ps.setString(++index, category.getCat1name());
            ps.setString(++index, category.getCat2name() == null ? "" : category.getCat2name());
            ps.setString(++index, category.getCat3name() == null ? "" : category.getCat3name());
            ps.addBatch();
        }
        ps.executeBatch();
        conn.commit();
        return categories.size();
    }

    public static int exportBooks(Collection<Book> books) throws SQLException {
        exam(books);
        exportBooksNoAttach(books);
        for (Book book : books) {
            try {
                exportRank(book);

                if (book.getAlsoBuy() != null) {
                    exportSuggests(book.getBookid(), book.getAlsoBuy());
                }
                if (book.getBuyTogether() != null) {
                    exportSuggests(book.getBookid(), book.getBuyTogether());
                }
                if (book.getVisitorBuy() != null) {
                    exportSuggests(book.getBookid(), book.getVisitorBuy());
                }
//                if (book.getComments() != null) {
//                    exportComments(book.getBookid(), book.getComments());
//                }
            } catch (Exception e) {
                logger.error("export book failed, " + book.toJsonStr() + "\n", e);
                System.exit(-1);
            }
        }
        return books.size();
    }

    private static void exam(Collection<Book> books) {
        for (Book book : books) {
            if (book.getUrl().length() > 1000) {
                logger.warn("URL TOO LONG : " + book.toJsonStr());
            }
        }
    }

    private static int exportBooksNoAttach(Collection<Book> books) throws SQLException {
        MysqlManager mysqlManager = MysqlManager.getInstance();
        Connection conn = mysqlManager.getConnection();
        String sql = "insert into book values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE " +
                "commentNum=VALUES(commentNum),price=VALUES(price),starGroups=VALUES(starGroups),imgUrls=VALUES(imgUrls),url=VALUES(url);";
        PreparedStatement ps = conn.prepareStatement(sql);
        conn.setAutoCommit(false);
        for (Book book : books) {
            psAddBook(ps, book);
        }
        ps.executeBatch();
        conn.commit();
        return books.size();
    }

    private static void psAddBook(PreparedStatement ps, Book book) throws SQLException {
        int index = 0;
        ps.setString(++index, book.getBookid());
        ps.setString(++index, book.getSpecies().toString());
        ps.setString(++index, book.getTitle());
        ps.setString(++index, book.getWrapType().toString());
        ps.setDate(++index, new Date(book.getOnloadDate() == null ? 0 : book.getOnloadDate().getTimeInMillis()));
        ps.setString(++index, StringUtils.join(book.getAuthor().iterator(), ','));
        ps.setString(++index, StringUtils.join(book.getTranslator().iterator(), ','));
        ps.setFloat(++index, (float) book.getStar());
        ps.setInt(++index, book.getCommentNum());
        ps.setInt(++index, book.getCategory() == null ? -1 : book.getCategory().getCategoryid());
        ps.setString(++index, book.getSeller());
        ps.setFloat(++index, (float) book.getPrice());
        ps.setString(++index, book.getDescription());
        ps.setString(++index, book.getPosterUrl());
        ps.setString(++index, StringUtils.join(book.getImgUrls().iterator(), ','));
        ps.setString(++index, book.getPublisher());
        ps.setInt(++index, book.getPageNum());
        ps.setString(++index, book.getLanguage());
        ps.setInt(++index, book.getSize());
        ps.setString(++index, book.getIsbn());
        ps.setString(++index, book.getBarcode());
        ps.setFloat(++index, (float) book.getLength());
        ps.setFloat(++index, (float) book.getWidth());
        ps.setFloat(++index, (float) book.getHeight());
        ps.setFloat(++index, (float) book.getWeight());
        ps.setString(++index, book.getBrand());
        ps.setString(++index, book.getAsin());
        ps.setString(++index, StringUtils.substring(book.getEditorSuggest(), 0, 2000));
        ps.setString(++index, StringUtils.substring(book.getCelebritySuggest(), 0, 2000));
        ps.setString(++index, StringUtils.substring(book.getMediaSuggest(), 0, 2000));
        ps.setString(++index, StringUtils.substring(book.getAuthorIntro(), 0, 2000));
        ps.setString(++index, StringUtils.substring(book.getCatalog(), 0, 2000));
        ps.setString(++index, StringUtils.substring(book.getPreface(), 0, 2000));
        ps.setString(++index, StringUtils.substring(book.getDigest(), 0, 2000));
        ps.setString(++index, StringUtils.join(book.getStarGroups().iterator(), ','));
        ps.setString(++index, book.getUrl());
        ps.addBatch();
    }

    private static int exportRank(Book book) throws SQLException {
        MysqlManager mysqlManager = MysqlManager.getInstance();
        Connection conn = mysqlManager.getConnection();
        String sql = "insert into rank values (?,?,?) ON DUPLICATE KEY UPDATE rank=VALUES(rank);";
        PreparedStatement ps = conn.prepareStatement(sql);
        conn.setAutoCommit(false);
        ps.setString(1, book.getBookid());
        ps.setInt(2, 0);
        ps.setInt(3, book.getRankAll());
        ps.addBatch();
        if (book.getRankCats().keySet() != null) {
            for (Category category : book.getRankCats().keySet()) {
                if (category != null) {
                    ps.setString(1, book.getBookid());
                    ps.setInt(2, category.getCategoryid());
                    ps.setInt(3, book.getRankCats().get(category));
                    ps.addBatch();
                }
            }
        }
        ps.executeBatch();
        conn.commit();
        return book.getRankCats().size() + 1;
    }

    private static int exportSuggests(String bookid, Collection<Suggest> suggests) throws Exception {
        MysqlManager mysqlManager = MysqlManager.getInstance();
        Connection conn = mysqlManager.getConnection();
        String sql = "insert into suggest values (?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE " +
                "price=VALUES(price),star=VALUES(star),commentNum=VALUES(commentNum);";
        Suggest currentSuggest = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            conn.setAutoCommit(false);
            for (Suggest suggest : suggests) {
                currentSuggest = suggest;
                int index = 0;
                ps.setString(++index, bookid);
                ps.setString(++index, suggest.getAsin());
                ps.setString(++index, suggest.getTitle());
                ps.setString(++index, suggest.getAuthor());
                ps.setInt(++index, (suggest.getWrapType() != null ? suggest.getWrapType().ordinal() : 0));
                ps.setFloat(++index, (float) suggest.getPrice());
                ps.setString(++index, suggest.getImgUrl());
                ps.setFloat(++index, (float) suggest.getStar());
                ps.setInt(++index, suggest.getCommentNum());
                ps.setString(++index, suggest.getUrl());
                ps.setInt(++index, suggest.getSuggestType().ordinal());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
        } catch (Exception e) {
            logger.error("export suggest failed, " + currentSuggest.toString(), e);
            throw e;
        }
        return suggests.size();
    }

    public static int exportComments(String bookid, Collection<Comment> comments) {
        MysqlManager mysqlManager = MysqlManager.getInstance();
        Connection conn = mysqlManager.getConnection();
        String sql = "insert into comment values (?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE " +
                "star=VALUES(star),praise=VALUES(praise);";
        Comment currentComment = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            conn.setAutoCommit(false);
            for (Comment comment : comments) {
                currentComment = comment;
                int index = 0;
                ps.setString(++index, bookid);
                ps.setString(++index, comment.getId());
                ps.setInt(++index, comment.getStar());
                ps.setDate(++index, new Date(comment.getDate().getTimeInMillis()));
                ps.setString(++index, comment.getTitle());
                ps.setString(++index, comment.getAuthor());
                String content = org.apache.commons.codec.binary.StringUtils.newStringUtf8(comment.getContent().getBytes());
                ps.setString(++index, content);
                ps.setInt(++index, comment.getPraise());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
        } catch (Exception e) {
            logger.error("export suggest failed, " + currentComment.toString(), e);
            System.exit(-1);
        }
        return comments.size();
    }

    public static int exportBookPreviews(Collection<BookPreview> bookPreviews) throws SQLException {
        //TODO
        return 0;
    }

}
