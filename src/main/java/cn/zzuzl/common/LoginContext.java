package cn.zzuzl.common;

/**
 * Created by Administrator on 2016/9/11.
 */

import cn.zzuzl.model.Student;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录上下文
 */
public final class LoginContext {
    private static ThreadLocal<LoginContext> holders = new ThreadLocal<LoginContext>();

    private Student student;
    private String password;

    private LoginContext(Student student, String password) {
        this.student = student;
        this.password = password;
    }

    public static LoginContext getLoginContext() {
        return holders.get();
    }

    public static String getCurrentSchoolNum() {
        return holders.get().getStudent().getSchoolNum();
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void setLoginContext(Student student, String password) {
        LoginContext context = holders.get();
        if (context != null) {
            context.setStudent(student);
            context.setPassword(password);
        } else {
            context = new LoginContext(student, password);
        }
        holders.set(context);
    }

    public static void removeLoginContext() {
        holders.remove();
    }
}
