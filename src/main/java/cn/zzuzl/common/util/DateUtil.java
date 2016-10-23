package cn.zzuzl.common.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/4.
 */
public class DateUtil {
    public String format(Date date,String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    public static boolean isBirthday(Date birthday) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthday);

        Calendar now = Calendar.getInstance();

        if(calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
                calendar.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            System.out.println(isBirthday(sdf.parse("1994-10-23")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
