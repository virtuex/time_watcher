package time.watcher.helper;

/**
 * @author xuda
 * @email xuda.it@outlook.com
 */
public class TimeUtils {

    public final static long ONE_SECOND = 1000;
    public final static long SECONDS = 60;

    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long MINUTES = 60;

    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long HOURS = 24;

    public final static long ONE_DAY = ONE_HOUR * 24;

    private TimeUtils() {
    }

    /**
     * converts time (in milliseconds) to human-readable format "<w> days, <x>
     * hours, <y> minutes and (z) seconds"
     */
    public static String millisToSubLongDHMS(long duration) {
        StringBuffer res = new StringBuffer();
        long temp = 0;
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_DAY;
            if (temp > 0) {
                duration -= temp * ONE_DAY;
                res.append(temp).append("d ").append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_HOUR;
            if (temp > 0) {
                duration -= temp * ONE_HOUR;
                res.append(temp).append("h ").append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                duration -= temp * ONE_MINUTE;
                res.append(temp).append("min ");
            }

            if (!res.toString().equals("") && duration >= ONE_SECOND) {
                res.append(" and ");
            }

            temp = duration / ONE_SECOND;
            if (temp > 0) {
                res.append(temp).append("s ");
            }
            temp = duration % ONE_SECOND;
            if (temp > 0) {
                res.append(temp).append("ms ");
            }
            return res.toString();
        } else {
            return duration + "ms ";
        }
    }

    public static String millisToLongDHMS(long duration) {
        StringBuffer res = new StringBuffer();
        long temp = 0;
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_DAY;
            if (temp > 0) {
                duration -= temp * ONE_DAY;
                res.append(temp).append(" day").append(temp > 1 ? "s" : "").append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_HOUR;
            if (temp > 0) {
                duration -= temp * ONE_HOUR;
                res.append(temp).append(" hour").append(temp > 1 ? "s" : "").append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                duration -= temp * ONE_MINUTE;
                res.append(temp).append(" minute").append(temp > 1 ? "s" : "");
            }

            if (!res.toString().equals("") && duration >= ONE_SECOND) {
                res.append(" and ");
            }

            temp = duration / ONE_SECOND;
            if (temp > 0) {
                res.append(temp).append(" second").append(temp > 1 ? "s " : " ");
            }
            temp = duration % ONE_SECOND;
            if (temp > 0) {
                res.append(temp).append(" ms ");
            }
            return res.toString();
        } else {
            return duration + " ms ";
        }
    }

    /**
     * converts time (in milliseconds) to human-readable format "<dd:>hh:mm:ss"
     */
    public static String millisToShortDHMS(long duration) {
        String res = "";
        duration /= ONE_SECOND;
        int seconds = (int) (duration % SECONDS);
        duration /= SECONDS;
        int minutes = (int) (duration % MINUTES);
        duration /= MINUTES;
        int hours = (int) (duration % HOURS);
        int days = (int) (duration / HOURS);
        if (days == 0) {
            res = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            res = String.format("%dd%02d:%02d:%02d", days, hours, minutes, seconds);
        }
        return res;
    }

    public static void main(String args[]) {
        System.out.println(millisToLongDHMS(1024));
        System.out.println(millisToLongDHMS(123));
        System.out.println(millisToLongDHMS((5 * ONE_SECOND) + 123));
        System.out.println(millisToLongDHMS(ONE_DAY + ONE_HOUR));
        System.out.println(millisToLongDHMS(ONE_DAY + 2 * ONE_SECOND));
        System.out.println(millisToLongDHMS(ONE_DAY + ONE_HOUR + (2 * ONE_MINUTE)));
        System.out.println(millisToLongDHMS((4 * ONE_DAY) + (3 * ONE_HOUR) + (2 * ONE_MINUTE) + ONE_SECOND));
        System.out.println(millisToLongDHMS((5 * ONE_DAY) + (4 * ONE_HOUR) + ONE_MINUTE + (23 * ONE_SECOND) + 123));
        System.out.println(millisToLongDHMS(42 * ONE_DAY));
        /*
         * output : 1 second 24 millisecond ,123 millisecond ,0 second 5 seconds
         * 1 day, 1 hour 1 day and 2 seconds 1 day, 1 hour, 2 minutes 4 days, 3
         * hours, 2 minutes and 1 second 5 days, 4 hours, 1 minute and 23
         * seconds 42 days
         */
        System.out.println(millisToShortDHMS(123));
        System.out.println(millisToShortDHMS((5 * ONE_SECOND) + 123));
        System.out.println(millisToShortDHMS(ONE_DAY + ONE_HOUR));
        System.out.println(millisToShortDHMS(ONE_DAY + 2 * ONE_SECOND));
        System.out.println(millisToShortDHMS(ONE_DAY + ONE_HOUR + (2 * ONE_MINUTE)));
        System.out.println(millisToShortDHMS((4 * ONE_DAY) + (3 * ONE_HOUR) + (2 * ONE_MINUTE) + ONE_SECOND));
        System.out.println(millisToShortDHMS((5 * ONE_DAY) + (4 * ONE_HOUR) + ONE_MINUTE + (23 * ONE_SECOND) + 123));
        System.out.println(millisToShortDHMS(42 * ONE_DAY));
        /*
         * output : 00:00:00 00:00:05 1d01:00:00 1d00:00:02 1d01:02:00
         * 4d03:02:01 5d04:01:23 42d00:00:00
         *
         */
    }
}
