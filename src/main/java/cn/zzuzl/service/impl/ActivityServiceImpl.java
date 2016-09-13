package cn.zzuzl.service.impl;

import cn.zzuzl.common.LoginContext;
import cn.zzuzl.dao.ActivityDao;
import cn.zzuzl.dao.ProjectDao;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.Activity;
import cn.zzuzl.model.Item;
import cn.zzuzl.service.ActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private ActivityDao activityDao;
    @Resource
    private ProjectDao projectDao;

    public Result addActivities(List<Activity> activities) {
        Result result = new Result(true);
        for(Activity activity : activities) {
            Item item = activity.getItem();
            if(item == null || item.getId() == null) {
                // item = projectDao.getItemById(item.getId());
                activities.remove(activity);
            } else {
                activity.setOperator(LoginContext.getCurrentSchoolNum());
            }
        }

        activityDao.batchInsert(activities);
        return result;
    }
}
