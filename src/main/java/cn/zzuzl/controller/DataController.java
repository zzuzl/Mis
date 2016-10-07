package cn.zzuzl.controller;

import cn.zzuzl.common.annotation.Authorization;
import cn.zzuzl.dao.StudentDao;
import cn.zzuzl.model.Authority;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/6.
 */
@Controller
@RequestMapping("data")
public class DataController {
    @Resource
    private StudentDao studentDao;
    private Logger logger = LogManager.getLogger(getClass());

    @Authorization
    @RequestMapping("authorities")
    @ResponseBody
    public List<Authority> authorities() {
        List<Authority> list = new ArrayList<Authority>();
        try {
            list = studentDao.allAuthority();
        } catch (Exception e) {
            logger.error(e);
        }
        return list;
    }
}
