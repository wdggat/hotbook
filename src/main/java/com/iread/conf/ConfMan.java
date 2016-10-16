package com.iread.conf;

import com.iread.bean.Species;
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

	public int getExecPeriod() {
		return Integer.parseInt(properties.getProperty("exec_period"));
	}

	public int getAmazonBooknumPerCat() {
		return Integer.parseInt(properties.getProperty("amazon_booknum_per_cat"));
	}

	public int getAmazonBooknumPerSquareSortPage() {
		return Integer.parseInt(properties.getProperty("amazon_booknum_per_squaresort"));
	}

	public String getWarehouse(Species species) {
		return properties.getProperty("warehouse") + "/" + species.toString();
	}

    public int getShelflife() {
        return Integer.parseInt(properties.getProperty("shelflife"));
    }

}
