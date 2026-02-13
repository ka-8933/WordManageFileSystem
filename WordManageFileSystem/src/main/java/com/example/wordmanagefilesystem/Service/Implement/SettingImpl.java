package com.example.wordmanagefilesystem.Service.Implement;

import com.example.wordmanagefilesystem.Mapper.SettingMapper;
import com.example.wordmanagefilesystem.Pojo.Setting.SettingBody;
import com.example.wordmanagefilesystem.Service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SettingImpl implements SettingService {

    @Autowired
    private SettingMapper settingMapper;

    /*
    * 用户设置*/

    //查询用户setting
    public SettingBody getUserSettingData(Integer userId){
        SettingBody userSettingData = settingMapper.getUserSettingData(userId);
        if (CheckValidUtil.isNull(userSettingData)){
            log.warn("查询用户setting：用户信息出现问题{}" , userSettingData);
            return null;
        }
        return userSettingData;
    }

    //插入用户setting
    public boolean insertUserSetting(Integer userId , Integer isSavePage , Integer savePage){
        Integer integerResult = settingMapper.insertUserSetting(userId, isSavePage, savePage);
        if (integerResult > 0){
            log.info("用户{}新增setting成功！！" , userId);
            return true;
        }
        log.warn("用户{}新增setting失败！！" , userId);
        return false;
    }

    //修改用户setting
    public boolean updateUserSetting( Integer isSavePage , Integer savePage , Integer userId){
        Integer updateResult = settingMapper.updateUserSetting(isSavePage, savePage, userId);
        if (updateResult > 0){
            log.info("用户{}修改setting成功！！" , userId);
            return true;
        }
        log.warn("用户{}修改setting失败！！" , userId);
        return false;
    }

}
