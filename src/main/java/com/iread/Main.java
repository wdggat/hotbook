package com.iread;

import com.iread.bean.Category;
import com.iread.bean.Species;
import com.iread.conf.ConfMan;
import com.iread.spider.AmazonSpider;
import com.iread.spider.Spider;
import com.iread.util.Exporter;
import com.iread.util.WBListHelper;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class);

    private static ConfMan conf;

    @Deprecated
    private static void scheduleScrawlers(Species species) {
        Scheduler scheduler = new Scheduler(conf, species);
        scheduler.startToWork();
    }

    private static void printHelp() {
        System.out.println("{0} start");
    }

    public static void main(String args[]) {
        PropertyConfigurator.configure(conf.DEFAULT_CONF_PATH);
        conf = new ConfMan();
        String speciesName = args[0];
        String command = args[1];
        Species species = Species.getFromName(speciesName);
        if (WBListHelper.init()) {
            logger.info("white and black lists loaded successfully");
        } else {
            logger.fatal("failed to load white and black lists");
            System.exit(-1);
        }
        if(command.equals("book")) {
//            scheduleScrawlers(species);
            ScrawlerTask bookTask = new ScrawlerTask(conf, new AmazonSpider(conf), species);
            bookTask.run();
        } else if (command.equals("category")) {
            logger.info("Begin to fetch categorys of " + species.toString());
            long timeBegin = System.currentTimeMillis();
            Spider spider = new AmazonSpider(conf);
            ArrayList<Category> categories = spider.fetchCategorys();
            long timeEnd = System.currentTimeMillis();
            logger.info("Fetched " + categories.size() + " categorys, time taken: " + (timeEnd - timeBegin) / 1000 + "s");
            try {
                Exporter.exportCategorys(categories);
                logger.info(categories.size() + " categorys exported to db");
            } catch (SQLException e) {
                logger.error("Failed to export categorys to db. ", e);
                return;
            }
        } else {
            printHelp();
        }
    }
}
