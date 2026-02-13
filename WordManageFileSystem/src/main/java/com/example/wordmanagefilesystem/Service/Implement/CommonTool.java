package com.example.wordmanagefilesystem.Service.Implement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;

/*
* 这个类是通用工具类*/
public class CommonTool {

    //用于将double处理成保留两位小数
    public double doubleHandle(double accuracy) {
        double remainDotTwoF = Double.parseDouble(String.format("%.2f", accuracy));
        return remainDotTwoF * 100;
    }

    //Decimal更精准保留两位小数处理
    public BigDecimal doubleToBigDecimal(double accuracy){
        if (CheckValidUtil.isNull(accuracy)){
            return null;
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(accuracy);
        BigDecimal bigDecimalHalfUp = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        return bigDecimalHalfUp;
    }

    //Decimal更精准保留两位小数处理，并且*100
    public BigDecimal bigdecimalAccuracy(double accuracy){
        if (CheckValidUtil.isNull(accuracy)){
            return null;
        }
        BigDecimal bigDecimal = BigDecimal.valueOf((accuracy * 100));
        BigDecimal bigDecimalHalfUp = bigDecimal.setScale(0, RoundingMode.HALF_UP);
        return bigDecimalHalfUp;
    }

    //Decimal更精准保留两位小数处理
    public BigDecimal doubleToBigDecimalNoDecimal(double accuracy){
        if (CheckValidUtil.isNull(accuracy)){
            return null;
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(accuracy);
        BigDecimal bigDecimalHalfUp = bigDecimal.setScale(0, RoundingMode.HALF_UP);
        return bigDecimalHalfUp;
    }

    /*
     * 时间戳校验，实时对比昨天数据
     * 将具体的LocalDateTime转化为时间戳*/
    public long localDateTimeToTimeStamp(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        return localDateTime.atZone(zoneId).toInstant().toEpochMilli();
    }

}
