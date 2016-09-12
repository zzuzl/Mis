package cn.zzuzl.model;

/**
 * Created by Administrator on 2016/9/12.
 */
public class ScoreVO {
    private String title;
    private Double score;
    private Integer stuScore;

    public ScoreVO(String title, Double score, Integer stuScore) {
        this.title = title;
        this.score = score;
        this.stuScore = stuScore;
    }

    public ScoreVO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getStuScore() {
        return stuScore;
    }

    public void setStuScore(Integer stuScore) {
        this.stuScore = stuScore;
    }

    @Override
    public String toString() {
        return "ScoreVO{" +
                "title='" + title + '\'' +
                ", score=" + score +
                ", stuScore=" + stuScore +
                '}';
    }
}
