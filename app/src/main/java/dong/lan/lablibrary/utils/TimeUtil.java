package dong.lan.lablibrary.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * 时间转换工具
 */
public class TimeUtil {


    public final static long DAY_IN_MILI = 24 * 60 * 60 * 1000;

    private TimeUtil(){}



    public static String getTime(long time,String patter){

        SimpleDateFormat sdf1 = new SimpleDateFormat(patter, Locale.CHINA);
        Date date1  = new Date(time);
        return sdf1.format(date1);
    }

    public static int getTimeDays(long startTime, long endTime) {
        long time = endTime - startTime;
        if(time <= 0)
            return 0;
        int d = (int) (time / DAY_IN_MILI);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);

        int d1 = calendar.get(Calendar.DATE);
        calendar.setTimeInMillis(endTime);
        int d2 = calendar.get(Calendar.DATE);
        if(d1 == d2)
            return 1;
        return d + 2;
    }
}
