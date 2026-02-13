package com.example.wordmanagefilesystem.Pojo.Report;

import lombok.Data;

import java.util.List;

/*
* {
      name: '公共单词总数',
      type: 'line',
      stack: 'Total',
      data: [150, 301, 334, 390, 932]
  }*/

@Data
public class DataOverLook {
    private String name;
    private String type;
    private String stack;
    private List<Integer> data;

    //设置默认值，echart图标需要！
    {
        type = "line";
        stack = "Total";
    }

    public DataOverLook() {
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

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public DataOverLook(String name, String type, String stack, List<Integer> data) {
        this.name = name;
        this.type = type;
        this.stack = stack;
        this.data = data;
    }
}
