package com.example.wordmanagefilesystem.Pojo.Check;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/*
* select u.id , c.total , c.right_num , c.mistake_num , c.update_time from user_check_data c
    left join user u on c.user_id = u.id where u.id = 1;*/
@Slf4j
@Data
public class CheckDataOriginal {
    private Integer userId;
    private Integer total;
    private Integer rightNum;
    private Integer mistakeNum;
    private LocalDateTime updateTime;

    public CheckDataOriginal() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getRightNum() {
        return rightNum;
    }

    public void setRightNum(Integer rightNum) {
        this.rightNum = rightNum;
    }

    public Integer getMistakeNum() {
        return mistakeNum;
    }

    public void setMistakeNum(Integer mistakeNum) {
        this.mistakeNum = mistakeNum;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public CheckDataOriginal(Integer userId, Integer total, Integer rightNum, Integer mistakeNum, LocalDateTime updateTime) {
        this.userId = userId;
        this.total = total;
        this.rightNum = rightNum;
        this.mistakeNum = mistakeNum;
        this.updateTime = updateTime;
    }
}
