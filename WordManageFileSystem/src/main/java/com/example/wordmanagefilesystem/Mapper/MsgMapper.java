package com.example.wordmanagefilesystem.Mapper;

import com.example.wordmanagefilesystem.Pojo.Msg.MsgBody;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;

@Mapper
public interface MsgMapper {

    //添加用户基础信息
    @Insert("insert into user_msg(user_id, name, phone_number, birth_day, img , grade, gender, city)\n" +
            "value (#{userId} , #{name} , #{phoneNumber} , #{birthDay} , #{img} , #{grade} , #{gender} , #{city})")
    Integer insertUserMsg(@Param("userId") Integer userId, @Param("name") String name
            , @Param("phoneNumber") String phoneNumber , @Param("birthDay") LocalDate birthDay
            , @Param("img") String img , @Param("grade") String grade
            , @Param("gender") String gender , @Param("city") String city);

    //更新用户基础信息
    @Update("update user_msg set name = #{name} , phone_number = #{phoneNumber} , birth_day = #{birthDay} " +
            ", grade = #{grade} , gender = #{gender} , city =  #{city} where user_id = #{userId}")
    Integer updateUserMsg( @Param("name") String name, @Param("phoneNumber") String phoneNumber
            , @Param("birthDay") LocalDate birthDay, @Param("grade") String grade
            , @Param("gender") String gender, @Param("city") String city
            , @Param("userId") Integer userId);

    //查询用户基础信息
    @Select("select user_id, name, phone_number, birth_day, img , grade, gender, city\n" +
            "from user_msg um left join user u on um.user_id = u.id where u.id = #{userId}")
    MsgBody getUserMsg(@Param("userId") Integer userId);

    //判断用户是否插入了数据
    @Select("select case\n" +
            "    when count(*) > 0 then 'true'\n" +
            "    when count(*) <= 0 then 'false'\n" +
            "    end as 'exists'\n" +
            "from user_msg um left join user u on um.user_id = u.id where u.id = #{userId}")
    boolean isUserMsgExists(@Param("userId") Integer userId);

    //文件单独上传（修改）
    @Update("update user_msg um set um.img = #{img} where um.user_id = #{userId}")
    Integer imgAloneUpdate(@Param("img") String img , @Param("userId") Integer userId);

    //全null插入
    @Insert("insert into user_msg(user_id, name, phone_number, birth_day , grade, gender, city)\n" +
            "value (#{userId} , null , null, null , null , null , null)")
    Integer msgNullInsert(@Param("userId") Integer userId);

}
