package cn.zzuzl.controller;

import cn.zzuzl.common.Constants;
import cn.zzuzl.common.annotation.Authorization;
import cn.zzuzl.common.util.NetUtil;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.LoginRecord;
import cn.zzuzl.model.Project;
import cn.zzuzl.model.Student;
import cn.zzuzl.model.query.BaseQuery;
import cn.zzuzl.model.query.ProjectQuery;
import cn.zzuzl.model.query.StudentQuery;
import cn.zzuzl.service.ProjectService;
import cn.zzuzl.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.security.auth.login.LoginContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Calendar;

@Controller
@RequestMapping("projects")
public class ProjectController {
    @Resource
    private ProjectService projectService;
    private Logger logger = LogManager.getLogger(getClass());

    // 查询
    @Authorization
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Result listProject(ProjectQuery query, HttpSession session) {
        Result result = new Result(true);
        try {
            Student student = (Student) session.getAttribute(Constants.STU);
            query.setMajorCode(student.getClassCode().substring(0, 4));
            query.setGrade(student.getGrade());
            query.setYear(Calendar.getInstance().get(Calendar.YEAR));
            // todo 改为 LoginContext,spring securate
            result = projectService.searchProject(query);
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }
        return result;
    }

    // 查看详细
    @Authorization
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Project getById(@PathVariable("id") Integer id) {
        return projectService.getById(id);
    }

    // 新建
    @Authorization
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Result updateStudent(@RequestBody Project project, HttpSession session) {
        Result result = new Result(true);
        try {
            Student student = (Student) session.getAttribute(Constants.STU);
            project.setMajorCode(student.getClassCode().substring(0, 4));
            project.setGrade(student.getGrade());
            project.setYear(Calendar.getInstance().get(Calendar.YEAR));
            project.setOperator(student.getSchoolNum());
            result = projectService.insertProject(project);
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }
        return result;
    }

    // 修改
    @Authorization
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    @ResponseBody
    public Result updateStudent(@PathVariable("id") Integer id, @RequestBody Project project, HttpSession session) {
        Result result = new Result(true);
        try {
            if (!id.equals(project.getId())) {
                result.setSuccess(false);
                result.setError("数据不一致");
            } else {
                Student student = (Student) session.getAttribute(Constants.STU);
                project.setOperator(student.getSchoolNum());
                result = projectService.updateProject(project);
            }
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }
        return result;
    }

    // 删除
    @Authorization
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result deleteStudent(@PathVariable("id") Integer id) {
        Result result = new Result(true);
        try {
            result = projectService.updateInvalid(id);
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }
        return result;
    }
}
