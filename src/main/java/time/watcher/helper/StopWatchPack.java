package time.watcher.helper;

import time.watcher.StopWatch;

import java.util.LinkedList;
import java.util.List;

/**
 * @author xuda
 * @email xuda.it@outlook.com
 */
public class StopWatchPack {
    private StopWatch rootStopWatch;
    private List<StopWatch> stopWatchArray = new LinkedList<StopWatch>();
    private int index = -1;

    public StopWatchPack(StopWatch rootStopWatch) {
        this.rootStopWatch = rootStopWatch;
        addStopWatch(rootStopWatch);
    }

    public void addStopWatch(StopWatch stopWatch) {
        this.index++;
        if (this.stopWatchArray.size() > 0) {
            this.stopWatchArray.get(index - 1).addSubStopWatch(stopWatch);
        }
        this.stopWatchArray.add(stopWatch);
    }

    /**
     * 同级获取
     *
     * @return
     */
    public StopWatch getStopWatch() {
        return stopWatchArray.get(index);

    }

    /**
     * 子集获取
     *
     * @return
     */
    public StopWatch removeStopWatch() {
        index--;
        return stopWatchArray.remove(index + 1);

    }

    public StopWatch getRootStopWatch() {
        return rootStopWatch;
    }

    public void setRootStopWatch(StopWatch rootStopWatch) {
        this.rootStopWatch = rootStopWatch;
    }
}
