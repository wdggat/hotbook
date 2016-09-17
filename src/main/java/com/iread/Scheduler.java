package com.iread;

import java.util.concurrent.*;

import com.iread.conf.ConfMan;
import com.iread.spider.Spider;
import org.apache.log4j.Logger;

public class Scheduler {
    private static Logger logger = Logger.getLogger(Scheduler.class);
    
    private final ScheduledExecutorService scheduler;
    private final int initialDelay;
    private static ConfMan conf;
    private static final int NUM_THREADS = 1;

    Scheduler(ConfMan conf) {
        this.initialDelay = conf.getInitDelay();
        this.conf = conf;
        scheduler = Executors.newScheduledThreadPool(NUM_THREADS);
    }
    
    public void startToWork() {
        startToWork(new ScrawlerTask(conf));
    }

    public void startToWork(Runnable task) {
        logger.info("Scheduler is started");
        final ScheduledFuture<?> scrawlerFuture = scheduler
                .scheduleAtFixedRate(task, initialDelay, 0,
                        TimeUnit.SECONDS);

        scheduler.schedule(new Runnable() {
            public void run() {
                scrawlerFuture.cancel(false);
                logger.info("Scheduler is terminated.");                
            }
        }, 0, TimeUnit.SECONDS);
    }
}
