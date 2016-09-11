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
    private static Map<String, HttpSession> map = Collections.synchronizedMap(new HashMap<String, HttpSession>());

    public static Student getCurrentLoginInfo() {
        return new Student();
    }

    public static String getCurrentLoginSchoolNum() {
        return getCurrentLoginInfo().getSchoolNum();
    }

    public static Map<String, HttpSession> getMap() {
        return map;
    }
}
