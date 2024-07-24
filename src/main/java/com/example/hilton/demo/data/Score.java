package com.example.hilton.demo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Score {

    private int maths;
    private int science;
    private int general;
    private int total;
    private int avg;
}
