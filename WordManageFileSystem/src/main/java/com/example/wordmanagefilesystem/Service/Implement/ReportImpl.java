package com.example.wordmanagefilesystem.Service.Implement;

import com.example.wordmanagefilesystem.Mapper.ReportMapper;
import com.example.wordmanagefilesystem.Pojo.Report.*;
import com.example.wordmanagefilesystem.Service.ReportService;
import com.example.wordmanagefilesystem.Service.StringTempToolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
@Slf4j
@Service
public class ReportImpl implements ReportService {

    private static final CommonTool commonTool = new CommonTool();

    private static final StringTempTool stringTemoTool = new StringTempTool();

    @Autowired
    private ReportMapper reportMapper;

    /*
    * “数据总览”处理*/
    //返回“数据总览”EChart的Json数据
    @Override
    public List<DataOverLook> dataOverLookHander(){
        List<Map<String, Object>> allOverLookData = reportMapper.getAllOverLookData();
        StringTempToolService stringTempToolService = new StringTempTool();
        List<DataOverLook> collectResult = allOverLookData.stream().map(dataMap -> {
            String dataString = (String) dataMap.get("data");
            List<Integer> dataList = stringTempToolService.sqlJsonArrayStringToListInteger(dataString);
            DataOverLook item = new DataOverLook(
                    (String) dataMap.get("name") , "line" , "Total" , dataList);
            return item;
        }).collect(Collectors.toList());
        return collectResult;
    }

    /*处理每凌晨0点更新数据*/
    @Transactional
    @Scheduled(cron = "0 * * * * ?")
    public void refreshOverLookDataInZeroOClock(){
        int[] getOverLookNewData = getOverLookTextDataOriginalData();//获得最新数据数组
        //获得更改好的单一数组
        int[] publicWordTotalToArray = overLookSingleDataToIntArray("公共单词总数" , getOverLookNewData[0]);
        int[] userAccuracyAvgToArray = overLookSingleDataToIntArray("用户平均准确率" , getOverLookNewData[1]);
        int[] reedAvgToArray = overLookSingleDataToIntArray("平均阅读总数" , getOverLookNewData[2]);
        int[] userTotalToArray = overLookSingleDataToIntArray("用户总数" , getOverLookNewData[3]);
        //插入更新，一一对应
        updateReportDate(publicWordTotalToArray , "公共单词总数");
        updateReportDate(userAccuracyAvgToArray , "用户平均准确率");
//        updateReportDate(reedAvgToArray , "平均阅读总数");
        updateReportDate(userTotalToArray , "用户总数");
    }

    //得到最新数据的原始数据
    private int[] getOverLookTextDataOriginalData(){
        Integer publicWordTotal = reportMapper.getPublicWordTotal();
        double userAccuracyAvg = reportMapper.getUserAccuracyAvg();
        BigDecimal userAccuracyAvgNoDecimal = commonTool.doubleToBigDecimalNoDecimal(userAccuracyAvg);
        Integer userTotal = reportMapper.getUserTotal();
        if (CheckValidUtil.isNull(publicWordTotal)||CheckValidUtil.isNull(userAccuracyAvgNoDecimal)
                ||CheckValidUtil.isNull(userTotal)){
            log.error("\"数据总览\"文本数据 数据为空");
            return null;
        }
        int userAccuracyAvgToInteger = Integer.parseInt(String.valueOf(userAccuracyAvgNoDecimal)); //数据库是int类型
        OverLookOriginalData overLookOriginalData = new OverLookOriginalData(publicWordTotal
                , userAccuracyAvgToInteger , null , userTotal);
        int[] overLookOriginalDataIntArrary = originalDataToIntArray(overLookOriginalData);
        return overLookOriginalDataIntArrary;
    }

    //将原始数据转化为int[] 类型
    private int[] originalDataToIntArray(OverLookOriginalData overLookOriginalData){
        return new int[]{overLookOriginalData.getWordTotal() , overLookOriginalData.getUserAccuracyAvg()
                , 0 , overLookOriginalData.getUserTotal()};
    }

    /*获得数据库表 report_date 单一数据*/
    private OverLookSingleData getOverLookWordTotal(String name){
        OverLookSingleData reportDataTableSingle = reportMapper.getReportDataTableSingle(name);
        if (CheckValidUtil.isNull(reportDataTableSingle)){
            log.error("获取数据可视化的“数据总览” 数据{} 的单一数据 为null",name);
        }
        return reportDataTableSingle; //result: [352, 675, 523, 645, 689]
    }

