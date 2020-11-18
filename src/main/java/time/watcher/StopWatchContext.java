package time.watcher;

import time.watcher.helper.StopWatchPack;

/**
 * @author xuda
 * @email xuda.it@outlook.com
 */
public class StopWatchContext {

    private static final ThreadLocal<StopWatchPack> LOCAL_STOP_WATCH = new ThreadLocal<StopWatchPack>();

    /**
     * 设置当前线程StopWatch
     *
     * @param stopWatch
     */
    public static void setCurrentStopWatch(StopWatchPack stopWatch) {
        if (stopWatch == null || stopWatch.getRootStopWatch() == null) {
            throw new RuntimeException("StopWatch can not be null！");
        }
        StopWatchPack curStopWatch = LOCAL_STOP_WATCH.get();
        if (curStopWatch != null && curStopWatch.getRootStopWatch() != null) {
            throw new RuntimeException(
                    curStopWatch.getRootStopWatch().getId() + " already exists, Can not add：" + stopWatch.getRootStopWatch().getId());
        }
        LOCAL_STOP_WATCH.set(stopWatch);
    }

    /**
     * 获取当前StopWatch
     *
     * @return
     */
    public static StopWatchPack getCurrentStopWatch() {
        StopWatchPack curStopWatch = LOCAL_STOP_WATCH.get();
        if (curStopWatch == null) {
            // throw new RuntimeException("请先调用setCurrentStopWatch！");
        }
        return curStopWatch;
    }

    /**
     * 释放资源
     *
     * @return
     */
    public static StopWatchPack releaseStopWatch() {
        StopWatchPack curStopWatch = LOCAL_STOP_WATCH.get();
        if (curStopWatch != null) {
            LOCAL_STOP_WATCH.remove();
            return curStopWatch;
        } else {
            return null;
        }
    }

    /**
     * 打印当前监听器的类容
     *
     * @return
     */
    public static StopWatchPack prettyPrint() {
        StopWatchPack stopWatch = getCurrentStopWatch();
        if (stopWatch != null && stopWatch.getRootStopWatch() != null) {
            stopWatch.getRootStopWatch().prettyPrint();
        }
        return stopWatch;
    }

    /**
     * 获取当前线程的监听器并启动 filterTime会默认设置为-1 即所有监控都打印出来，不做filter
     */
    public static StopWatchPack startWatch(String taskName) {
        return startWatch(taskName, -1);
    }


    /**
     * 获取当前线程的监听器并启动
     *
     * @param filterTimeMilles 单位为毫秒，如果小于等于0的情况下，则打印所有监控，不做filter
     */
    public static StopWatchPack startWatch(String taskName, long filterTimeMilles) {

        StopWatchPack stopWatchPack = getCurrentStopWatch();
        if (stopWatchPack == null) {
            return null;
        }
        StopWatch stopWatch = stopWatchPack.getStopWatch();
        if (stopWatch.isStart()) {
            // 如果已经启用，重复执行，则表示嵌套监听
            stopWatch = new StopWatch(taskName, stopWatchPack.getRootStopWatch().isStartWatch(), filterTimeMilles);
            stopWatchPack.addStopWatch(stopWatch);
            stopWatch.start(taskName);
        } else {
            stopWatch.start(taskName);
        }
        return stopWatchPack;
    }


    /**
     * 获取当前线程的监听器并停止
     *
     * @return
     */
    public static StopWatchPack stopWatch() {
        StopWatchPack stopWatchPack = getCurrentStopWatch();
        if (stopWatchPack == null) {
            return null;
        }
        StopWatch stopWatch = stopWatchPack.getStopWatch();
        if (stopWatch.isStart()) {
            stopWatch.stop();
        } else {
            // 如果还没有停止回调，则认为是嵌套服务，应该把上级关掉
            stopWatchPack.removeStopWatch();// 删除不在使用的服务
            stopWatch = stopWatchPack.getStopWatch();
            stopWatch.stop();
        }

        return stopWatchPack;
    }

}
