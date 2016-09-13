package cn.zzuzl.service;

import cn.zzuzl.dto.Result;
import cn.zzuzl.model.Activity;

import java.util.List;

public interface ActivityService {
    Result addActivities(List<Activity> activities);
}
