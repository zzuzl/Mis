package cn.zzuzl.common.util;

import cn.zzuzl.common.Constants;
import cn.zzuzl.dto.Result;
import cn.zzuzl.model.Student;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * Created by zhanglei53 on 2016/8/2.
 */
@Component
public class NetUtil {

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
}
