package cn.zzuzl.common.util;

import cn.zzuzl.model.Activity;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016/9/11.
 */
public class StringUtil {

    // json转换为activity的数组
    public static List<Activity> json2Activity(String json) throws IOException {
        List<Activity> activityList = new ArrayList<Activity>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Activity[] activities = objectMapper.readValue(json, Activity[].class);
        activityList = Arrays.asList(activities);
        return activityList;
    }

    // 获取当前年份
    public static Integer getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
}
