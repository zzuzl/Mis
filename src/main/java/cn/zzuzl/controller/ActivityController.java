package cn.zzuzl.controller;

import cn.zzuzl.common.annotation.Authorization;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.Activity;
import cn.zzuzl.service.ActivityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("activities")
public class ActivityController {
    @Resource
    private ActivityService activityService;
    private Logger logger = LogManager.getLogger(getClass());

    // 添加素质得分
    @Authorization
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Result addActivity(String json) {
        logger.info(json);
        Result result = new Result(true);
        try {
            // result = activityService.addActivities();
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }

        return result;
    }
}
