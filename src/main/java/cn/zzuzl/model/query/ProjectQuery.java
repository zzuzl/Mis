package cn.zzuzl.model.query;

/**
 * Created by Administrator on 2016/9/11.
 */
public class ProjectQuery extends BaseQuery {
    private String majorCode;
    private String grade;
    private Integer year;

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
