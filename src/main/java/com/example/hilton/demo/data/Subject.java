package com.example.hilton.demo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    private String name;
    private int totalQuestions;
    private int correct;
    private int incorrect;
    private int score;

}
