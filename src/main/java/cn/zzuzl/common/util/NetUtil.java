package cn.zzuzl.common.util;

import cn.zzuzl.common.Constants;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.ScoreVO;
import cn.zzuzl.model.Student;
import cn.zzuzl.model.TermScore;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringEscapeUtils;
import com.auth0.jwt.internal.org.apache.commons.lang3.time.DateFormatUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.time.DateUtils;
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
import java.text.ParseException;
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

                try {
                    // 姓名
                    String name = document.body().select("tr").eq(1).select("td").eq(0).text();
                    student.setName(name.substring(name.indexOf('：') + 1));

                    // 性别
                    String sex = document.body().select("tr").eq(3).select("td").eq(0).text();
                    student.setSex(sex.substring(sex.indexOf('：') + 1));

                    // 班级号
                    student.setClassCode(schoolNum.substring(4, 9));

                    // 生源地
                    String originAddress = document.body().select("tr").eq(3).select("td").eq(1).text();
                    student.setOriginAddress(originAddress.substring(originAddress.indexOf('：') + 1));

                    // 出生日期
                    String birthday = document.body().select("tr").eq(4).select("td").eq(0).text();
                    Date date = DateUtils.parseDate(birthday.substring(birthday.indexOf('：') + 1), new String[]{"yyyy-MM-dd"});
                    student.setBirthday(date);

                    // 身份证号
                    String idNo = document.body().select("tr").eq(6).select("td").eq(1).text();
                    student.setIdNo(idNo.substring(idNo.indexOf('：') + 1));

                    //民族
                    String nation = document.body().select("tr").eq(5).select("td").eq(0).text();
                    student.setNation(nation.substring(nation.indexOf('：') + 1));

                    // 入学时间
                    String entranceDate = document.body().select("tr").eq(8).select("td").eq(1).text();
                    entranceDate = entranceDate.substring(entranceDate.indexOf('：') + 1);
                    date = DateUtils.parseDate(entranceDate, new String[]{"yyyy-MM-dd HH:mm:ss"});
                    student.setEntranceDate(date);
                } catch (Exception e) {
                    logger.error(e);
                    e.printStackTrace();
                }

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
                TermScore termScore = null;
                // 只取最后两学期的成绩
                List<TermScore> list = new ArrayList<TermScore>();
                boolean flag = false;
                for (int i = 0; i < elements.size(); i++) {
                    Element a = elements.get(i);

                    if ("http://jw.zzu.edu.cn/jpxx.htm".equals(a.attr("href"))) {
                        throw new RuntimeException("成绩暂时无法查询，请稍微再试！");
                    }

                    if (isTermHref(a.text())) {
                        Document doc = Jsoup.connect(a.attr("href")).timeout(10 * 1000).get();
                        termScore = new TermScore();
                        termScore.setTerm(a.text());
                        termScore.setScores(getTermScoreList(doc));
                        list.add(termScore);
                        if (!flag) {
                            flag = true;
                            termScore = new TermScore();
                            termScore.setTerm(StringEscapeUtils.escapeHtml4(a.parent().ownText()).replaceAll("&nbsp;", ""));
                            termScore.setScores(getTermScoreList(document));
                            list.add(termScore);
                        }
                    }
                }

                Collections.sort(list, new Comparator<TermScore>() {
                    public int compare(TermScore o1, TermScore o2) {
                        return o1.getTerm().compareTo(o2.getTerm());
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
        /*try {
            Result result = netUtil.searchScore("20133410139", "672399171");
            if (result.isSuccess()) {
                System.out.println(result.getData().get("scores"));
            } else {
                System.out.println(result.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // System.out.println("     第6学期   ".replaceAll(" ",""));
        // System.out.println(netUtil.isDouble("良好"));
        // System.out.println(netUtil.isNumber("3"));

        try {
            Date date = DateUtils.parseDate("2013-8-29 10:34:23", new String[]{"yyyy-MM-dd HH:mm:ss"});
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
