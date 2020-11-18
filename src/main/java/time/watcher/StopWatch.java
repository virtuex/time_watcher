package time.watcher;

import time.watcher.helper.TimeUtils;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xuda
 * @email xuda.it@outlook.com
 */
public class StopWatch implements Serializable {

    private static final long serialVersionUID = 1854486022524688606L;

    /**
     * Identifier of this stop watch. Handy when we have output from multiple
     * stop watches and need to distinguish between them in log or console
     * output.
     */
    private final String id;

    /**
     * 平级的监听信息
     */
    private final List<TaskInfo> taskList = new LinkedList<TaskInfo>();

    /**
     * 嵌套监听
     */
    private final List<StopWatch> subStopWatch = new ArrayList<StopWatch>();

    /**
     * 父监听，作用于嵌套环节
     */
    private StopWatch parentStopWatch = null;

    private int taskIndex = 0;

    private int taskCount;

    /** Total running time */
    private long totalTimeMillis = 0;

    /**
     * 是否开启监听
     */
    private boolean startWatch = false;

    /**
     * 过滤时间，如果为负数标识为不过滤，单位为毫秒
     */
    private long filterTime = -1;

    private String printTab = "  ";

    private boolean isStart = false;

    /**
     * Construct a new stop watch. Does not start any task.
     */
    public StopWatch() {
        this("", true, -1);
    }

    /**
     * Construct a new stop watch with the given id. Does not start any task.
     *
     * @param id
     *            identifier for this stop watch. Handy when we have output from
     *            multiple stop watches and need to distinguish between them.
     * @param startWatch
     *            是否开启日志记录器
     * @param filterTime
     *            记录过滤时间，单位为毫秒，当少于这个数量的则不打印，如果为0或者负数则表示不过滤
     */
    public StopWatch(String id, boolean startWatch, long filterTime) {
        this.id = id;
        this.startWatch = startWatch;
        this.filterTime = filterTime;
    }

    /**
     * Return the id of this stop watch, as specified on construction.
     *
     * @return the id (empty String by default)
     * @since 4.2.2
     * @see
     */
    public String getId() {
        return this.id;
    }

    /**
     * Start an unnamed task. The results are undefined if {@link #stop()} or
     * timing methods are called without invoking this method.
     *
     * @see #stop()
     */
    public void start() throws IllegalStateException {
        start("");
    }

    /**
     * Start a named task. The results are undefined if {@link #stop()} or
     * timing methods are called without invoking this method.
     *
     * @param taskName
     *            the name of the task to start
     * @see #stop()
     */
    public void start(String taskName) {
        if (!startWatch) {
            return;
        }
        TaskInfo taskInfo = new TaskInfo(taskName, getCurTimeMillis());
        taskList.add(taskInfo);
        taskIndex++;
        isStart = true;

    }

    /**
     * Stop the current task. The results are undefined if timing methods are
     * called without invoking at least one pair {@code start()} /
     * {@code stop()} methods.
     *
     * @see #start()
     */
    public void stop() {
        if (!startWatch) {
            return;
        }
        TaskInfo taskInfo = taskList.get(taskIndex - 1);
        long lastTime = getCurTimeMillis() - taskInfo.getStartTimeMillis();
        this.totalTimeMillis += lastTime;
        taskInfo.setTimeMillis(lastTime);
        ++this.taskCount;
        isStart = false;
    }

    /**
     * Return whether the stop watch is currently running.
     *
     * @see #currentTaskName()
     */
    public boolean isRunning() {
        return (taskIndex != 0);
    }

    /**
     * Return the name of the currently running task, if any.
     *
     * @since 4.2.2
     * @see #isRunning()
     */
    public String currentTaskName() {
        if (isRunning()) {
            TaskInfo taskInfo = taskList.get(taskIndex - 1);
            return taskInfo.getTaskName();
        }
        return null;
    }

    /**
     * Return the total time in milliseconds for all tasks.
     */
    public long getTotalTimeMillis() {
        return this.totalTimeMillis;
    }

    /**
     * Return the total time in seconds for all tasks.
     */
    public double getTotalTimeSeconds() {
        return this.totalTimeMillis / 1000.0;
    }

