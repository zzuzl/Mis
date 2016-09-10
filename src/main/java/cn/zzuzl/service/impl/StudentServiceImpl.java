package cn.zzuzl.service.impl;

import cn.zzuzl.common.util.NetUtil;
import cn.zzuzl.dao.StudentDao;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.LoginRecord;
import cn.zzuzl.model.Student;
import cn.zzuzl.model.query.StudentQuery;
import cn.zzuzl.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/9/10.
 */
@Service("studentService")
public class StudentServiceImpl implements StudentService {
    @Resource
    private StudentDao studentDao;
    @Resource
    private NetUtil netUtil;

    public Student searchBySchoolNum(String schoolNum) {
        return studentDao.searchBySchoolNum(schoolNum);
    }

    public Result login(String schoolNum, String password) throws Exception {
        return netUtil.connect("http://jw.zzu.edu.cn/scripts/stuinfo.dll/check",
                schoolNum, password, "input[name='userid']");
    }

    public Result insert(Student student) {
        Result result = new Result(true);
        if (studentDao.insertStudent(student) < 1) {
            result.setSuccess(false);
            result.setError("添加失败");
        }
        return result;
    }

    public Result<Student> searchStudent(StudentQuery query) {
        Result<Student> result = new Result<Student>(query.getPage(), query.getPerPage());
        query.adjust();
        result.setList(studentDao.searchStudent(query));
        result.setTotalItem(studentDao.getStudentCount(query));
        return result;
    }

    public void insertLoginRecord(LoginRecord record) {
        studentDao.insertLoginRecord(record);
    }

    public Result updateInvalid(String schoolNum) {
        Result result = new Result(true);
        if(studentDao.updateInvalid(schoolNum) < 1) {
            result.setSuccess(false);
            result.setError("删除失败");
        }
        return result;
    }
}
