package time.watcher;

import java.io.Serializable;

/**
 * @author xuda
 * @email xuda.it@outlook.com
 */
public class TaskInfo implements Serializable {
    private final String taskName;// 任务名称

    private final long startTimeMillis;// 任务开始时间

    private long timeMillis;// 任务耗时时间

    /**
     *
     * @param taskName
     *            任务名称
     * @param startTimeMillis
     *            任务开始时间
     */
    TaskInfo(String taskName, long startTimeMillis) {
        this.taskName = taskName;
        this.startTimeMillis = startTimeMillis;
    }

    /**
     * Return the name of this task.
     */
    public String getTaskName() {
        return this.taskName;
    }

    /**
     * Return the Start time of the current task
     *
     * @return
     */
    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    /**
     * Return the time in milliseconds this task took.
     */
    public long getTimeMillis() {
        return this.timeMillis;
    }

    /**
     * Set the time in milliseconds this task took
     *
     * @param timeMillis
     */
    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }

    /**
     * Return the time in seconds this task took.
     */
    public double getTimeSeconds() {
        return (this.timeMillis / 1000.0);
    }
}
