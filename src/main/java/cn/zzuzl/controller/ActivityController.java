package cn.zzuzl.controller;

import cn.zzuzl.common.util.StringUtil;
import cn.zzuzl.dto.ParameterBean;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.Activity;
import cn.zzuzl.service.ActivityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("activities")
public class ActivityController {
    @Resource
    private ActivityService activityService;
    private Logger logger = LogManager.getLogger(getClass());

    // 添加素质得分
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Result addActivity(@RequestBody ParameterBean bean) {
        Result result = new Result(true);
        try {
            List<Activity> activityList = StringUtil.parseActivities(bean.getJson());
            result = activityService.addActivities(activityList);
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }

        return result;
    }
}
