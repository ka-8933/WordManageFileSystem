package com.example.wordmanagefilesystem.Pojo.Report;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CheckReportBody {
    private LocalDateTime time;
    private Object data;

    public CheckReportBody() {
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public CheckReportBody(LocalDateTime time, Object data) {
        this.time = time;
        this.data = data;
    }
}
