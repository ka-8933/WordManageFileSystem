package com.example.wordmanagefilesystem.Mapper;

import com.example.wordmanagefilesystem.Pojo.LoginMsg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {

    //判断注册是否输入正确
    @Select("select case when count(*) > 0 then 'true' else 'false' end as 'exists'\n" +
            "from user u where u.username = #{username} and u.password = #{password}")
    boolean isLoginCorrection(@Param("username") String username ,@Param("password") String password);

    @Options(useGeneratedKeys = true , keyProperty = "id" , keyColumn = "id")
    @Select("select id , username , password from user where username = #{username} and password = #{password}")
    LoginMsg selectUserMag(@Param("username") String username ,@Param("password") String password);
}
