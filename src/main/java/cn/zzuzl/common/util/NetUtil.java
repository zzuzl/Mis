package cn.zzuzl.common.util;

import cn.zzuzl.common.Constants;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.Student;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringEscapeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Created by zhanglei53 on 2016/8/2.
 */
@Component
public class NetUtil {
    private Logger logger = LogManager.getLogger(getClass());

    public Result connect(String url, String schoolNum, String password, String selec) throws Exception {
        Result result = new Result(true);
        String nianji = schoolNum.substring(0, 4);
        Document document = document = Jsoup.connect(url)
                .timeout(10 * 1000)
                .data("nianji", nianji)
                .data("xuehao", schoolNum)
                .data("mima", password)
                .data("selec", selec)
                .post();

        if (document != null) {
            Elements elements = document.body().select(selec);
            if (elements != null && elements.size() == 1) {
                Student student = new Student();
                student.setGrade(nianji);
                student.setSchoolNum(schoolNum);

                String name = document.body().select("tr").eq(1).select("td").eq(0).text();
                String sex = document.body().select("tr").eq(3).select("td").eq(0).text();
                String classCode = document.body().select("tr").eq(8).select("td").eq(0).text();
                student.setName(name.substring(name.indexOf('：') + 1));
                student.setSex(sex.substring(sex.indexOf('：') + 1));
                student.setClassCode(classCode.substring(classCode.indexOf('：') + 1));

                result.getData().put(Constants.STU, student);
            } else {
                result.setSuccess(false);
                result.setError("学号或密码错误");
            }
        } else {
            result.setSuccess(false);
            result.setError("连接错误");
        }

        return result;
    }

    public String getRemoteIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }

    public String getRemoteLocation(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return null;
        }
        Document document = null;
        try {
            document = Jsoup.connect("http://ip.taobao.com/service/getIpInfo.php")
                    .timeout(10 * 1000)
                    .data("ip", ip)
                    .get();
            ObjectMapper mapper = new ObjectMapper();
            Map map = (Map) mapper.readValue(document.text(), Map.class).get("data");
            String country = StringEscapeUtils.unescapeJava((String) map.get("country"));
            String region = StringEscapeUtils.unescapeJava((String) map.get("region"));
            String city = StringEscapeUtils.unescapeJava((String) map.get("city"));
            return country + region + city;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        NetUtil netUtil = new NetUtil();
        System.out.println(netUtil.getRemoteLocation("139.129.10.226"));

        // System.out.println(StringEscapeUtils.unescapeJava("\\u4e2d\\u56fd"));
    }
}
