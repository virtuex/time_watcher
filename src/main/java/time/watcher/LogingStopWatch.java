package time.watcher;


/**
 * @author xuda
 * @email xuda.it@outlook.com
 */
public class LogingStopWatch extends StopWatch {
    private static final long serialVersionUID = 1L;

    /**
     *
     * @param id
     *            标识符，标识此记录器
     * @param startWatch
     *            是否开启日志记录器
     * @param filterTime
     *            当开启日志记录器时，时间过滤，小于此时间的不输出，单位为毫秒，小于0表示不过滤
     */
    public LogingStopWatch(String id, boolean startWatch, long filterTime) {
        super(id, startWatch, filterTime);
    }

    @Override
    public String prettyPrint() {
        String prettyPrint = super.prettyPrint();
        log(prettyPrint);
        return prettyPrint;
    }

    /**
     * 日志输出
     *
     * @param prettyPrint
     */
    protected void log(String prettyPrint) {
        if (this.isStartWatch()) {
            System.out.println(prettyPrint);
        }
    }
}
