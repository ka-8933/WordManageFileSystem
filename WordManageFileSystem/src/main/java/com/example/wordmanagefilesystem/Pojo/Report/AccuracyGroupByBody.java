package com.example.wordmanagefilesystem.Pojo.Report;

import lombok.Data;

/*
* {value: 28, name: '良好'},*/
@Data
public class AccuracyGroupByBody {
    private Long value;
    private String name;

    public AccuracyGroupByBody() {
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccuracyGroupByBody(Long value, String name) {
        this.value = value;
        this.name = name;
    }
}
