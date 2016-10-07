package cn.zzuzl.controller;

import cn.zzuzl.common.Constants;
import cn.zzuzl.common.LoginContext;
import cn.zzuzl.common.annotation.Authorization;
import cn.zzuzl.dao.StudentDao;
import cn.zzuzl.model.Authority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class PageController {
    @Resource
    private StudentDao studentDao;

    @Authorization
    @RequestMapping("/")
    public String index(HttpSession session, Model model) {
        List<String> resources = (List<String>) session.getAttribute(Constants.RESOURCES);
        if (resources == null) {
            List<Authority> authorities = studentDao.getResources(LoginContext.getCurrentSchoolNum());
            if (authorities != null) {
                resources = new ArrayList<String>();
                for (Authority authority : authorities) {
                    resources.add(authority.getAuthCode());
                }
            }
            session.setAttribute(Constants.RESOURCES, resources);
        }

        if (resources != null) {
            // 根据权限动态加载菜单
            for (String str : resources) {
                if (Constants.AUTH_STU_MANAGE.equalsIgnoreCase(str)) {
                    model.addAttribute(Constants.AUTH_STU_MANAGE, true);
                } else if (Constants.AUTH_PRO_MANAGE.equalsIgnoreCase(str)) {
                    model.addAttribute(Constants.AUTH_PRO_MANAGE, true);
                } else if (Constants.AUTH_QUA_MANAGE.equalsIgnoreCase(str)) {
                    model.addAttribute(Constants.AUTH_QUA_MANAGE, true);
                } else if (Constants.AUTH_GH_MANAGE.equalsIgnoreCase(str)) {
                    model.addAttribute(Constants.AUTH_GH_MANAGE, true);
                } else if (Constants.AUTH_AUTH_MANAGE.equalsIgnoreCase(str)) {
                    model.addAttribute(Constants.AUTH_AUTH_MANAGE, true);
                }
            }
        }

        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
