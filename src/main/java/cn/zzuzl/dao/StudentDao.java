package cn.zzuzl.dao;

import cn.zzuzl.model.LoginRecord;
import cn.zzuzl.model.Student;
import cn.zzuzl.model.query.StudentQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentDao {
    Student searchBySchoolNum(@Param("schoolNum") String schoolNum);

    int insertStudent(Student student);

    List<Student> searchStudent(StudentQuery query);

    int getStudentCount(StudentQuery query);

    int insertLoginRecord(LoginRecord record);

    int updateStudent(Student student);

    int updateInvalid(@Param("schoolNum") String schoolNum);

    List<String> getResourcesBySchoolNum(@Param("schoolNum") String schoolNum);
}
