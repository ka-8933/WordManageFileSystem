package com.example.wordmanagefilesystem.Pojo.Setting;

import lombok.Data;

@Data
public class SettingBody { //userId , isSavePage , savePage
    private Integer userId;
    private Integer isSavePage;
    private Integer savePage;

    public SettingBody() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIsSavePage() {
        return isSavePage;
    }

    public void setIsSavePage(Integer isSavePage) {
        this.isSavePage = isSavePage;
    }

    public Integer getSavePage() {
        return savePage;
    }

    public void setSavePage(Integer savePage) {
        this.savePage = savePage;
    }

    public SettingBody(Integer userId, Integer isSavePage, Integer savePage) {
        this.userId = userId;
        this.isSavePage = isSavePage;
        this.savePage = savePage;
    }
}
