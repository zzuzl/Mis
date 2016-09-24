package cn.zzuzl.service.impl;

import cn.zzuzl.common.LoginContext;
import cn.zzuzl.common.util.StringUtil;
import cn.zzuzl.dao.ActivityDao;
import cn.zzuzl.dao.ProjectDao;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.Activity;
import cn.zzuzl.model.Item;
import cn.zzuzl.model.query.ItemQuery;
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
    @Resource
    private ProjectDao projectDao;

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

        ItemQuery query = new ItemQuery();
        query.setGrade(LoginContext.getLoginContext().getStudent().getGrade());
        query.setMajorCode(LoginContext.getLoginContext().getStudent().majorCode());
        query.setYear(StringUtil.getCurrentYear());
        List<Item> items = projectDao.searchItems(query);

        List<Integer> itemIds = null;
        if (items != null) {
            itemIds = new ArrayList<Integer>();
            for (Item item : items) {
                itemIds.add(item.getId());
            }
            // delete other activity
            activityDao.updateInvalid(ids, LoginContext.getCurrentSchoolNum(), StringUtil.getCurrentYear(), itemIds);
        }

        return result;
    }

    public Result<Activity> listActivities(String schoolNum, Integer year, String majorCode, Integer itemId) {
        Result<Activity> result = new Result<Activity>(true);
        List<Activity> activityList = activityDao.listActivities(schoolNum, year, null, itemId);
        result.setList(activityList);
        result.setTotalItem(activityList.size());
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<Activity> manageActivities(List<Activity> activityList, String schoolNum, Integer itemId) {
        if (itemId == null) {
            throw new RuntimeException("小项目不存在");
        }

        Result<Activity> result = new Result<Activity>(true);

        List<Long> ids = new ArrayList<Long>();
        for (Activity activity : activityList) {
            if (activity.getItem() == null || activity.getItem().getId() == null) {
                activityList.remove(activity);
            } else {
                ids.add(activity.getId());
            }
        }
        activityDao.batchInsert(activityList);

        List<Integer> itemIds = new ArrayList<Integer>();
        itemIds.add(itemId);
        // delete other activity
        activityDao.updateInvalid(ids, schoolNum, StringUtil.getCurrentYear(), itemIds);

        return result;
    }
}
