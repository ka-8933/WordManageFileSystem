package com.example.wordmanagefilesystem.Pojo;

import lombok.Data;

@Data
public class JudgeWordResponse {
    private Boolean isRight;
    private String meaningString;

    public JudgeWordResponse() {
    }

    public Boolean getRight() {
        return isRight;
    }

    public void setRight(Boolean right) {
        isRight = right;
    }

    public String getMeaningString() {
        return meaningString;
    }

    public void setMeaningString(String meaningString) {
        this.meaningString = meaningString;
    }

    public JudgeWordResponse(Boolean isRight, String meaningString) {
        this.isRight = isRight;
        this.meaningString = meaningString;
    }
}
