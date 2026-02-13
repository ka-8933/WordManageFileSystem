package com.example.wordmanagefilesystem.Pojo.Report;

import lombok.Data;

import java.util.List;

/*{
            name: '2026年1月24日',
            type: 'bar',
            data: [102, 68, 170]
        }*/

@Data
public class CheckEchartJson {
    private String name;
    private String type;
    private List<Integer> data;

    public CheckEchartJson() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public CheckEchartJson(String name, String type, List<Integer> data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }
}
