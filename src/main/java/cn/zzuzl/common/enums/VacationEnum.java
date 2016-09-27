package cn.zzuzl.common.enums;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/27.
 */
public enum VacationEnum {
    WINTER_VACATION("寒假"),
    SUMMER_VACATION("暑假");

    private String title;

    private VacationEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static VacationEnum vacation(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        if (month > 1 && month < 7) {
            return SUMMER_VACATION;
        } else {
            return WINTER_VACATION;
        }
    }
}
