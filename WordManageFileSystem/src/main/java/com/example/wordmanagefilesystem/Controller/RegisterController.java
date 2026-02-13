package com.example.wordmanagefilesystem.Controller;

import com.example.wordmanagefilesystem.Pojo.Register;
import com.example.wordmanagefilesystem.Pojo.Result;
import com.example.wordmanagefilesystem.Service.Implement.CheckValidUtil;
import com.example.wordmanagefilesystem.Service.Implement.RegisterImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterImpl registerImpl;

    @PostMapping("/insert")
    public Result registerInsert(@RequestBody Register register){
        Result result = new Result();
        if (CheckValidUtil.isNull(register)){
            return result.registerError();
        }
        Register registerResult = registerImpl.registerInsert(register);
        if (registerResult.getResultRow() < 0){
            return result.registerRepeat(registerResult);
        }
        return result.registerSuccess(registerResult);
    }

}
