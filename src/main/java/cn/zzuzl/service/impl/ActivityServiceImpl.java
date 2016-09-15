package cn.zzuzl.service.impl;

import cn.zzuzl.common.LoginContext;
import cn.zzuzl.common.util.StringUtil;
import cn.zzuzl.dao.ActivityDao;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.Activity;
import cn.zzuzl.service.ActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private ActivityDao activityDao;

    @Transactional(rollbackFor = Exception.class)
    public Result addActivities(List<Activity> activityList) {
        Result result = new Result(true);

        List<Long> ids = new ArrayList<Long>();
        for (Activity activity : activityList) {
            activity.setStudent(LoginContext.getLoginContext().getStudent());
            if (activity.getItem() == null || activity.getItem().getId() == null) {
                activityList.remove(activity);
            } else {
                ids.add(activity.getId());
            }
        }
        activityDao.batchInsert(activityList);

        // delete other activity
        activityDao.updateInvalid(ids, LoginContext.getCurrentSchoolNum(), StringUtil.getCurrentYear());
        return result;
    }

    public Result<Activity> listActivities(String schoolNum, Integer year, String majorCode) {
        Result<Activity> result = new Result<Activity>(true);
        List<Activity> activityList = activityDao.listActivities(schoolNum, year, null);
        result.setList(activityList);
        result.setTotalItem(activityList.size());
        return result;
    }
}
