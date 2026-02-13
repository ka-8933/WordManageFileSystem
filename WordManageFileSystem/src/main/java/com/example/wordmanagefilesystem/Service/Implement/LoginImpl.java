package com.example.wordmanagefilesystem.Service.Implement;

import com.example.wordmanagefilesystem.Mapper.LoginMapper;
import com.example.wordmanagefilesystem.Pojo.LoginMsg;
import com.example.wordmanagefilesystem.Service.LoginService;
import com.example.wordmanagefilesystem.Tool.JWTTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Component
public class LoginImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    //令牌工具类
    @Autowired
    private JWTTool jwtTool;

    //用户是否输入正确判断
    public boolean isLoginCorrection(String username , String password){
        boolean loginCorrection = loginMapper.isLoginCorrection(username, password);
        return loginCorrection;
    }

    //验证用户登录信息
    @Override
    public LoginMsg login(LoginMsg loginMsg) {
        LoginMsg loginMsg1 = loginMapper.selectUserMag(loginMsg.getUsername() , loginMsg.getPassword());
        if (CheckValidUtil.isNull(loginMsg1)){
            log.warn("验证用户登录信息，验证失败！！");
            return null;
        }
        Map<String , Object> dataMap = new HashMap<>();
        dataMap.put("id" , loginMsg1.getId());
        dataMap.put("username" , loginMsg1.getUsername());
        String token = jwtTool.generateToken(dataMap);
        loginMsg1.setToken(token);
        String s = loginMsg1.toString();
        System.out.println(s);
        return loginMsg1;
    }


}
