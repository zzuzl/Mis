package cn.zzuzl.common.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * Created by Administrator on 2016/10/4.
 */
public class DateUtil {
    public String format(Date date,String pattern) {
        return DateFormatUtils.format(date, pattern);
    }
}
