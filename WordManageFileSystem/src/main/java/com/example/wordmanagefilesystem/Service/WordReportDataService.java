package com.example.wordmanagefilesystem.Service;

import com.example.wordmanagefilesystem.Pojo.AllAccuracyCountReport;

import java.util.Map;

public interface WordReportDataService {

    Map<String , Object> getLessCount();

    AllAccuracyCountReport getAllAccuracyValues();
}
