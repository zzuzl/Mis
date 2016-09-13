package cn.zzuzl.dao;

import cn.zzuzl.model.Activity;
import java.util.List;

public interface ActivityDao {
    int batchInsert(List<Activity> activityList);

    int insert(Activity activity);

}
