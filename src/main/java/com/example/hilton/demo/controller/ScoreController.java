package com.example.hilton.demo.controller;

import com.example.hilton.demo.data.ScoreResponse;
import com.example.hilton.demo.data.Sheet;
import com.example.hilton.demo.data.Subject;
import com.example.hilton.demo.service.ScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping(value = "evaluation")
public class ScoreController {

    private final List<String> allowedSubjects = List.of("maths", "science", "general");

    @Autowired
    ScoreService scoreService;

    @GetMapping(value = "/status")
    public String getHello() {
        return "UP!";
    }


    @PostMapping("/sheets")
    public ResponseEntity<String> saveScoreSheets(@RequestBody Sheet sheet) {
        try {
            if (validRequest(sheet)) {
                scoreService.saveScoreSheet(sheet);
                return handleResponse("successful", HttpStatusCode.valueOf(202));
            }
            return handleResponse("validation failure", HttpStatusCode.valueOf(400));
        } catch (Exception e) {
            log.error(e.getMessage());
            return handleResponse("failed with error " + e.getMessage(), HttpStatusCode.valueOf(400));
        }

    }

    @GetMapping("scores")
    public ResponseEntity<?> getScore(@RequestParam(required = false) List<String> testeeIds,
                                                  @RequestParam(required = false) List<String> subjects,
                                                  @RequestParam(required = false) String totalRange,
                                                  @RequestParam(required = false) String averageRange,
                                                  @RequestParam(required = false) String scoreRange){

        try {
            List<ScoreResponse> scores = scoreService.getScores();

            // Filter by testeeIds
            if (testeeIds != null && !testeeIds.isEmpty()) {
                scores = scores.stream()
                        .filter(score -> testeeIds.contains(score.getTesteeId()))
                        .collect(Collectors.toList());
            }

            // Filter by totalRange
            if (totalRange != null && !totalRange.isEmpty()) {
                String[] range = totalRange.split("-");
                double min = Double.parseDouble(range[0]);
                double max = Double.parseDouble(range[1]);
                scores = scores.stream()
                        .filter(score -> {
                            double total = score.getScores().get("total");
                            return total >= min && total <= max;
                        })
                        .collect(Collectors.toList());
            }

            // Filter by averageRange
            if (averageRange != null && !averageRange.isEmpty()) {
                String[] range = averageRange.split("-");
                double min = Double.parseDouble(range[0]);
                double max = Double.parseDouble(range[1]);
                scores = scores.stream()
                        .filter(score -> {
                            double average = score.getScores().get("average");
                            return average >= min && average <= max;
                        })
                        .collect(Collectors.toList());
            }

            // Filter by scoreRange for specific subjects
            if (scoreRange != null && !scoreRange.isEmpty() && subjects != null && !subjects.isEmpty()) {
                String[] range = scoreRange.split("-");
                double min = Double.parseDouble(range[0]);
                double max = Double.parseDouble(range[1]);
                scores = scores.stream()
                        .filter(score -> subjects.stream().allMatch(subject -> {
                            double subjectScore = score.getScores().getOrDefault(subject, 0.0);
                            return subjectScore >= min && subjectScore <= max;
                        }))
                        .collect(Collectors.toList());
            }

            // Sort by total descending and testeeId ascending
            scores.sort(Comparator.comparing((ScoreResponse sr) -> sr.getScores().get("total")).reversed()
                    .thenComparing(ScoreResponse::getTesteeId));

            return new ResponseEntity<>(scores, HttpStatusCode.valueOf(202));
        } catch (Exception e) {
            return new ResponseEntity<>("failed with error: "+e.getMessage(), HttpStatusCode.valueOf(400));
        }


    }

    private boolean validRequest(Sheet sheet) throws Exception {
        if(sheet != null ){
            for(Subject subject : sheet.getSubjects()){
                if(!allowedSubjects.contains(subject.getName())){
                    throw new Exception("Invalid subject");
                }
            }
            return true;
        }
        return false;
    }

    private ResponseEntity<String> handleResponse(String msg, HttpStatusCode httpStatusCode){
        return new ResponseEntity<>(msg, httpStatusCode);
    }
}
