package cn.zzuzl.model;

import java.util.Date;

/**
 * Created by Administrator on 2016/9/10.
 */
public class LoginRecord {
    private Long id;
    private String schoolNum;
    private Date loginTime;
    private String ip;
    private String location;

    public LoginRecord(String schoolNum, String ip, String location) {
        this.schoolNum = schoolNum;
        this.ip = ip;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolNum() {
        return schoolNum;
    }

    public void setSchoolNum(String schoolNum) {
        this.schoolNum = schoolNum;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
