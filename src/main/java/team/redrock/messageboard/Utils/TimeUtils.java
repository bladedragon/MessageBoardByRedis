package team.redrock.messageboard.Utils;

public class TimeUtils {               //计算时间
    public String getCurrentTime() {
        long totalMilliSeconds = System.currentTimeMillis();
        long totalSeconds = totalMilliSeconds / 1000;
        long currentSeconds = totalSeconds % 60;

        long totalMinutes = totalSeconds / 60;
        long currentMinute = totalMinutes % 60;

        long totalHour = totalMinutes / 60;
        long currentHour = totalHour % 24;
        return Long.toString(currentHour) + ":" + Long.toString(currentMinute) + ":" + Long.toString(currentSeconds);
    }


    public static void main(String[] args) {
        TimeUtils timeUtils = new TimeUtils();
        System.out.println(timeUtils.getCurrentTime());
    }
}

