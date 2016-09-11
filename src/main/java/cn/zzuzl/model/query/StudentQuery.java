package cn.zzuzl.model.query;

/**
 * Created by Administrator on 2016/9/10.
 */
public class StudentQuery extends BaseQuery {
    private String schoolNum;
    private String name;

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
}
