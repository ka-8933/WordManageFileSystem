package com.example.wordmanagefilesystem.Mapper;

import com.example.wordmanagefilesystem.Pojo.Word;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WordRelateUserMapper {

    @Select("select v.word , v.meaning , v.part_of_speech , v.belong_grade , v.similar_word , v.phrase , wa.accuracy\n" +
            "from vocabulary v left join word_acuuracy wa on v.id = wa.word_id left join user u on wa.user_id = u.id and u.id = #{userId}")
    List<Word> getAllWordRalateUserID(Integer userId);


}
