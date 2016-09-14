package cn.zzuzl.model;

import java.util.List;

public class TermScore {
    private String term;
    private List<ScoreVO> scores;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<ScoreVO> getScores() {
        return scores;
    }

    public void setScores(List<ScoreVO> scores) {
        this.scores = scores;
    }
}
