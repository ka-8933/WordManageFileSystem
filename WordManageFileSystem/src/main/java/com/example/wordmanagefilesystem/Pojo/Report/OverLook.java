package com.example.wordmanagefilesystem.Pojo.Report;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OverLook {
    private Integer wordTotal;
    private String userAccuracyAvg;
    private Integer userReedAvg;
    private Integer UserTotal;

    public OverLook() {
    }

    public Integer getWordTotal() {
        return wordTotal;
    }

    public void setWordTotal(Integer wordTotal) {
        this.wordTotal = wordTotal;
    }

    public String getUserAccuracyAvg() {
        return userAccuracyAvg;
    }

    public void setUserAccuracyAvg(String userAccuracyAvg) {
        this.userAccuracyAvg = userAccuracyAvg;
    }

    public Integer getUserReedAvg() {
        return userReedAvg;
    }

    public void setUserReedAvg(Integer userReedAvg) {
        this.userReedAvg = userReedAvg;
    }

    public Integer getUserTotal() {
        return UserTotal;
    }

    public void setUserTotal(Integer userTotal) {
        UserTotal = userTotal;
    }

    public OverLook(Integer wordTotal, String userAccuracyAvg, Integer userReedAvg, Integer userTotal) {
        this.wordTotal = wordTotal;
        this.userAccuracyAvg = userAccuracyAvg;
        this.userReedAvg = userReedAvg;
        UserTotal = userTotal;
    }
}
