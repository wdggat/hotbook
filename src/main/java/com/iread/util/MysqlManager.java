package com.iread.util;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class MysqlManager {
private Logger logger = Logger.getLogger(MysqlManager.class);
	
	private Properties properties;
	public final String DEFAULT_CONF_PATH = System.getProperty("user.dir")
			+ "/conf.properties";
	private String driver;

	private String url;

	private String user;

	private String password;
	private static MysqlManager INSTANCE;

	private Connection conn = null;

	public String[] getSqlProperties() {
		return new String[] { url, user, password };
	}
	
	public static MysqlManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MysqlManager();
        }
        return INSTANCE;
	}
	
	public MysqlManager(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
		
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			logger.error("can't load dbDriver.");
			e.printStackTrace();
		}
	}

	public MysqlManager() {
		setDefaultProperties();
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			logger.error("can't load dbDriver.");
			e.printStackTrace();
		}
	}

	private void setDefaultProperties(){
		loadConf(getDefaultConfInputStream());
		this.driver = properties.getProperty("db.driver");
		this.url = properties.getProperty("db.url");
		this.user = properties.getProperty("db.username");
		this.password = properties.getProperty("db.password");
	}

	public InputStream getDefaultConfInputStream() {
        try {
            return new FileInputStream(DEFAULT_CONF_PATH);
        } catch (FileNotFoundException e) {
            logger.error("database.properties not found!");
            e.printStackTrace();
            return null;
        }
    }

    protected void loadConf(InputStream in) {
        properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public Connection getConnection() {
		try {
			if (conn == null || conn.isClosed()) {
				conn = DriverManager.getConnection(url, user, password);
			}
		} catch (SQLException e) {
			System.out.println("getConnection failed. driver : " + driver + ", url : " + url + ", user : " + user + ", password : " + password);
			e.printStackTrace();
		}
		return conn;
	}
	
	public void closeConnection(PreparedStatement ps, Connection conn){
		try{
			if(ps != null){
				ps.close();
			}
			if(conn != null){
				conn.close();
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
}
