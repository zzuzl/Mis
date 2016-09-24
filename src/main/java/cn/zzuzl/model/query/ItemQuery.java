package cn.zzuzl.model.query;

import java.util.List;

public class ItemQuery {
    private List<Integer> projectIds;
    private String majorCode;
    private String grade;
    private Integer year;

    public List<Integer> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<Integer> projectIds) {
        this.projectIds = projectIds;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
