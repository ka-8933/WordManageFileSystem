package com.example.wordmanagefilesystem.Pojo;

import lombok.Data;

/*
*const queryBody = {
            word : wordV,
            meaning : meaningV,
            wordClass : wordClassV,
            selectDegrad : selectDegradV
        }*/
@Data
public class QueryWordBody {
    private Integer userId;
    private Integer page;
    private Integer size;
    private String word;
    private String meaning;
    private String wordClass;
    private String selectDegrad;

    public QueryWordBody() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
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

    public String getWordClass() {
        return wordClass;
    }

    public void setWordClass(String wordClass) {
        this.wordClass = wordClass;
    }

    public String getSelectDegrad() {
        return selectDegrad;
    }

    public void setSelectDegrad(String selectDegrad) {
        this.selectDegrad = selectDegrad;
    }

    public QueryWordBody(Integer userId, Integer page, Integer size, String word, String meaning, String wordClass, String selectDegrad) {
        this.userId = userId;
        this.page = page;
        this.size = size;
        this.word = word;
        this.meaning = meaning;
        this.wordClass = wordClass;
        this.selectDegrad = selectDegrad;
    }
}
