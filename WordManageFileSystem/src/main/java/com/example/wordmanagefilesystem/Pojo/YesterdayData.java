package com.example.wordmanagefilesystem.Pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class YesterdayData {
    private Integer id;
    private double yesterdayAccuracy;
    private Integer yesterdayNewWord;
    private Integer yesterdayOfficialsTotal;
    private Integer yesterdayLessWord;
    private LocalDateTime latestUpdateTime;

    public YesterdayData() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getYesterdayAccuracy() {
        return yesterdayAccuracy;
    }

    public void setYesterdayAccuracy(double yesterdayAccuracy) {
        this.yesterdayAccuracy = yesterdayAccuracy;
    }

    public Integer getYesterdayNewWord() {
        return yesterdayNewWord;
    }

    public void setYesterdayNewWord(Integer yesterdayNewWord) {
        this.yesterdayNewWord = yesterdayNewWord;
    }

    public Integer getYesterdayOfficialsTotal() {
        return yesterdayOfficialsTotal;
    }

    public void setYesterdayOfficialsTotal(Integer yesterdayOfficialsTotal) {
        this.yesterdayOfficialsTotal = yesterdayOfficialsTotal;
    }

    public Integer getYesterdayLessWord() {
        return yesterdayLessWord;
    }

    public void setYesterdayLessWord(Integer yesterdayLessWord) {
        this.yesterdayLessWord = yesterdayLessWord;
    }

    public LocalDateTime getLatestUpdateTime() {
        return latestUpdateTime;
    }

    public void setLatestUpdateTime(LocalDateTime latestUpdateTime) {
        this.latestUpdateTime = latestUpdateTime;
    }

    public YesterdayData(Integer id, double yesterdayAccuracy, Integer yesterdayNewWord, Integer yesterdayOfficialsTotal, Integer yesterdayLessWord, LocalDateTime latestUpdateTime) {
        this.id = id;
        this.yesterdayAccuracy = yesterdayAccuracy;
        this.yesterdayNewWord = yesterdayNewWord;
        this.yesterdayOfficialsTotal = yesterdayOfficialsTotal;
        this.yesterdayLessWord = yesterdayLessWord;
        this.latestUpdateTime = latestUpdateTime;
    }
}
