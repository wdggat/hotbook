package com.iread.util;

import com.iread.bean.BlackWhiteListType;
import com.iread.bean.Category;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxiaolong on 16/10/11.
 */
public class WBListHelper {
    private static final Logger logger = Logger.getLogger(WBListHelper.class);
    private static List<BlackWhiteListType> categoryWhiteList;
    private static List<BlackWhiteListType> bookBlackList;

    public static boolean init() {
        List<BlackWhiteListType> allList = readAllBWlist();
        if (allList.size() == 0) {
            return false;
        }
        categoryWhiteList = new ArrayList<BlackWhiteListType>();
        bookBlackList = new ArrayList<BlackWhiteListType>();
        for(BlackWhiteListType listType : allList) {
            if (listType.getType() == BlackWhiteListType.TYPE_CATEGORY) {
                categoryWhiteList.add(listType);
            } else if (listType.getType() == BlackWhiteListType.TYPE_BOOK) {
                bookBlackList.add(listType);
            }
        }
        logger.info("cat1 whitelist inited, " + ArrayUtils.toString(categoryWhiteList));
        return true;
    }

    private static List<BlackWhiteListType> readAllBWlist() {
        String sql = "select type,blackorwhite,value FROM wblist;";
        try{
            MysqlManager mysqlManager = MysqlManager.getInstance();
            Connection conn = mysqlManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<BlackWhiteListType> list = new ArrayList<BlackWhiteListType>();
            while(rs.next()){
                int type = rs.getInt(1);
                int blackorwhite = rs.getInt(2);
                String value = rs.getString(3);
                list.add(new BlackWhiteListType(type, blackorwhite, value));
            }
            return list;
        } catch (SQLException e){
            logger.error("failed to read black and white lists, ", e);
            return null;
        }
    }

    public static boolean checkCategory(Category category) {
        for (BlackWhiteListType categoryList : categoryWhiteList) {
            if(categoryList.getValue().equals(category.getCat1name())) {
                return true;
            }
        }
        return false;
    }

}
