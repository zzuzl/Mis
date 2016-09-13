package cn.zzuzl.common.util;

import cn.zzuzl.common.Constants;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.ScoreVO;
import cn.zzuzl.model.Student;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringEscapeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhanglei53 on 2016/8/2.
 */
@Component
public class NetUtil {
    private Logger logger = LogManager.getLogger(getClass());

    // 登录校园网
    public Result connect(String url, String schoolNum, String password, String selec) throws Exception {
        Result result = new Result(true);
        String nianji = schoolNum.substring(0, 4);
        Document document = Jsoup.connect(url)
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
                student.setClassCode(schoolNum.substring(4, 9));

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

    // 获取远程ip
    public String getRemoteIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }

    // 根据ip获取所在地
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

    // 查询成绩
    public Result searchScore(String schoolNum, String password) throws Exception {
        String url = "http://jw.zzu.edu.cn/scripts/qscore.dll/search";
        Result result = new Result(true);
        String nianji = schoolNum.substring(0, 4);
        Document document = Jsoup.connect(url)
                .timeout(10 * 1000)
                .data("nianji", nianji)
                .data("xuehao", schoolNum)
                .data("mima", password)
                .data("selec", "http://jw.zzu.edu.cn/scripts/qscore.dll/search")
                .post();

        if (document != null) {
            Elements elements = document.body().select("p font a");
            if (elements != null) {
                Map<String, List<ScoreVO>> map = new HashMap<String, List<ScoreVO>>();
                boolean flag = false;
                for (int i = 0; i < elements.size(); i++) {
                    Element a = elements.get(i);
                    if (isTermHref(a.text())) {
                        Document doc = Jsoup.connect(a.attr("href")).timeout(10 * 1000).get();
                        map.put(a.text(), getTermScoreList(doc));
                        if (!flag) {
                            flag = true;
                            map.put(StringEscapeUtils.escapeHtml4(a.parent().ownText()).replaceAll("&nbsp;", ""),
                                    getTermScoreList(document));
                        }
                    }
                }

                // 只取最后两学期的成绩
                List<Map.Entry<String, List<ScoreVO>>> list = new ArrayList<Map.Entry<String, List<ScoreVO>>>();
                list.addAll(map.entrySet());
                Collections.sort(list, new Comparator<Map.Entry<String, List<ScoreVO>>>() {
                    public int compare(Map.Entry<String, List<ScoreVO>> o1, Map.Entry<String, List<ScoreVO>> o2) {
                        return o1.getKey().compareTo(o2.getKey());
                    }
                });

                int fromIndex = list.size() - 2 > 0 ? list.size() - 2 : 0;
                result.getData().put("scores", list.subList(fromIndex, list.size()));
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

    private ScoreVO getScore(Element tr) {
        ScoreVO scoreVO = null;
        if (tr != null) {
            Elements tds = tr.children();
            if (tds != null && tds.size() == 5) {
                if (isDouble(tds.get(3).text()) && isNumber(tds.get(2).text())) {
                    scoreVO = new ScoreVO(tds.get(0).text(),
                            Double.parseDouble(tds.get(3).text()),
                            Integer.parseInt(tds.get(2).text()));
                }
            }
        }
        return scoreVO;
    }

    private boolean isTermHref(String text) {
        Pattern pattern = Pattern.compile("第\\d{1}学期");
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    private boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d*$");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    private boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    private List<ScoreVO> getTermScoreList(Document document) throws IOException {
        List<ScoreVO> list = new ArrayList<ScoreVO>();
        if (document != null) {
            Elements elements = document.body().select("center table tr");
            if (elements != null && elements.size() >= 1) {
                for (int i = 1; i < elements.size(); i++) {
                    Element tr = elements.get(i);
                    ScoreVO scoreVO = getScore(tr);
                    if (scoreVO != null) {
                        list.add(scoreVO);
                    }
                }
            }
        }
        return list;
    }

    public static void main(String[] args) {
        NetUtil netUtil = new NetUtil();
        // System.out.println(netUtil.getRemoteLocation("139.129.10.226"));
        // System.out.println("20133410139".substring(4, 9));
        // System.out.println(StringEscapeUtils.unescapeJava("\\u4e2d\\u56fd"));
        // System.out.println(netUtil.isTermHref("第1学期"));
        try {
            Result result = netUtil.searchScore("20133410139", "672399171");
            if (result.isSuccess()) {
                System.out.println(result.getData().get("scores"));
            } else {
                System.out.println(result.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // System.out.println("     第6学期   ".replaceAll(" ",""));
        // System.out.println(netUtil.isDouble("良好"));
        // System.out.println(netUtil.isNumber("3"));
    }
}
