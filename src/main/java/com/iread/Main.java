package com.iread;

import com.iread.conf.ConfMan;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class);

    private static ConfMan conf;

    private static void scheduleScrawlers(String weiboSource) {
        Scheduler scheduler = new Scheduler(conf);
        scheduler.startToWork();
    }

    private static void printHelp() {
        System.out.println("{0} start");
    }

    public static void main(String args[]) {
        PropertyConfigurator.configure(conf.DEFAULT_CONF_PATH);
    }
}
