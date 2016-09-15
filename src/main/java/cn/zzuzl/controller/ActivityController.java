package cn.zzuzl.controller;

import cn.zzuzl.common.LoginContext;
import cn.zzuzl.common.annotation.Authorization;
import cn.zzuzl.common.enums.SortDirEnum;
import cn.zzuzl.common.util.StringUtil;
import cn.zzuzl.dao.RedisDao;
import cn.zzuzl.dto.ParameterBean;
import cn.zzuzl.dto.QualityJsonBean;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.*;
import cn.zzuzl.model.query.ProjectQuery;
import cn.zzuzl.model.query.StudentQuery;
import cn.zzuzl.service.ActivityService;
import cn.zzuzl.service.ProjectService;
import cn.zzuzl.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping("activities")
public class ActivityController {
    @Resource
    private ActivityService activityService;
    @Resource
    private RedisDao redisDao;
    @Resource
    private StudentService studentService;
    @Resource
    private ProjectService projectService;
    private Logger logger = LogManager.getLogger(getClass());

    // 添加素质得分
    @Authorization
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Result addActivity(@RequestBody ParameterBean bean) {
        Result result = new Result(true);
        try {
            List<Activity> activityList = StringUtil.json2Activity(bean.getJson());
            result = activityService.addActivities(activityList);
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }

        return result;
    }

    // 获取当前登录人已填写的activity
    @Authorization
    @RequestMapping(value = "/myActivities", method = RequestMethod.GET)
    @ResponseBody
    public Result<Activity> listMyActivities() {
        Result<Activity> result = new Result<Activity>(true);
        try {
            result = activityService.listActivities(
                    LoginContext.getCurrentSchoolNum(),
                    StringUtil.getCurrentYear(),
                    null);
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }

        return result;
    }

    // 获取本专业学生综测列表
    @Authorization
    @RequestMapping(value = "/qualities", method = RequestMethod.GET)
    @ResponseBody
    public Result<QualityJsonBean> listQuality() {
        Result<QualityJsonBean> result = new Result<QualityJsonBean>(true);
        try {
            Student student = LoginContext.getLoginContext().getStudent();
            StudentQuery query = new StudentQuery();
            query.setPage(0);
            query.setPerPage(0);
            query.setMajorCode(student.getClassCode().substring(0, 4));
            query.setGrade(student.getGrade());
            query.setSortField("schoolNum");
            query.setSortDir(SortDirEnum.ASC.getTitle());

            Result<Student> studentResult = studentService.searchStudent(query);
            List<QualityJsonBean> list = new ArrayList<QualityJsonBean>();
            Set<String> firstSet = new HashSet<String>();
            Set<String> secondSet = new HashSet<String>();
            if (result.isSuccess()) {
                for (Student s : studentResult.getList()) {
                    List<TermScore> termScoreList = redisDao.getScores(s.getSchoolNum());
                    // 取出所有的课程名称
                    if (termScoreList != null) {
                        if (termScoreList.size() > 0) {
                            for (ScoreVO scoreVO : termScoreList.get(0).getScores()) {
                                firstSet.add(scoreVO.getTitle());
                            }
                        }
                        if (termScoreList.size() > 1) {
                            for (ScoreVO scoreVO : termScoreList.get(1).getScores()) {
                                secondSet.add(scoreVO.getTitle());
                            }
                        }
                    }
                    QualityJsonBean bean = new QualityJsonBean();
                    bean.setSchoolNum(s.getSchoolNum());
                    bean.setName(s.getName());
                    bean.setList(termScoreList);
                    list.add(bean);
                }
            }
            result.getData().put("firstSet", firstSet);
            result.getData().put("secondSet", secondSet);
            result.setList(list);
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }

        return result;
    }

    // 获取本专业已填写的activity
    @Authorization
    @RequestMapping(value = "/majorActivities", method = RequestMethod.GET)
    @ResponseBody
    public Result<Activity> listMajorActivities() {
        Result<Activity> result = new Result<Activity>(true);
        try {
            result = activityService.listActivities(
                    LoginContext.getCurrentSchoolNum(),
                    StringUtil.getCurrentYear(),
                    LoginContext.getLoginContext().getStudent().getClassCode().substring(0, 4));
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }

        return result;
    }
}
