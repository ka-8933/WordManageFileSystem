package com.example.wordmanagefilesystem.Mapper;

import com.example.wordmanagefilesystem.Pojo.Check.CheckDataOriginal;
import com.example.wordmanagefilesystem.Pojo.Word;
import com.example.wordmanagefilesystem.Pojo.WordAccuracyCombineVocabulary;
import com.example.wordmanagefilesystem.Pojo.YesterdayData;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface WordMapper {

    //查询所有公共单词
    @Select("select * from public_vocabulary")
    List<Word> getAllWord();

    //修改公共单词通过id
    @Update("update public_vocabulary set word = #{word}, meaning = #{meaning} , part_of_speech = #{partOfSpeech} ," +
            " belong_grade = #{belongGrade} , similar_word = #{similarWord} , phrase = #{phrase} where id = #{id}")
    Integer updatePublicVocabulary(@Param("word") String word , @Param("meaning") String meaning
            , @Param("partOfSpeech") String  partOfSpeech, @Param("belongGrade") String belongGrade
            , @Param("similarWord") String similarWord, @Param("phrase") String phrase, @Param("id") Integer id);

    //删除公共单词
    @Delete("delete from public_vocabulary v where v.id = #{id}")
    Integer deletePublicWordById(@Param("id") Integer id);

    //添加公共单词
    @Insert("insert into public_vocabulary(word, meaning, part_of_speech, belong_grade, similar_word, phrase)\n" +
            "value(#{word} , #{meaning} , #{part_of_speech} , #{belong_grade} , #{similar_word} , #{phrase} )")
    Integer addPublicWord(@Param("word") String word , @Param("meaning") String meaning
            , @Param("part_of_speech") String part_of_speech , @Param("belong_grade") String belong_grade
            , @Param("similar_word") String similar_word , @Param("phrase") String phrase);

    //通过id获取单词所有信息
    @Select("select * from public_vocabulary pv where id = #{id}")
    Word getPublicWordById(@Param("id") Integer id);

    //通过word得到此单词的所有信息（公共单词表）
    @Select("select * from public_vocabulary where word = #{word};")
    Word getWordByWordIsPublic(String word);

    //根据单词，释义，阶段，词性，分别查询单词
    @Select("SELECT v.id , v.word , v.meaning , v.part_of_speech , v.belong_grade , v.similar_word , v.phrase ,  wa.accuracy\n" +
            "     , wa.total , wa.is_right FROM public_vocabulary v LEFT JOIN word_accuracy wa ON v.id = wa.word_id AND wa.user_id = #{userId}\n" +
            "where v.word = #{word} or v.meaning = #{meaning} or v.part_of_speech = #{partOfSpeech} or v.belong_grade = #{belongGrade} \n" +
            "limit #{start} , #{size}")
    List<Word> queryWordByCondition(@Param("word") String word , @Param("meaning") String meaning
            , @Param("partOfSpeech") String partOfSpeech , @Param("belongGrade") String belongGrade
            , @Param("userId") Integer userId,@Param("start") Integer start,@Param("size") Integer size);

    /*
     * 这里是获取首页四组信息的mapper数据库处理
     * */

    //得到用户所有
    @Select("select count(*) from private_vocabulary pv where pv.user_id = #{userId};")
    Integer getAllAddWordQuatity(@Param("userId") Integer userId);

    /*
     * 这里得到用户平均单词准确率，sql层直接查询获得*/
    @Select("select avg(accuracy) from word_accuracy where user_id = #{userId}")
    Double getUserAverageAccuracy(@Param("userId") Integer userId);

    //获取用户公共单词小于准确率60的总数
    @Select("select count(*) from word_accuracy wa left join user u on wa.user_id = u.id\n" +
            "left join public_vocabulary on wa.word_id = public_vocabulary.id\n" +
            "where u.id = #{userId} and (wa.accuracy < 60);")
    Integer getLessScoreQuatity(@Param("userId") Integer userId);

    //官方单词总数
    @Select("select count(*) from public_vocabulary")
    Integer getOfficialsQuatity();

    /*
    * 实时数据更新功能mapper（相对每次过24小时更新）
    * 先获得yesterday时间*/
    @Select("select yd.latest_update_time from yesterday_data yd where yd.userId = #{userId}")
    LocalDateTime getYesterdayLocalDateTime(@Param("userId") Integer userId);

    //得到每个用户id
    @Select("select u.id from user u")
    List<Integer> getAllUserId();

    //通过userId得到用户名
    @Select("select u.username from user u where id = #{userId}")
    String getUsernameByUserId(@Param("userId") Integer userId);

    /*
    * 修改 yesterday 的数据*/
    @Update("update yesterday_data yd set yesterday_accuracy = #{accuracy} , " +
            "yesterday_new_word = #{newWord} , yesterday_officials_total = #{officialsTotal} , " +
            "yesterday_less_word = #{lessWord} , latest_update_time = #{localDateTime} where yd.userId = #{userId};")
    void updateYesterdayDateToCurrentData(@Param("accuracy") double accuracy , @Param("newWord") Integer newWord ,
                                          @Param("officialsTotal") Integer officialsTotal , @Param("lessWord") Integer lessWord ,
                                          @Param("localDateTime") LocalDateTime localDateTime , @Param("userId") Integer userId);

    /*
    * 通过用户id获得该用户的Yesterday所有数据*/
    @Select("select * from yesterday_data yd where yd.userId = #{userId}")
    YesterdayData getYesterdayDataByUserId(@Param("userId") Integer userId);

    //这里通过连表查询该用户对应的word_accuracy表和vocabulary表，的所有数据
    @Select("select v.id , v.word , v.meaning , v.part_of_speech , v.belong_grade , v.similar_word , " +
            "v.phrase , v.belong_grade , wa.accuracy , wa.total , wa.is_right from public_vocabulary v " +
            "left join word_accuracy wa on v.id = wa.word_id and wa.user_id = #{userId}\n" +
            "limit #{start} , #{pageSize}")
    List<Word> getUserAllWordPage(
            @Param("start") Integer start ,@Param("pageSize") Integer pageSize ,@Param("userId") Integer userId);

    /*
    * 这里通过word和用户id判断，该用户的这个单词是否在word_accuracy表中已经创建了
    * 数据 ， 并且返回true或false*/
    @Select("select case when exists(select 1 from word_accuracy wa\n" +
            "left join public_vocabulary v on wa.word_id = v.id where user_id = #{userId} and wa.word_id =\n" +
            "(select v.id from public_vocabulary v where word = #{word})) then 'true' else 'false' end as isExists ;")
    boolean wordIsCreateWordAccuracyTable(@Param("word") String word ,@Param("userId") Integer userId);

    /*
    * 如果不存在就插入新的数据，再通过boolean isRight判断插入的数据的正确初始值为1 ， 还是为0
    * 核心语句：“if(#{isRight} , 1 , 0)”*/
    @Insert("insert into word_accuracy(user_id , word_id , total , is_right , accuracy )" +
            "values(#{userId} , (select v.id from public_vocabulary v " +
            "where word = #{word}) , 1 , if(#{isRight} , 1 , 0), if(#{isRight} , 100 , 0));")
    Integer addNewwordAccuracy(@Param("word") String word ,@Param("userId") Integer userId ,@Param("isRight") boolean isRight);

    /*
    * 通过答对或答错，来获得该用户此单词的最新准确率*/
    @Select("select if(#{isRight} , ((wa.is_right + 1)/ (wa.total + 1)) , ((wa.is_right)/ (wa.total + 1))) " +
            "from word_accuracy wa where user_id = #{userId} and word_id = (select v.id from public_vocabulary v " +
            "where word = #{word} );")
    double getWordLatestAccuracy(
            @Param("word") String word ,@Param("userId") Integer userId ,@Param("isRight") boolean isRight);

    /*
    * 修改单词准确率*/
    @Update("update word_accuracy wa " +
            "set total = (wa.total + 1) , is_right = if(#{isRight} , (wa.is_right + 1) , wa.is_right) , accuracy = #{accuracy} " +
            "where user_id = #{userId} and word_id = (select v.id from public_vocabulary v where word = #{word});")
    Integer updateWordAcuuracyTable(
            @Param("isRight") boolean isRight,@Param("accuracy") BigDecimal accuracy ,
            @Param("userId") Integer userId , @Param("word") String word );

    /*
    * 通过单词获取word_accuracy信息*/
    @Select("select v.id , v.word , v.meaning , wa.total , wa.is_right , wa.accuracy , wa.user_id from word_accuracy wa\n" +
            "left join public_vocabulary v on wa.word_id = v.id where word = #{word} and wa.user_id = #{userId}")
    WordAccuracyCombineVocabulary getWordAccuracyByWordAndUserId(
            @Param("word") String word ,
            @Param("userId") Integer userId);

    /*
    * 抽查数据处理*/

    //新增用户 user_check_data 数据
    @Insert("insert into user_check_data(user_id, total, right_num, mistake_num, update_time)\n" +
            "    value (#{userId} , #{total} , #{rightNum} , #{mistakeNum} , #{updateTime})")
    Integer insertCheckData(@Param("userId") Integer userId , @Param("total") Integer total
            ,@Param("rightNum") Integer rightNum ,@Param("mistakeNum") Integer mistakeNum
            ,@Param("updateTime") LocalDateTime updateTime );

    //
    @Update("update user_check_data\n" +
            "set total = #{total} , right_num = #{rightNum} , mistake_num = #{mistakeNum} , update_time = #{updateTime}\n" +
            "where user_check_data.user_id = #{userId}")
    Integer updateCheckData(@Param("total") Integer total , @Param("rightNum") Integer rightNum
            ,@Param("mistakeNum") Integer mistakeNum ,@Param("updateTime") LocalDateTime updateTime
            ,@Param("userId") Integer userId );

    //CheckData 是用户否之前存在数据
    @Select("select count(*) from user_check_data where user_id = #{userId}")
    Long isUserCheckDataExists(@Param("userId") Integer userId);

    //获得原本的 CheckData 用户数据
    @Select("select u.id , c.total , c.right_num , c.mistake_num , c.update_time from user_check_data c\n" +
            "left join user u on c.user_id = u.id where u.id = #{userId}")
    CheckDataOriginal getCheckDataOriginal(@Param("userId") Integer userId);

}
