package com.example.wordmanagefilesystem.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface WordReportDataMapper {

    @Select("select 'accuracy < 60' as accuracy , count(*) as num from vocabulary where accuracy < 60")
    Map<String , Object> getLessCount();

    //不同准确率的所有单词
    @Select("select\n" +
            "    case when accuracy < 60 then '生疏'\n" +
            "        when accuracy >=60 and accuracy < 80 then '良好'\n" +
            "        when accuracy >= 80 and accuracy < 90 then '优'\n" +
            "        when accuracy >=90 and accuracy <=100 then '易如反掌' end as accuracy_all,\n" +
            "    count(*) as num\n" +
            "    from public_vocabulary group by accuracy_all order by num")
    List<Map<String , Object>> getAllAccuracyAndCount();
}
