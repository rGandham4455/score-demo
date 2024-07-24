package com.example.hilton.demo.data;



import java.util.Map;

public class ScoreResponse {
    private String testeeId;
    private Map<String, Double> scores;


    public String getTesteeId() {
        return testeeId;
    }

    public void setTesteeId(String testeeId) {
        this.testeeId = testeeId;
    }

    public Map<String, Double> getScores() {
        return scores;
    }

    public void setScores(Map<String, Double> scores) {
        this.scores = scores;
    }
}

