package cn.zzuzl.service;

import cn.zzuzl.dto.LoginRecordVO;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.LoginRecord;
import cn.zzuzl.model.Student;
import cn.zzuzl.model.query.StudentQuery;

/**
 * Created by Administrator on 2016/9/10.
 */
public interface StudentService {
    Student searchBySchoolNum(String schoolNum);

    Result login(String schoolNum, String password) throws Exception;

    Result insert(Student student);

    Result<Student> searchStudent(StudentQuery query);

    void insertLoginRecord(LoginRecord record);

    Result updateStudent(Student student);

    Result updateInvalid(String schoolNum);

    Result<LoginRecordVO> searchLoginRecord(int n);
}
