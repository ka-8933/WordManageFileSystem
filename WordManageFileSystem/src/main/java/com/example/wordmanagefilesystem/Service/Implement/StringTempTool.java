package com.example.wordmanagefilesystem.Service.Implement;

import com.example.wordmanagefilesystem.Service.StringTempToolService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class StringTempTool implements StringTempToolService {

    public static void main(String[] args) {

    }

    public StringTempTool() {
    }

    /*
     * 工具一：
     * 将sql的Json Array的数据转化为List<Integer>类型
     * 列如：String:[0,0,0,0,143] ,转化为List<Integer>
     * 先将String[0,0,0,0,143]变成String[] [0,0,0,0,143] 再变成
     * */
    @Override
    public List<Integer> sqlJsonArrayStringToListInteger(String jsonArray){
        String[] strings = sqlJsonArrayStringToStringArray(jsonArray);
        int[] ints = stringArrToIntArr(strings);
        List<Integer> list = intArrToListInteger(ints);
        return list;
    }


    /*
    * 将String[0,0,0,0,143] 变成 String[] [0,0,0,0,143]*/
    private static String[] sqlJsonArrayStringToStringArray(String jsonArray){ //[23,21,32,44,143]
        String stringRemoveBracket = sqlJsonArrayStringRemoveBracket(jsonArray); // 23,21,32,44,143
        String[] split = stringRemoveBracket.split(",");

        return split; //String[] : [23,21,32,44,143]
    }

    /*
    * 将String[] 转化为 int[]*/
    private static int[] stringArrToIntArr(String[] stringArr){
        int[] getStringArrToIntArr = new int[stringArr.length];
        for (int i = 0; i < stringArr.length; i++) {
            getStringArrToIntArr[i] = Integer.parseInt(stringArr[i].trim());
        }
        return getStringArrToIntArr;
    }

    /*
    * 将int[] 每个数据存入同一个 List集合*/
    private static List<Integer> intArrToListInteger(int[] intArr){
        List<Integer> getIntArrToListInteger = new ArrayList<>();
        for (int i = 0; i < intArr.length; i++) {
            getIntArrToListInteger.add(intArr[i]);
        }
        return getIntArrToListInteger;
    }

    /*
     * 将String:[0,0,0,0,143] 去括号变成String:0,0,0,0,143*/
    private static String sqlJsonArrayStringRemoveBracket(String jsonArray){
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.charAt(i) == '[' || jsonArray.charAt(i) == ']'){
                continue;
            }
            stringBuilder.append(String.valueOf(jsonArray.charAt(i)));
        }
        return stringBuilder.toString();
    }

    /*
    * 记录String：0,0,0,0,143 每个','的坐标*/
    private static int[] getEveryDotIndex(String jsonArray){ // 0,0,0,0,143
        int[] arr = new int[4];
        int index = 0;
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.charAt(i) == ','){
                arr[index] = i;
                index++;
            }
        }
        return arr; //[1, 3, 5, 7]
    }

}
