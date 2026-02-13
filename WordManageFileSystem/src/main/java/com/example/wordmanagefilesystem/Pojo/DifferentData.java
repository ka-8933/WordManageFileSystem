package com.example.wordmanagefilesystem.Pojo;

import lombok.Data;

@Data
public class DifferentData {
    private String accuracyDifferent;
    private String newWordDifferent;
    private String officialsWordDifferent;
    private String lessDifferent;

    public DifferentData() {
    }

    public String getAccuracyDifferent() {
        return accuracyDifferent;
    }

    public void setAccuracyDifferent(String accuracyDifferent) {
        this.accuracyDifferent = accuracyDifferent;
    }

    public String getNewWordDifferent() {
        return newWordDifferent;
    }

    public void setNewWordDifferent(String newWordDifferent) {
        this.newWordDifferent = newWordDifferent;
    }

    public String getOfficialsWordDifferent() {
        return officialsWordDifferent;
    }

    public void setOfficialsWordDifferent(String officialsWordDifferent) {
        this.officialsWordDifferent = officialsWordDifferent;
    }

    public String getLessDifferent() {
        return lessDifferent;
    }

    public void setLessDifferent(String lessDifferent) {
        this.lessDifferent = lessDifferent;
    }

    public DifferentData(String accuracyDifferent, String newWordDifferent, String officialsWordDifferent, String lessDifferent) {
        this.accuracyDifferent = accuracyDifferent;
        this.newWordDifferent = newWordDifferent;
        this.officialsWordDifferent = officialsWordDifferent;
        this.lessDifferent = lessDifferent;
    }
}
