package com.example.wordmanagefilesystem.Pojo.Msg;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MsgBody {
    private Integer userId;
    private String name;
    private String phoneNumber;
    private LocalDate birthDay;
    private String img;
    private String grade;
    private String gender;
    private String city;

    public MsgBody() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public MsgBody(Integer userId, String name, String phoneNumber, LocalDate birthDay, String img, String grade, String gender, String city) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.img = img;
        this.grade = grade;
        this.gender = gender;
        this.city = city;
    }
}
