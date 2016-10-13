package com.iread.util;

import com.iread.bean.Book;
import com.iread.bean.BookPreview;
import com.iread.bean.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by liu on 16/9/26.
 */
public class Exporter {
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
        //TODO
        return 0;
    }

    public static int exportBookPreviews(Collection<BookPreview> bookPreviews) throws SQLException {
        //TODO
        return 0;
    }

}
