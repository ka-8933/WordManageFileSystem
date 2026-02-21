package com.example.wordmanagefilesystem.Mapper;

import com.example.wordmanagefilesystem.Pojo.Word;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CheckMapper {

    //得到分页单词
    @Select("select * from public_vocabulary limit #{page} , #{pageSize}")
    List<Word> getLimitWord(@Param("page") Integer page , @Param("pageSize") Integer pageSize);

    //得到所有单词的总数
    @Select("select count(*) from public_vocabulary")
    Integer getWordCount();
}
