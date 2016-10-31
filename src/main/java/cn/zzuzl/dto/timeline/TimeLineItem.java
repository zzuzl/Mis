package cn.zzuzl.dto.timeline;

/**
 * Created by Administrator on 2016/10/31.
 */
public class TimeLineItem {
    private Integer id;
    private String startDate;
    private String endDate;
    private String headline;
    private String text;
    private String tag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void convertDateFormat() {
        if (startDate != null) {
            startDate = startDate.replaceAll("-", ",");
        }
        if (endDate != null) {
            endDate = endDate.replaceAll("-", ",");
        }
    }
}
