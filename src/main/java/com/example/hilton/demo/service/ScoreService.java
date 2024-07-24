package com.example.hilton.demo.service;

import com.example.hilton.demo.data.ScoreResponse;
import com.example.hilton.demo.data.Sheet;
import com.example.hilton.demo.data.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class ScoreService {
    @Autowired
    MongoTemplate mongoTemplate;

    public void saveScoreSheet(Sheet sheet) {
        addScore(sheet);
        mongoTemplate.save(sheet);
        System.out.println("Saved sheet");
    }

    private void addScore(Sheet sheet) {
        sheet.getSubjects().stream().forEach(subject -> {
            subject.setScore((int) ((subject.getCorrect() * 1) - (subject.getIncorrect() * 0.25)));
        });
    }


    public List<ScoreResponse> getScores()
    {
        List<Sheet> sheets = mongoTemplate.findAll(Sheet.class);

       return  buildScoreResponse(sheets);
    }

    private List<ScoreResponse> buildScoreResponse(List<Sheet> sheets) {
        List<ScoreResponse> responses = new ArrayList<>();
        for(Sheet sheet : sheets) {
            ScoreResponse scoreResponse1 = new ScoreResponse();
            scoreResponse1.setTesteeId(String.valueOf(sheet.getTesteeId()));
            scoreResponse1.setScores(buildScores(sheet));
            responses.add(scoreResponse1);
        }
        return responses;


    }

    private Map<String, Double> buildScores(Sheet sheet) {
        Map<String, Double> marksMap = new HashMap<>();
        int total = 0;
        for(Subject subject : sheet.getSubjects()) {
                marksMap.put(subject.getName(), (double) subject.getScore());
                total = total + subject.getScore();
        }
        marksMap.put("total", (double) total);
        marksMap.put("average", (double) (total/sheet.getSubjects().size()));

        return marksMap;
    }
}
