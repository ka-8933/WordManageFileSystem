package com.example.wordmanagefilesystem.Mapper;

import com.example.wordmanagefilesystem.Pojo.Setting.SettingBody;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SettingMapper {

    /*
    * 用户设置mapper*/

    //查询用户setting
    @Select("select user_id , is_save_page , save_page from user_setting us \n" +
            "left join user u on us.user_id = u.id where u.id = #{userId}")
    SettingBody getUserSettingData(@Param("userId") Integer userId);

    //插入用户setting
    @Insert("insert into user_setting(user_id, is_save_page, save_page)\n" +
            "    value (#{userId} , #{isSavePage} , #{savePage});")
    Integer insertUserSetting(@Param("userId") Integer userId
            , @Param("isSavePage") Integer isSavePage , @Param("savePage") Integer savePage);

    //修改用户setting
    @Update("update user_setting set is_save_page = #{isSavePage} , save_page = #{savePage} where user_id = #{userId}")
    Integer updateUserSetting(@Param("isSavePage") Integer isSavePage
            , @Param("savePage") Integer savePage , @Param("userId") Integer userId);

    //注册自动新增setting数据
    @Insert("insert into user_setting(user_id, is_save_page, save_page)\n" +
            "    value (#{userId} , 0 , 1)")
    Integer autoInsertSettingData(@Param("userId") Integer userId);

    //判断用户是否有Setting数据
    @Select("select \n" +
            "    case when count(*) > 0 then 'true' \n" +
            "    when count(*) <= 0 then 'false' \n" +
            "    end as 'isExists' \n" +
            "from user_setting us where us.user_id = #{userId}")
    boolean isUserSettingExists(@Param("userId") Integer userId);

}
