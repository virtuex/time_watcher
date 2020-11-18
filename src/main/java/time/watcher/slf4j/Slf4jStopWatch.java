package time.watcher.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import time.watcher.LogingStopWatch;

/**
 * @author xuda
 * @email xuda.it@outlook.com
 */
public class Slf4jStopWatch extends LogingStopWatch {

    private static final long serialVersionUID = 1L;

    private final Logger logger;

    /**
     *
     * @param id
     *            标识符，标识此记录器
     * @param startWatch
     *            是否开启日志记录器
     * @param filterTime
     *            当开启日志记录器时，时间过滤，小于此时间的不输出，单位为毫秒，小于0表示不过滤
     */
    public Slf4jStopWatch(String id, boolean startWatch, long filterTime) {
        this(id, startWatch, filterTime, Slf4jStopWatch.class.getName());
    }

    /**
     *
     * @param id
     *            标识符，标识此记录器
     * @param startWatch
     *            是否开启日志记录器
     * @param filterTime
     *            当开启日志记录器时，时间过滤，小于此时间的不输出，单位为毫秒，小于0表示不过滤
     * @param loggerName
     *            日志记录名称，可以是Slf4jStopWatch.class.getName();
     */
    public Slf4jStopWatch(String id, boolean startWatch, long filterTime, String loggerName) {
        super(id, startWatch, filterTime);
        logger = LoggerFactory.getLogger(loggerName);
    }

    @Override
    protected void log(String prettyPrint) {
        if (this.isStartWatch() && logger.isDebugEnabled()) {
            logger.debug(prettyPrint);
        }
    }
}
