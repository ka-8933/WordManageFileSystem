package com.example.wordmanagefilesystem.Pojo;

import lombok.Data;

@Data
public class UpdateAccuracyJson {
    private String word;
    private String inputMeaning;
    private Integer userId;

    public UpdateAccuracyJson() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getInputMeaning() {
        return inputMeaning;
    }

    public void setInputMeaning(String inputMeaning) {
        this.inputMeaning = inputMeaning;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public UpdateAccuracyJson(String word, String inputMeaning, Integer userId) {
        this.word = word;
        this.inputMeaning = inputMeaning;
        this.userId = userId;
    }
}
