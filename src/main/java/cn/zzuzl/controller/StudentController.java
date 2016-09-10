package cn.zzuzl.controller;

import cn.zzuzl.common.Constants;
import cn.zzuzl.common.util.NetUtil;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.LoginRecord;
import cn.zzuzl.model.Student;
import cn.zzuzl.model.query.StudentQuery;
import cn.zzuzl.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("students")
public class StudentController {
    @Resource
    private StudentService studentService;
    @Resource
    private NetUtil netUtil;
    private Logger logger = LogManager.getLogger(getClass());

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Result login(String schoolNum, String password, HttpSession session, HttpServletRequest request) {
        Result result = new Result(true);
        if (StringUtils.isEmpty(schoolNum) || StringUtils.isEmpty(password)) {
            result.setSuccess(false);
            result.setError("用户名或密码为空");
        } else {
            try {
                result = studentService.login(schoolNum, password);
                if (result.isSuccess()) {
                    Student student = (Student) result.getData().get(Constants.STU);
                    session.setAttribute(Constants.STU, student);
                    // 如果第一次登陆，则添加到数据库
                    if (studentService.searchBySchoolNum(schoolNum) == null) {
                        result = studentService.insert(student);
                    }
                    // 获取ip，插入登录记录
                    String ip = netUtil.getRemoteIP(request);
                    String location = netUtil.getRemoteLocation(ip);
                    LoginRecord record = new LoginRecord(schoolNum, ip, location);
                    studentService.insertLoginRecord(record);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setSuccess(false);
                result.setError(e.getMessage());
            }
        }

        return result;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Result listStudent(StudentQuery query) {
        Result result = new Result(true);
        try {
            result = studentService.searchStudent(query);
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{schoolNum}", method = RequestMethod.GET)
    @ResponseBody
    public Student getById(@PathVariable("schoolNum") String schoolNum) {
        return studentService.searchBySchoolNum(schoolNum);
    }

    @RequestMapping(value = "/{schoolNum}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result deleteStudent(@PathVariable("schoolNum") String schoolNum) {
        Result result = new Result(true);
        try {
            result = studentService.updateInvalid(schoolNum);
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }
        return result;
    }
}
