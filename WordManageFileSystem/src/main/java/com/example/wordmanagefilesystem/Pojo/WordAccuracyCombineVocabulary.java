package com.example.wordmanagefilesystem.Pojo;

import lombok.Data;

@Data
public class WordAccuracyCombineVocabulary{
    private Integer id;
    private String word;
    private String meaning;
    private Integer total;
    private Integer isRight;
    private double accuracy;
    private Integer userId;

    public WordAccuracyCombineVocabulary() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getIsRight() {
        return isRight;
    }

    public void setIsRight(Integer isRight) {
        this.isRight = isRight;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public WordAccuracyCombineVocabulary(Integer id, String word, String meaning, Integer total, Integer isRight, double accuracy, Integer userId) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.total = total;
        this.isRight = isRight;
        this.accuracy = accuracy;
        this.userId = userId;
    }
}
