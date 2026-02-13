package com.example.wordmanagefilesystem.Pojo;

import lombok.Data;

import java.util.List;

@Data
public class AllAccuracyCountReport {
    private List<Object> allAccuracyList;
    private List<Object> allAccuracyValuesList;

    public AllAccuracyCountReport() {
    }

    public List<Object> getAllAccuracyList() {
        return allAccuracyList;
    }

    public void setAllAccuracyList(List<Object> allAccuracyList) {
        this.allAccuracyList = allAccuracyList;
    }

    public List<Object> getAllAccuracyValuesList() {
        return allAccuracyValuesList;
    }

    public void setAllAccuracyValuesList(List<Object> allAccuracyValuesList) {
        this.allAccuracyValuesList = allAccuracyValuesList;
    }

    public AllAccuracyCountReport(List<Object> allAccuracyList, List<Object> allAccuracyValuesList) {
        this.allAccuracyList = allAccuracyList;
        this.allAccuracyValuesList = allAccuracyValuesList;
    }
}
