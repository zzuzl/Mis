package cn.zzuzl.common.interceptor;

import cn.zzuzl.common.Constants;
import cn.zzuzl.common.LoginContext;
import cn.zzuzl.common.annotation.Authorization;
import cn.zzuzl.dao.StudentDao;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private StudentDao studentDao;
    private Logger logger = LogManager.getLogger(getClass());

    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        HttpSession session = request.getSession();

        Student student = (Student) session.getAttribute(Constants.STU);
        if (student != null) {
            LoginContext.setLoginContext(student, (String) session.getAttribute(Constants.PASS));
        }

        if (handler instanceof HandlerMethod) {
            final HandlerMethod method = (HandlerMethod) handler;
            Authorization auth = method.getMethodAnnotation(Authorization.class);

            if (auth != null) {
                if (student == null) {
                    logger.error("error:未登录");
                    response.sendError(401);
                    return false;
                }

                // 去掉空串
                List<String> list = new ArrayList<String>();
                for (String str : auth.value()) {
                    if (!StringUtils.isEmpty(str)) {
                        list.add(str);
                    }
                }

                if (list.size() > 0) {
                    // 获取登录人的权限
                    List<String> resources = (List<String>) session.getAttribute(Constants.RESOURCES);
                    if (resources == null) {
                        resources = studentDao.getResourcesBySchoolNum(student.getSchoolNum());
                        session.setAttribute(Constants.RESOURCES, resources);
                    }
                    boolean flag = false;
                    if(resources != null) {
                        for (String str : list) {
                            if (resources.contains(str)) {
                                flag = true;
                                break;
                            }
                        }
                    }

                    if (!flag) {
                        // 无权限
                        response.sendError(403, "无权限访问");
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        LoginContext.removeLoginContext();
    }
}
