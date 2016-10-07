package cn.zzuzl.controller;

import cn.zzuzl.common.annotation.Authorization;
import cn.zzuzl.dao.StudentDao;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.Authority;
import cn.zzuzl.service.MailService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Resource
    private MailService mailService;
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

    @Authorization
    @RequestMapping(value = "advice", method = RequestMethod.POST)
    @ResponseBody
    public Result advice(@RequestParam(value = "advice", required = false, defaultValue = "") String advice) {
        Result result = new Result(true);
        try {
            if (StringUtils.isEmpty(advice)) {
                result.setSuccess(false);
                result.setError("请填写内容");
            } else {
                mailService.sendAdviceEmail(advice);
            }
        } catch (Exception e) {
            logger.error(e);
            result.setSuccess(false);
            result.setError("提交失败");
        }
        return result;
    }
}
