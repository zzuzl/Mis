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

    private LoginContext(Student student) {
        this.student = student;
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

    public static void setLoginContext(Student student) {
        LoginContext context = new LoginContext(student);
        holders.set(context);
    }

    public static void removeLoginContext() {
        holders.remove();
    }
}
