package cn.zzuzl.controller;

import cn.zzuzl.common.Constants;
import cn.zzuzl.common.LoginContext;
import cn.zzuzl.common.annotation.Authorization;
import cn.zzuzl.common.enums.VacationEnum;
import cn.zzuzl.common.util.ExcelExportUtil;
import cn.zzuzl.common.util.NetUtil;
import cn.zzuzl.common.util.StringUtil;
import cn.zzuzl.dao.RedisDao;
import cn.zzuzl.dto.LoginRecordVO;
import cn.zzuzl.dto.QualityJsonBean;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.*;
import cn.zzuzl.model.query.ProjectQuery;
import cn.zzuzl.model.query.StudentQuery;
import cn.zzuzl.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("students")
public class StudentController {
    @Resource
    private StudentService studentService;
    @Resource
    private NetUtil netUtil;
    @Resource
    private RedisDao redisDao;
    @Resource
    private ExcelExportUtil excelExportUtil;
    private Logger logger = LogManager.getLogger(getClass());

    // 学生登录
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
                    session.setAttribute(Constants.PASS, password);
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

    // 退出
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public Result logout(HttpSession session) {
        Result result = new Result(true);
        session.removeAttribute(Constants.PASS);
        session.removeAttribute(Constants.RESOURCES);
        session.removeAttribute(Constants.STU);
        return result;
    }

    // 查看个人信息
    @Authorization
    @RequestMapping(value = "/myInfo", method = RequestMethod.GET)
    @ResponseBody
    public Student myInfo() {
        return studentService.searchBySchoolNum(LoginContext.getCurrentSchoolNum());
    }

    //修改个人信息
    @Authorization
    @RequestMapping(value = "/modifyMyInfo", method = RequestMethod.PUT, consumes = "application/json")
    @ResponseBody
    public Result modifyMyInfo(@RequestBody Student student) {
        Result result = new Result(true);
        try {
            if (!LoginContext.getCurrentSchoolNum().equals(student.getSchoolNum())) {
                result.setSuccess(false);
                result.setError("信息错误");
            } else {
                result = studentService.updateStudent(student);
            }
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }
        return result;
    }

    // 获取指定最近n天的登录记录
    @Authorization
    @RequestMapping(value = "/loginRecords/{n}", method = RequestMethod.GET)
    @ResponseBody
    public Result<LoginRecordVO> getLoginRecords(@PathVariable("n") int n) {
        Result<LoginRecordVO> result = new Result<LoginRecordVO>(true);
        // 最多查3个月的
        if (n > 0 && n <= 90) {
            result = studentService.searchLoginRecord(n);
        }
        return result;
    }

    // 查询
    @Authorization({Constants.AUTH_STU_MANAGE})
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Result listStudent(StudentQuery query) {
        Result result = new Result(true);
        try {
            query.setClassCode(LoginContext.getLoginContext().getStudent().getClassCode());
            query.setGrade(LoginContext.getLoginContext().getStudent().getGrade());
            result = studentService.searchStudent(query);
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }
        return result;
    }

    // 查看详细
    @Authorization({Constants.AUTH_STU_MANAGE})
    @RequestMapping(value = "/{schoolNum}", method = RequestMethod.GET)
    @ResponseBody
    public Student getById(@PathVariable("schoolNum") String schoolNum) {
        return studentService.searchBySchoolNum(schoolNum);
    }

    // 修改
    @Authorization({Constants.AUTH_STU_MANAGE})
    @RequestMapping(value = "/{schoolNum}", method = RequestMethod.PUT, consumes = "application/json")
    @ResponseBody
    public Result updateStudent(@PathVariable("schoolNum") String schoolNum, @RequestBody Student student) {
        Result result = new Result(true);
        try {
            if (!schoolNum.equals(student.getSchoolNum())) {
                result.setSuccess(false);
                result.setError("数据不一致");
            } else {
                result = studentService.updateStudent(student);
            }
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }
        return result;
    }

    // 删除
    @Authorization({Constants.AUTH_STU_MANAGE})
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

    // 查询登录人的成绩单
    @Authorization
    @RequestMapping(value = "/myScore", method = RequestMethod.GET)
    @ResponseBody
    public Result myScore() {
        Result result = new Result(true);
        try {
            result = netUtil.searchScore(LoginContext.getLoginContext().getStudent().getSchoolNum(),
                    LoginContext.getLoginContext().getPassword());
            if (result.isSuccess()) {
                // 刷新成绩单到redis
                redisDao.updateScore(LoginContext.getCurrentSchoolNum(), result.getData().get("scores"));
            }
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }
        return result;
    }

    // 导出学生列表excel
    @Authorization({Constants.AUTH_STU_MANAGE})
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public ModelAndView export(HttpServletResponse response) {
        StudentQuery query = new StudentQuery();
        query.setClassCode(LoginContext.getLoginContext().getStudent().getClassCode());
        query.setGrade(LoginContext.getLoginContext().getStudent().getGrade());

        List<Student> list = studentService.export(query);
        HSSFWorkbook workbook = excelExportUtil.genStudentXls(list, response);
        return new ModelAndView("excelView", "workbook", workbook);
    }

    // 根据学号，年份，假期查询回家信息
    @Authorization
    @RequestMapping(value = "/goHome/me", method = RequestMethod.GET)
    @ResponseBody
    public GoHome myGoHome() {
        return studentService.searchOneGoHome(
                LoginContext.getCurrentSchoolNum(),
                StringUtil.getCurrentYear(),
                VacationEnum.vacation(new Date()).getTitle()
        );
    }

    // 创建或修改回家信息
    @Authorization
    @RequestMapping(value = "/goHome", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Result addGoHome(@RequestBody GoHome goHome) {
        Result result = new Result(true);
        try {
            goHome.setStudent(LoginContext.getLoginContext().getStudent());
            goHome.setYear(StringUtil.getCurrentYear());
            goHome.setVacation(VacationEnum.vacation(new Date()).getTitle());
            goHome.setOperator(LoginContext.getLoginContext().getStudent());
            result = studentService.addGoHome(goHome);
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError(e.getMessage());
        }
        return result;
    }

    // 寒暑假回家列表
    @Authorization({Constants.AUTH_GH_MANAGE})
    @RequestMapping(value = "goHomeList", method = RequestMethod.GET)
    @ResponseBody
    public List<GoHome> listGoHome() {
        List<GoHome> list = null;
        try {
            Student student = LoginContext.getLoginContext().getStudent();
            list = studentService.searchGoHome(student.getClassCode(), StringUtil.getCurrentYear(), VacationEnum.vacation(new Date()).getTitle());
        } catch (Exception e) {
            logger.error(e);
        }
        return list;
    }

    // 导出寒暑假回家列表excel
    @Authorization({Constants.AUTH_GH_MANAGE})
    @RequestMapping(value = "/goHome/export", method = RequestMethod.GET)
    public ModelAndView goHomeExport(HttpServletResponse response) {
        List<GoHome> list = listGoHome();
        HSSFWorkbook workbook = excelExportUtil.genGoHomeXls(list, response);
        return new ModelAndView("excelView", "workbook", workbook);
    }
}
