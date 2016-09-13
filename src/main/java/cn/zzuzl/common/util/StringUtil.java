package cn.zzuzl.common.util;

import cn.zzuzl.model.Activity;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/9/11.
 */
public final class StringUtil {

    // 把json解析成数组
    public static List<Activity> parseActivities(String json) throws Exception {
        List<Activity> activityList = new ArrayList<Activity>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        Activity[] activities = objectMapper.readValue(json, Activity[].class);
        if (activities != null) {
            activityList = Arrays.asList(activities);
        }
        return activityList;
    }
}
