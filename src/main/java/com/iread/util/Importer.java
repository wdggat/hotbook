package com.iread.util;

import com.iread.bean.Category;
import com.iread.bean.Species;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by liu on 16/9/26.
 */
public class Importer {
    private static final Logger logger = Logger.getLogger(Importer.class);

    public static List<Category> readCategorys(Species species){
        MysqlManager mysqlManager = MysqlManager.getInstance();
        try{
            Connection conn = mysqlManager.getConnection();
            String sql = "select * from category where species=\"" + species.toString() + "\"";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Category> categories = new ArrayList<Category>();
            while(rs.next()){
                Category cat = new Category();
                int index = 0;
                cat.setCategoryid(rs.getInt(++index));
                cat.setSpecies(Species.getFromName(rs.getString(++index)));
                cat.setType(rs.getInt(++index));
                ++index;
                cat.setUrl(rs.getString(++index));
                cat.setAmount(rs.getInt(++index));
                cat.setLevel(rs.getInt(++index));
                cat.setLeaf(rs.getBoolean(++index));
                cat.setCat1name(rs.getString(++index));
                cat.setCat2name(rs.getString(++index));
                cat.setCat3name(rs.getString(++index));
                cat.setName(cat.getLevel() == 1 ? cat.getCat1name() : (cat.getLevel() == 2 ? cat.getCat2name() : cat.getCat3name()));
                cat.setParent(cat.getLevel() == 1 ? "" : (cat.getLevel() == 2 ? cat.getCat1name() : cat.getCat2name()));
                categories.add(cat);
            }
            return categories;
        } catch (SQLException e){
            logger.error("failed to read categorys of " + species.toString(), e);
            return null;
        }
    }

    /**
     * 记录当前爬取节点
     * @param category
     * @return
     */
    public boolean punchout(Category category, int page) {
        // TODO
        return false;
    }
}
