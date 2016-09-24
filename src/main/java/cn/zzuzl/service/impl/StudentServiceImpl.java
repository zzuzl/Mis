package cn.zzuzl.service.impl;

import cn.zzuzl.common.util.NetUtil;
import cn.zzuzl.dao.StudentDao;
import cn.zzuzl.dto.LoginRecordVO;
import cn.zzuzl.dto.RecordCountVO;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.LoginRecord;
import cn.zzuzl.model.Student;
import cn.zzuzl.model.query.StudentQuery;
import cn.zzuzl.service.StudentService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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

    public Result updateStudent(Student student) {
        Result result = new Result(true);
        if (studentDao.updateStudent(student) < 1) {
            result.setSuccess(false);
            result.setError("修改失败");
        }
        return result;
    }

    public Result updateInvalid(String schoolNum) {
        Result result = new Result(true);
        if (studentDao.updateInvalid(schoolNum) < 1) {
            result.setSuccess(false);
            result.setError("删除失败");
        }
        return result;
    }

    public Result<LoginRecordVO> searchLoginRecord(int n) {
        Result<LoginRecordVO> result = new Result<LoginRecordVO>(true);
        Date date = DateUtils.addDays(new Date(), -n);
        List<LoginRecordVO> list = studentDao.searchLoginRecord(DateFormatUtils.format(date, "yyyy-MM-dd"));

        Map<String, RecordCountVO> map = new LinkedHashMap<String, RecordCountVO>();
        for (int i = 0; i < n; i++) {
            date = DateUtils.addDays(date, 1);
            String str = DateFormatUtils.format(date, "yyyy-MM-dd");
            RecordCountVO vo = new RecordCountVO();
            vo.setPersonCount(0);
            vo.setRecordCount(0);
            map.put(str, vo);
        }

        if (list != null) {
            for (LoginRecordVO vo : list) {
                if (map.containsKey(vo.getLoginDate())) {
                    RecordCountVO recordCountVO = map.get(vo.getLoginDate());
                    recordCountVO.incPersonCount();
                    recordCountVO.addRecordCount(vo.getCount());
                    map.put(vo.getLoginDate(), recordCountVO);
                }
            }
        }
        result.getData().put("map", map);

        return result;
    }
}