    //将 OverLookSingleData 转化为 int[]，并且用换位算法处理 （可以直接调用此函数 直接获得转化为int[]的单一行数据）
    private int[] overLookSingleDataToIntArray(String name , Integer newData){
        OverLookSingleData overLookSingleDataOriginal = getOverLookWordTotal(name);
        int[] overLookSingleDataOriginalToArrary = new int[]{overLookSingleDataOriginal.getFourAgo()
                , overLookSingleDataOriginal.getThirdAgo(), overLookSingleDataOriginal.getTwoAgo() ,
                overLookSingleDataOriginal.getOneAgo() , overLookSingleDataOriginal.getToday()};
        int[] result = refreshReplaceAlgorithm(overLookSingleDataOriginalToArrary, newData);
        return result;
    }

    //修改 report_date 的数据
    private void updateReportDate(int[] updateEndResult , String name){
        reportMapper.updateReportDate(updateEndResult[0] , updateEndResult[1] ,updateEndResult[2]
                , updateEndResult[3] , updateEndResult[4] , name);
    }

    //"数据总览"文本数据（用于前端渲染数据）
    public OverLook getOverLookTextData(){
        Integer publicWordTotal = reportMapper.getPublicWordTotal();
        double userAccuracyAvg = reportMapper.getUserAccuracyAvg();
        Integer userTotal = reportMapper.getUserTotal();
        if (CheckValidUtil.isNull(publicWordTotal)||CheckValidUtil.isNull(userAccuracyAvg)
                ||CheckValidUtil.isNull(userTotal)){
            log.error("\"数据总览\"文本数据 数据为空");
            return null;
        }
        BigDecimal userAccuracyAvgFormatTwo = commonTool.doubleToBigDecimalNoDecimal(userAccuracyAvg);
        String userAccuracyAvgResult = String.valueOf(userAccuracyAvgFormatTwo) + "%";
        return new OverLook(publicWordTotal , userAccuracyAvgResult
                , null , userTotal);
    }

    /*
    * 准确率EChart图表数据处理*/
    @Override
    public List<AccuracyGroupByBody> accuracyGroupByJson(Integer userId){
        List<Map<String, Object>> userAccuracyGroupByData = reportMapper.getUserAccuracyGroupByData(userId);
        if (CheckValidUtil.isValid(userAccuracyGroupByData)){
            log.warn("用户{} 获得 准确率EChart图表数据处理出现问题",userId);
            return null;
        }
        List<AccuracyGroupByBody> collect = userAccuracyGroupByData.stream().map(resultMap -> {
            AccuracyGroupByBody accuracyGroupByBody = new AccuracyGroupByBody((Long) resultMap.get("num")
                    , (String) resultMap.get("accuracy_grade"));
            return accuracyGroupByBody;
        }).collect(Collectors.toList());
        return collect;
    }

    /* 每日抽查单词可视化EChart数据*/
    //获得可视化Json格式数据
    @Override
    public List<CheckEchartJson> getCheckDataEchartJson(Integer userId){
        CheckReportBody checkReportBody = reportMapper.getCheckReportBody(userId);
        if (CheckValidUtil.isNull(checkReportBody)){
            log.error("获取每日抽查单词可视化EChart数据有问题！！");
            return null;
        }
        List<Integer> data = stringTemoTool.sqlJsonArrayStringToListInteger((String) checkReportBody.getData());
        List<CheckEchartJson> checkEchartJsonList = new ArrayList<>();
        String yearMonthAndDay = getYearMonthAndDay(checkReportBody.getTime());
        CheckEchartJson checkEchartJson = new CheckEchartJson(yearMonthAndDay, "bar", data);
        checkEchartJsonList.add(checkEchartJson);
        return checkEchartJsonList;
    }

    //获得年月日
    private String getYearMonthAndDay(LocalDateTime now){
        String year = String.valueOf(now.getYear());
        String month = String.valueOf(now.getMonthValue());
        String dayOfMonth = String.valueOf(now.getDayOfMonth());
        String result = year+"年"+month+"月"+dayOfMonth+"日";
        return result;
    }

    //换位算法
    private static int[] refreshReplaceAlgorithm(int[] data , Integer newData){
        int[] newIntArr = new int[data.length];
        int dataIndex = 1;
        int newIntArrIndex = 0;
        for (int i = 0; i < data.length; i++) {
            if (newIntArrIndex == 4){
                newIntArr[newIntArrIndex] = newData;
                return newIntArr;
            }
            newIntArr[newIntArrIndex] = data[dataIndex];
            newIntArrIndex++;
            dataIndex++;
        }
        return newIntArr;
    }

}
