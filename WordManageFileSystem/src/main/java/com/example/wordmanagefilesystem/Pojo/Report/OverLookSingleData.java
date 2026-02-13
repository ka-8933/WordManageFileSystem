package com.example.wordmanagefilesystem.Pojo.Report;

import lombok.Data;

@Data
public class OverLookSingleData {
    private String dataName;
    private Integer fourAgo;
    private Integer thirdAgo;
    private Integer twoAgo;
    private Integer oneAgo;
    private Integer today;

    public OverLookSingleData() {
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public Integer getFourAgo() {
        return fourAgo;
    }

    public void setFourAgo(Integer fourAgo) {
        this.fourAgo = fourAgo;
    }

    public Integer getThirdAgo() {
        return thirdAgo;
    }

    public void setThirdAgo(Integer thirdAgo) {
        this.thirdAgo = thirdAgo;
    }

    public Integer getTwoAgo() {
        return twoAgo;
    }

    public void setTwoAgo(Integer twoAgo) {
        this.twoAgo = twoAgo;
    }

    public Integer getOneAgo() {
        return oneAgo;
    }

    public void setOneAgo(Integer oneAgo) {
        this.oneAgo = oneAgo;
    }

    public Integer getToday() {
        return today;
    }

    public void setToday(Integer today) {
        this.today = today;
    }

    public OverLookSingleData(String dataName, Integer fourAgo, Integer thirdAgo, Integer twoAgo, Integer oneAgo, Integer today) {
        this.dataName = dataName;
        this.fourAgo = fourAgo;
        this.thirdAgo = thirdAgo;
        this.twoAgo = twoAgo;
        this.oneAgo = oneAgo;
        this.today = today;
    }
}
