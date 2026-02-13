package com.example.wordmanagefilesystem.Mapper;

import com.example.wordmanagefilesystem.Pojo.Report.CheckReportBody;
import com.example.wordmanagefilesystem.Pojo.Report.OverLookSingleData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {

    /*
    * 数据总览*/

    //数据总览可视化Json
    @Select("select data_name as name , JSON_ARRAY(fourAgo , thirdAgo , twoAgo , oneAgo , today) as data from report_date")
    List<Map<String , Object>> getAllOverLookData();

    //公共单词总数
    @Select("select count(*) from public_vocabulary")
    Integer getPublicWordTotal();

    //用户平均准确率
    @Select("select avg(accuracy) from word_accuracy")
    double getUserAccuracyAvg();

    //用户平均阅读总数
    //...

    //用户总数
    @Select("select count(*) from user")
    Integer getUserTotal();

    /*
    * 获得“数据总览”每项单一数据*/

    @Select("select * from report_date where data_name = #{name}")
    OverLookSingleData getReportDataTableSingle(@Param("name") String name);

    //修改 report_date
    @Update("update report_date set fourAgo = #{fourAgo} , thirdAgo = #{thirdAgo} , twoAgo = #{twoAgo} " +
            ", oneAgo = #{oneAgo} , today = #{today} where data_name = #{name}")
    void updateReportDate(@Param("fourAgo") Integer fourAgo , @Param("thirdAgo") Integer thirdAgo , @Param("twoAgo") Integer twoAgo
            , @Param("oneAgo") Integer oneAgo , @Param("today") Integer today , @Param("name") String name);

    /*
    * 获得用户个人单词准确率分组查询*/
    @Select("select " +
            "case when wa.accuracy >= 90 then '了如指掌' " +
            "when wa.accuracy >= 80 and wa.accuracy < 90 then '优秀' " +
            "when wa.accuracy >= 60 and wa.accuracy < 80 then '良好' " +
            "when wa.accuracy < 60 then '不合格' " +
            "end as 'accuracy_grade', " +
            "count(*) as 'num' " +
            "from word_accuracy wa left join user u on wa.user_id = u.id " +
            "where u.id = #{userId}  group by accuracy_grade")
    List<Map<String , Object>> getUserAccuracyGroupByData(@Param("userId") Integer userId); // <accuracy_grade , num>
    /*
    * {”accuracy_grade“ : ”优“},
    * {“num” : 34}*/

    /*
    * 每日单词抽查可视化*/
    //查询
    @Select("select ucd.update_time as 'time' , JSON_ARRAY(ucd.right_num , ucd.mistake_num , ucd.total) as 'data'\n" +
            "from user_check_data ucd where ucd.user_id = #{userId}")
    CheckReportBody getCheckReportBody(@Param("userId") Integer userId);


}
