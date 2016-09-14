package cn.zzuzl.controller;

import cn.zzuzl.common.LoginContext;
import cn.zzuzl.common.annotation.Authorization;
import cn.zzuzl.common.util.StringUtil;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.Project;
import cn.zzuzl.model.Student;
import cn.zzuzl.model.query.ProjectQuery;
import cn.zzuzl.service.ProjectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    public Result listProject(ProjectQuery query) {
        Result result = new Result(true);
        try {
            Student student = LoginContext.getLoginContext().getStudent();
            query.setMajorCode(student.getClassCode().substring(0, 4));
            query.setGrade(student.getGrade());
            query.setYear(StringUtil.getCurrentYear());
            result = projectService.searchProject(query);
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }
        return result;
    }

    // 查询(带有子项目)
    @Authorization
    @RequestMapping(value = "/items", method = RequestMethod.GET)
    @ResponseBody
    public Result listProjectWithItems(ProjectQuery query) {
        Result result = new Result(true);
        try {
            Student student = LoginContext.getLoginContext().getStudent();
            query.setMajorCode(student.getClassCode().substring(0, 4));
            query.setGrade(student.getGrade());
            query.setYear(StringUtil.getCurrentYear());
            result = projectService.searchProjectWithItems(query);
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
    public Result addProject(@RequestBody Project project) {
        Result result = new Result(true);
        try {
            Student student = LoginContext.getLoginContext().getStudent();
            project.setMajorCode(student.getClassCode().substring(0, 4));
            project.setGrade(student.getGrade());
            project.setYear(StringUtil.getCurrentYear());
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
    public Result updateProject(@PathVariable("id") Integer id, @RequestBody Project project) {
        Result result = new Result(true);
        try {
            if (!id.equals(project.getId())) {
                result.setSuccess(false);
                result.setError("数据不一致");
            } else {
                Student student = LoginContext.getLoginContext().getStudent();
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
    public Result deleteProject(@PathVariable("id") Integer id) {
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
