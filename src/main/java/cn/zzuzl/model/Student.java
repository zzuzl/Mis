package cn.zzuzl.model;

import java.io.Serializable;

public class Student implements Serializable {
    private String schoolNum;
    private String name;
    private String grade;
    private String sex;
    private String classCode;

    public Student() {
    }

    public Student(String schoolNum, String name, String grade, String sex, String classCode) {
        this.schoolNum = schoolNum;
        this.name = name;
        this.grade = grade;
        this.sex = sex;
        this.classCode = classCode;
    }

    public String getSchoolNum() {
        return schoolNum;
    }

    public void setSchoolNum(String schoolNum) {
        this.schoolNum = schoolNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
}
