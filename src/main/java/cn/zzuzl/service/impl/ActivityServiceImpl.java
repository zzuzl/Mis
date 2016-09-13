package cn.zzuzl.service.impl;

import cn.zzuzl.dao.ActivityDao;
import cn.zzuzl.dto.Result;
import cn.zzuzl.service.ActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private ActivityDao activityDao;

    public Result addActivities() {

        return null;
    }
}
