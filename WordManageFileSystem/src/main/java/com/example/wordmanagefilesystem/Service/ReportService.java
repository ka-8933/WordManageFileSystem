package com.example.wordmanagefilesystem.Service;

import com.example.wordmanagefilesystem.Pojo.Report.AccuracyGroupByBody;
import com.example.wordmanagefilesystem.Pojo.Report.CheckEchartJson;
import com.example.wordmanagefilesystem.Pojo.Report.DataOverLook;

import java.util.List;

public interface ReportService {
    public List<DataOverLook> dataOverLookHander();
    public List<AccuracyGroupByBody> accuracyGroupByJson(Integer userId);
    public List<CheckEchartJson> getCheckDataEchartJson(Integer userId);
}
