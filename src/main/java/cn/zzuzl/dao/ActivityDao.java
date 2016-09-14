package cn.zzuzl.dao;

import cn.zzuzl.model.Activity;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ActivityDao {
    int batchInsert(@Param("activityList") List<Activity> activityList);

    int insert(Activity activity);

    List<Activity> listActivities(@Param("schoolNum") String schoolNum,
                                  @Param("year") Integer year);

    int updateInvalid(@Param("ids") List<Long> ids,
                      @Param("schoolNum") String schoolNum,
                      @Param("year") Integer year);
}
