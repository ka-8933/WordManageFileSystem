package com.example.wordmanagefilesystem.Service.Implement;

import com.example.wordmanagefilesystem.Pojo.Word;

import java.util.Collection;

/*校验null和非空工具*/
public class CheckValidUtil {

    //私有构造器：不用创建对象（new）直接调用对象中的静态方法
    private CheckValidUtil(){}

    //判断Object通用类是否为空
    public static boolean isNull(Object object){
        return object == null; //是null 就返回 true
    }

    public static boolean isValid(String string){
        return string == null || string.trim().isEmpty(); //不合理就返回 true
    }

    public static boolean isValid(Collection<?> collection){ //假如是合理的
        return collection == null || collection.isEmpty(); // 不是合理的就 返回 true
    }

    /*
    * 判断Word对象的word和meaning是否非空非null
    * @Param:word , meaning
    * @return:true , false
    * */
    public static boolean isWordValid(Word word){
        if (isNull(word)){
            return false; //假如是null ， 返回：false
        }if (isValid(word.getWord())){
            return false;
        }if (isValid(word.getMeaning())){
            return false;
        }
        return true;
    }
}
