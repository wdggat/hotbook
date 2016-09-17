package com.iread;

import com.iread.conf.ConfMan;
import com.iread.spider.Spider;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class ScrawlerTask implements Runnable {
    private Logger logger = Logger.getLogger(ScrawlerTask.class);
    private int searchRounds;

    private ConfMan conf;
    private Spider spider;

    public ScrawlerTask(ConfMan conf) {
        this.conf = conf;
        this.searchRounds = 1;
    }

    public void run() {
        try {
            // Reload configurations that changes at runtime
            conf.reload();

        } catch (Throwable e) {
            logger.error("Something wrong happens during spider running:",
                    e);
        }
    }

    private File moveDir(String srcPath, String destPath) {
        File srcDir = new File(srcPath);
        if (!srcDir.exists()) {
            logger.info("Failed to move dirs since File " + srcPath
                    + " doesn't exist");
            return null;
        }
        File destDir = new File(destPath);
        try {
            FileUtils.moveDirectory(srcDir, destDir);
            logger.info("Move " + srcPath + " to " + destPath);
        } catch (IOException e) {
            logger.error("Can't move " + srcPath + " to " + destPath, e);
            return null;
        }
        return destDir;
    }
}