    /**
     * Return the number of tasks timed.
     */
    public int getTaskCount() {
        return this.taskCount;
    }

    /**
     * Return an array of the data for tasks performed.
     */
    public List<TaskInfo> getTaskInfo() {
        return this.taskList;
    }

    /**
     * Return a short description of the total running time.
     */
    public String shortSummary() {
        return "Task Name '" + getId() + "': Running Time = " + TimeUtils.millisToSubLongDHMS(getTotalTimeMillis());
    }

    /**
     * Return a string with a table describing all tasks performed. For custom
     * reporting, call getTaskInfo() and use the task info directly.
     */
    public String prettyPrint() {
        if (!startWatch) {
            return "";
        }
        if (filterTime > 0) {
            //
            if (getTotalTimeMillis() < filterTime) {
                taskList.clear();
                subStopWatch.clear();
                return "";
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.printTab).append(shortSummary()).append('\n');
        if (this.taskList.isEmpty()) {
            sb.append("No task info kept");
        } else {
            sb.append(this.printTab).append("-----------------------------------------\n");
            sb.append(this.printTab).append("time        %     Task name\n");
            sb.append(this.printTab).append("-----------------------------------------\n");
            // NumberFormat nf = NumberFormat.getNumberInstance();
            // nf.setMinimumIntegerDigits(5);
            // nf.setGroupingUsed(false);
            NumberFormat pf = NumberFormat.getPercentInstance();
            pf.setMinimumIntegerDigits(3);
            pf.setGroupingUsed(false);
            int nsLen = 0;
            for (TaskInfo task : getTaskInfo()) {
                String timeStr = TimeUtils.millisToSubLongDHMS(task.getTimeMillis());
                int timeStrLen = 0;
                if (timeStr != null) {
                    timeStrLen = timeStr.length();
                }
                if (timeStrLen > nsLen) {
                    nsLen = timeStrLen;
                }
                String fillStr = "";
                int fillLen = nsLen - timeStrLen;
                if (fillLen > 0) {
                    char[] chars = new char[fillLen];
                    java.util.Arrays.fill(chars, 0, fillLen, ' ');
                    fillStr = new String(chars);
                }
                sb.append(this.printTab);
                sb.append(timeStr).append(fillStr).append("  ");
                sb.append(pf.format(task.getTimeSeconds() / getTotalTimeSeconds())).append("  ");
                sb.append(task.getTaskName()).append("\n");
            }

            for (StopWatch stopWatch : this.subStopWatch) {
                sb.append(stopWatch.prettyPrint());
            }

        }
        return sb.toString();
    }

    /**
     * Return an informative string describing all tasks performed For custom
     * reporting, call {@code getTaskInfo()} and use the task info directly.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(shortSummary());
        if (this.taskList.isEmpty()) {
            sb.append("; no task info kept");
        } else {
            for (TaskInfo task : getTaskInfo()) {
                sb.append("; [").append(task.getTaskName()).append("] took ").append(task.getTimeMillis());
                long percent = Math.round((100.0 * task.getTimeSeconds()) / getTotalTimeSeconds());
                sb.append(" = ").append(percent).append("%");
            }
        }
        return sb.toString();
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    public long getCurTimeMillis() {
        return System.currentTimeMillis();
    }

    public boolean isStartWatch() {
        return startWatch;
    }

    public List<StopWatch> getSubStopWatch() {
        return subStopWatch;
    }

    /**
     * 添加嵌套监听器
     *
     * @param stopWatch
     */
    public void addSubStopWatch(StopWatch stopWatch) {
        if (stopWatch == null) {
            return;
        }
        stopWatch.setParentStopWatch(this);
        stopWatch.setPrintTab(stopWatch.getPrintTab() + this.getPrintTab());
        this.subStopWatch.add(stopWatch);
    }

    public StopWatch getParentStopWatch() {
        return parentStopWatch;
    }

    public void setParentStopWatch(StopWatch parentStopWatch) {
        this.parentStopWatch = parentStopWatch;
    }

    public String getPrintTab() {
        return printTab;
    }

    public void setPrintTab(String printTab) {
        this.printTab = printTab;
    }

    public long getFilterTime() {
        return filterTime;
    }

    public boolean isStart() {
        return isStart;
    }

}
