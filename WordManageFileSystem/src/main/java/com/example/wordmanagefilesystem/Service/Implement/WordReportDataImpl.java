package com.example.wordmanagefilesystem.Service.Implement;

import com.example.wordmanagefilesystem.Mapper.WordReportDataMapper;
import com.example.wordmanagefilesystem.Pojo.AllAccuracyCountReport;
import com.example.wordmanagefilesystem.Service.WordReportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WordReportDataImpl implements WordReportDataService {

    @Autowired
    private WordReportDataMapper wordReportDataMapper;

    @Override
    public Map<String, Object> getLessCount() {
        Map<String, Object> lessCount = wordReportDataMapper.getLessCount();
        return lessCount;
    }

    @Override
    public AllAccuracyCountReport getAllAccuracyValues() {
        List<Map<String, Object>> allAccuracyAndCount = wordReportDataMapper.getAllAccuracyAndCount();
        //先得到所有accuracy的区分名
        List<Object> accuracy = allAccuracyAndCount.stream().map(mapData -> mapData.get("accuracy_all")).toList();
        //再得到对应的值
        List<Object> num = allAccuracyAndCount.stream().map(mapData -> mapData.get("num")).toList();

        return new AllAccuracyCountReport(accuracy , num);
    }
}
