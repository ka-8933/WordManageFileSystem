package com.example.wordmanagefilesystem.Service.Implement;

import com.example.wordmanagefilesystem.Mapper.RegisterMapper;
import com.example.wordmanagefilesystem.Mapper.SettingMapper;
import com.example.wordmanagefilesystem.Pojo.Register;
import com.example.wordmanagefilesystem.Pojo.User;
import com.example.wordmanagefilesystem.Service.RegisterService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class RegisterImpl implements RegisterService {

    @Autowired
    private RegisterMapper registerMapper;

    @Autowired
    private SettingMapper settingMapper;

    //User缓存信息
    private static Map<Integer, User> userMessageCache = new ConcurrentHashMap<>();

    //开启启动User信息缓存
    @PostConstruct
    public void startUserMessageCache() {
        List<User> allUserMsg = registerMapper.getAllUserMsg();
        if (CheckValidUtil.isValid(allUserMsg)) {
            return;
        }
        Integer index = 0;
        Iterator<User> iterator = allUserMsg.iterator();
        while (iterator.hasNext()){
            userMessageCache.put(index , iterator.next());
            index++;
        }
    }

    //更新用户信息缓存
    private void refreshUserMessageCache(){
        List<User> allUserMsg = registerMapper.getAllUserMsg();
        if (CheckValidUtil.isValid(allUserMsg)) {
            return;
        }
        Integer index = 0;
        Iterator<User> iterator = allUserMsg.iterator();
        while (iterator.hasNext()){
            userMessageCache.put(index , iterator.next());
            index++;
        }
    }

    //注册：添加新用户
    @Override
    @Transactional
    public Register registerInsert(Register register) {
        if (isRepeatUsername(register.getUsername())){
            register.setRegisterCondition("用户名重复!");
            register.setResultRow(0);
            return register;
        }
        Integer resultRow = registerMapper.registerInsert(register.getUsername(), register.getPassword());
        if (CheckValidUtil.isNull(resultRow) || resultRow <= 0) {
            register.setRegisterCondition("用户注册时数据库出现问题！");
            log.error("用户注册时数据库出现问题:{} , 操作用户暂命名为{}", resultRow, register.getUsername());
            return register;
        }
        register.setResultRow(resultRow);
        register.setRegisterCondition("用户注册成功！");
        refreshUserMessageCache();
        regiterAutoInsertYesterday(register.getUsername());
        autoInsertSettingData(register.getUsername());
        return register;
    }

    //用户注册后自动添加用户的 yesterday 数据
    @Transactional
    public void regiterAutoInsertYesterday(String username){
        Integer userId = registerMapper.getUserIdByUsername(username);
        if (CheckValidUtil.isNull(userId)){
            return;
        }
        registerMapper.insertUserYesterdayData(userId , LocalDateTime.now());
    }

    //判断用户名字是否重复
    private boolean isRepeatUsername(String username){
        boolean isUsernameRepeat = registerMapper.isUsernameRepeat(username);
        if (CheckValidUtil.isNull(isUsernameRepeat)){
            return true;
        }
        return isUsernameRepeat;
    }

    /*
    * setting初始化*/

    //用户注册自动插入setting数据
    public void autoInsertSettingData(String username){
        Integer userId = registerMapper.getUserIdByUsername(username);
        if (CheckValidUtil.isNull(userId)){
            return;
        }
        Integer autoInsertResult = settingMapper.autoInsertSettingData(userId);
        if (autoInsertResult > 0){
            log.info("用户注册自动插入setting数据成功！！");
            return;
        }
        log.warn("用户注册自动插入setting数据失败！");
        return;
    }

}
