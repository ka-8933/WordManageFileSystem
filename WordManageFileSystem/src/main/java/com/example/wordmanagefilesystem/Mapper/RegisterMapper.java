package com.example.wordmanagefilesystem.Mapper;

import com.example.wordmanagefilesystem.Pojo.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RegisterMapper {

    //插入新用户基础信息
    @Insert("insert into user(username, password) value (#{username} , #{password})")
    Integer registerInsert(@Param("username") String username , @Param("password") String password);

    //查询所有用户
    @Select("select * from user")
    List<User> getAllUserMsg();

    //判断用户名有没有重复
    @Select("select case when exists(select * from user u where username = #{username}) " +
            "then 'true' else 'false' end as exsists")
    boolean isUsernameRepeat(@Param("username") String username);

    //根据username查询用户id
    @Options(useGeneratedKeys = true , keyProperty = "id" , keyColumn = "id")
    @Select("select u.id from user u where u.username  = #{username}")
    Integer getUserIdByUsername(@Param("username") String username);

    //每新增一个用户都要在yesterday数据插入新数据条（不然定时更新不能完成）
    @Insert("insert into yesterday_data(userid, yesterday_accuracy, yesterday_new_word\n" +
            ", yesterday_officials_total, yesterday_less_word, latest_update_time)\n" +
            "    value (#{userId} , 0.0 , 0 , 0 , 0 , #{date})")
    void insertUserYesterdayData(@Param("userId") Integer userId , @Param("date")LocalDateTime dateTime);
}
