package com.example.wordmanagefilesystem.Pojo;

import lombok.Data;

@Data
public class WordWithUserId {
    private Integer userId;
    private Integer id;
    private String word;
    private String meaning;
    private String partOfSpeech;
    private String belongGrade;
    private String similarWord;
    private String phrase;
    private Integer total;
    private Integer isRight;
    private double accuracy;
    private String belong;

    public WordWithUserId() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getBelongGrade() {
        return belongGrade;
    }

    public void setBelongGrade(String belongGrade) {
        this.belongGrade = belongGrade;
    }

    public String getSimilarWord() {
        return similarWord;
    }

    public void setSimilarWord(String similarWord) {
        this.similarWord = similarWord;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
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

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public WordWithUserId(Integer userId, Integer id, String word, String meaning, String partOfSpeech, String belongGrade, String similarWord, String phrase, Integer total, Integer isRight, double accuracy, String belong) {
        this.userId = userId;
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.partOfSpeech = partOfSpeech;
        this.belongGrade = belongGrade;
        this.similarWord = similarWord;
        this.phrase = phrase;
        this.total = total;
        this.isRight = isRight;
        this.accuracy = accuracy;
        this.belong = belong;
    }
}
