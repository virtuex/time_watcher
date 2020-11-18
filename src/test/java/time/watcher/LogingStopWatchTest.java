package time.watcher;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import time.watcher.helper.StopWatchPack;
import time.watcher.slf4j.Slf4jStopWatch;

public class LogingStopWatchTest {
    private static Logger logger = LoggerFactory.getLogger(LogingStopWatchTest.class);

    public static void main(String[] args) {
        PropertyConfigurator.configure("C:\\workspace\\time_watcher\\src\\main\\resources\\log4j.properties");
        initWatch("ROOT");
        startWatch("TEST");
        startWatch("TEST2-1");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch();
        startWatch("TEST2-2");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch();
        stopWatch();
        outAndReleaseWatch();
    }


    /**
     * 开始监控任务，与stopWatch成对出现
     */
    protected static void startWatch(String taskName) {
        if (logger.isDebugEnabled()) {
            StopWatchContext.startWatch(taskName, -1);
        }
    }

    /**
     * 结束监控任务，与startWatch成对出现
     */
    protected static void stopWatch() {
        if (logger.isDebugEnabled()) {
            StopWatchContext.stopWatch();
        }
    }

    /**
     * 初始化监控
     */
    private static void initWatch(String watchName) {
        if (logger.isDebugEnabled()) {
            StopWatch stopWatch = new Slf4jStopWatch(watchName, true, -1);
            StopWatchContext.setCurrentStopWatch(new StopWatchPack(stopWatch));
            // 开始根任务
            startWatch("--");
        }
    }


    /**
     * 始放监控资源
     */
    private static void outAndReleaseWatch() {
        if (logger.isDebugEnabled()) {
            // 结束根任务
            stopWatch();
            // 打印详细
            StopWatchContext.prettyPrint();
            StopWatchContext.releaseStopWatch();
        }
    }
}