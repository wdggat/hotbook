package com.iread.conf;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Properties file parser
 */
public class ConfMan {
	// Default options
	public static final String DEFAULT_CONF_PATH = System.getProperty("user.dir")
			+ "/conf.properties";
	private static Logger logger = Logger.getLogger(ConfMan.class);
	private static Properties properties;

    public ConfMan() {
        loadProp();
    }

	private static void loadProp() {
		properties = new Properties();
		try {
			properties.load(new FileInputStream(DEFAULT_CONF_PATH));
		} catch (IOException e) {
			logger.fatal("conf not found, exit", e);
            System.exit(-1);
		}
	}

    public void reload() {
        loadProp();
    }

	public int getInitDelay() {
		return Integer.parseInt(properties.getProperty("delay_init", "0"));
	}
}
