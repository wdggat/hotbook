package com.iread.util;

import com.iread.bean.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by liu on 16/9/26.
 */
public class Exporter {
    public static int exportCategorys(Collection<Category> records) throws SQLException {
        MysqlManager mysqlManager = MysqlManager.getInstance();
        Connection conn = mysqlManager.getConnection();
        String sql = "insert into repost (date,username,renzheng,location,fans, " +
                "follow,weibonumber,content,weibourl,repub,reply,keyword," +
                "platform,pathway, weiboid, userid, nickname, gender, lastweibotime, " +
                "birthday, tags, trends, brief, level, hostwid, hostwuser)"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        conn.setAutoCommit(false);
        for (WeiboRecord record : records) {
            ps.setString(1,dateTransform(record.getDate()));
            ps.setString(2, WeiboUtils.removeInvalidCharacters(record.getUsername()));
            ps.setBoolean(3, record.getRenzheng());
            ps.setString(4, WeiboUtils.removeInvalidCharacters(record.getLocation()));
            ps.setInt(5, record.getFans());
            ps.setInt(6, record.getFollow());
            ps.setInt(7, record.getWeiboNumber());
            ps.setString(8, WeiboUtils.removeInvalidCharacters(record.getContent()));
            ps.setString(9, record.getWeiboUrl());
            ps.setInt(10, record.getRepub());
            ps.setInt(11, record.getReply());
            ps.setString(12, record.getKeyword());
            ps.setString(13, record.getWeiboSource());
            ps.setString(14, record.getPathway());
            ps.setString(15, record.getWeiboid());
            ps.setString(16, record.getUserid());
            ps.setString(17, record.getNickname());
            ps.setString(18, record.getGender());
            long time = record.getLastweibotime() == null ? 1000 : (record.getLastweibotime().getTime() > 0 ? record.getLastweibotime().getTime() : 1000);
            ps.setTimestamp(19, new Timestamp(time));
            time = record.getBirthday() == null ? 1000 : (record.getBirthday().getTime() > 0 ? record.getBirthday().getTime() : 1000);
            ps.setTimestamp(20, new Timestamp(time));
            ps.setString(21, WeiboUtils.removeInvalidCharacters(record.getTags()));
            ps.setString(22, WeiboUtils.removeInvalidCharacters(record.getTrends()));
            ps.setString(23, WeiboUtils.removeInvalidCharacters(record.getBrief()));
            ps.setInt(24, record.getLevel());
            ps.setString(25, record.getHostwid());
            ps.setString(26, record.getHostwuser());
            ps.addBatch();
        }
        ps.executeBatch();
        conn.commit();
        System.out.println(records.size() + " records inserted into weibo_record.");
        return records.size();
    }

    public static List<WeiboRecord> readRepostsFromSql(String platform, String hostwuser){
        MysqlManager mysqlManager = new MysqlManager();
        try{
            Connection conn = mysqlManager.getConnection();
            String sql = "select * from repost where platform=\"" + platform + "\" and hostwuser=\"" + hostwuser + "\"";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<WeiboRecord> records = new ArrayList<WeiboRecord>();
            while(rs.next()){
                WeiboRecord record = new WeiboRecord();
                record.setContent(rs.getString("content"));
                record.setReply(rs.getInt("reply"));
                record.setRepub(rs.getInt("repub"));
                record.setPathway(rs.getString("pathway"));
                record.setBirthday(rs.getTimestamp("birthday"));
                record.setBrief(rs.getString("brief"));
                record.setDate(rs.getTimestamp("date").toString());
                record.setFans(rs.getInt("fans"));
                record.setFollow(rs.getInt("follow"));
                record.setGender(rs.getString("gender"));
                record.setHostwid(rs.getString("hostwid"));
                record.setHostwuser("hostwuser");
                record.setLastweibotime(rs.getTimestamp("lastweibotime"));
                record.setLevel(rs.getInt("level"));
                record.setLocation(rs.getString("location"));
                record.setNickname(rs.getString("nickname"));
                record.setRenzheng(rs.getBoolean("renzheng"));
                record.setTags(rs.getString("tags"));
                record.setTrends(rs.getString("trends"));
                record.setWeiboSource(rs.getString("platform"));
                record.setWeiboUrl(rs.getString("weibourl"));
                record.setKeyword(rs.getString("keyword"));
                records.add(record);
            }
            mysqlManager.closeConnection(ps, conn);
            return records;
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("failed to read reposts of " + hostwuser + " from mysql");
            return null;
        }
    }
}
