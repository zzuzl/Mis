package cn.zzuzl.controller;

import cn.zzuzl.dao.LoveDao;
import cn.zzuzl.dto.timeline.TimeLineBean;
import cn.zzuzl.dto.timeline.TimeLineItem;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/31.
 */
@Controller
@RequestMapping("love")
public class LoveController {
    @Resource
    private LoveDao loveDao;

    // page
    @RequestMapping("")
    public String lovePage() {
        return "love";
    }

    // 获取时间轴信息
    @RequestMapping(value = "/timeline", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, TimeLineBean> timeline() {
        Map<String, TimeLineBean> map = new HashMap<String, TimeLineBean>();
        TimeLineBean timeLineBean = new TimeLineBean();
        timeLineBean.setHeadline("ZL & LLH 's love");
        timeLineBean.setText("从2016年10月14日晚23:31分说起。。。");
        List<TimeLineItem> itemList = loveDao.timeLineItems();
        if(itemList != null) {
            for(TimeLineItem item : itemList) {
                item.convertDateFormat();
            }
        }
        timeLineBean.setDate(itemList);
        map.put("timeline", timeLineBean);
        return map;
    }
}
