package com.example.wordmanagefilesystem.Pojo;

import lombok.Data;

@Data
public class CheckEndBody {
    private Integer userId;
    private Integer total;
    private Integer right;

    public CheckEndBody() {
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

    public Integer getRight() {
        return right;
    }

    public void setRight(Integer right) {
        this.right = right;
    }

    public CheckEndBody(Integer userId, Integer total, Integer right) {
        this.userId = userId;
        this.total = total;
        this.right = right;
    }
}
