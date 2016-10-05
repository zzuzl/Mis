package cn.zzuzl.dao;

import cn.zzuzl.dto.LoginRecordVO;
import cn.zzuzl.model.GoHome;
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

    List<LoginRecordVO> searchLoginRecord(@Param("loginDate") String loginDate);

    List<Student> export(StudentQuery query);

    GoHome searchOneGoHome(@Param("schoolNum") String schoolNum,
                           @Param("year") Integer year,
                           @Param("vacation") String vacation);

    int insertGoHome(GoHome goHome);

    List<GoHome> searchGoHome(@Param("classCode") String classCode,
                              @Param("year") Integer year,
                              @Param("vacation") String vacation);
}
