package cn.zzuzl.dto;

import cn.zzuzl.model.TermScore;

import java.util.List;

public class QualityJsonBean {
    private String schoolNum;
    private String name;
    private List<TermScore> list;

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

    public List<TermScore> getList() {
        return list;
    }

    public void setList(List<TermScore> list) {
        this.list = list;
    }
}
