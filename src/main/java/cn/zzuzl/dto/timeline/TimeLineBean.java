package cn.zzuzl.dto.timeline;

import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */
public class TimeLineBean {
    private String headline;
    private String type = "default";
    private String text;
    private List<TimeLineItem> date;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TimeLineItem> getDate() {
        return date;
    }

    public void setDate(List<TimeLineItem> date) {
        this.date = date;
    }
}
