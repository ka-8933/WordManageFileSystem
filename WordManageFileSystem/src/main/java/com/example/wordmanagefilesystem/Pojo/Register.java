package com.example.wordmanagefilesystem.Pojo;

import lombok.Data;

@Data
public class Register {
    private String username;
    private String password;

    private String registerCondition;

    private Integer resultRow;

    public Register() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegisterCondition() {
        return registerCondition;
    }

    public void setRegisterCondition(String registerCondition) {
        this.registerCondition = registerCondition;
    }

    public Integer getResultRow() {
        return resultRow;
    }

    public void setResultRow(Integer resultRow) {
        this.resultRow = resultRow;
    }

    public Register(String username, String password, String registerCondition, Integer resultRow) {
        this.username = username;
        this.password = password;
        this.registerCondition = registerCondition;
        this.resultRow = resultRow;
    }
}
