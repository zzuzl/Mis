package cn.zzuzl.common.interceptor;

import cn.zzuzl.common.Constants;
import cn.zzuzl.common.LoginContext;
import cn.zzuzl.common.annotation.Authorization;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LogManager.getLogger(getClass());

    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        HttpSession session = request.getSession();

        Student student = (Student) session.getAttribute(Constants.STU);
        if (student != null) {
            LoginContext.setLoginContext(student);
        }

        if (handler instanceof HandlerMethod) {
            final HandlerMethod method = (HandlerMethod) handler;
            Authorization auth = method.getMethodAnnotation(Authorization.class);

            Result result = new Result(true);
            if (auth != null) {
                if (student == null) {
                    logger.error("error:未登录");
                    response.sendRedirect("/login");
                    return false;
                }
                /*result.setSuccess(false);
                result.setError("未认证");
                response.getWriter().println(new ObjectMapper().writeValueAsString(result));*/
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
