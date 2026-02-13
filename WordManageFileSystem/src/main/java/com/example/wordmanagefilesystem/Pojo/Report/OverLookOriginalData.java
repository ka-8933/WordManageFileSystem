package com.example.wordmanagefilesystem.Pojo.Report;

import lombok.Data;

@Data
public class OverLookOriginalData {
    private Integer wordTotal;
    private Integer userAccuracyAvg;
    private Integer userReedAvg;
    private Integer UserTotal;

    public OverLookOriginalData() {
    }

    public Integer getWordTotal() {
        return wordTotal;
    }

    public void setWordTotal(Integer wordTotal) {
        this.wordTotal = wordTotal;
    }

    public Integer getUserAccuracyAvg() {
        return userAccuracyAvg;
    }

    public void setUserAccuracyAvg(Integer userAccuracyAvg) {
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

    public OverLookOriginalData(Integer wordTotal, Integer userAccuracyAvg, Integer userReedAvg, Integer userTotal) {
        this.wordTotal = wordTotal;
        this.userAccuracyAvg = userAccuracyAvg;
        this.userReedAvg = userReedAvg;
        UserTotal = userTotal;
    }
}
