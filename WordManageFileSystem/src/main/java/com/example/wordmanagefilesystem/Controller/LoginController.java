package com.example.wordmanagefilesystem.Controller;

import com.example.wordmanagefilesystem.Pojo.LoginMsg;
import com.example.wordmanagefilesystem.Pojo.Result;
import com.example.wordmanagefilesystem.Service.Implement.LoginImpl;
import com.example.wordmanagefilesystem.Tool.JWTTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Cookie;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginImpl loginimpl;

    @PostMapping()
    public Result selectUserMsg(@RequestBody LoginMsg loginMsg){
        Result result = new Result();
        if (!loginimpl.isLoginCorrection(loginMsg.getUsername() , loginMsg.getPassword())){ //正确就返回true
            return result.loginDefeat();
        }
        LoginMsg login = loginimpl.login(loginMsg);
        login.toString();
        Result result1 = result.loginSuccess(login);
        return result1;
    }
}
