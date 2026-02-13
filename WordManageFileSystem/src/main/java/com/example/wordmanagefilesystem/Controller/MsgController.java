package com.example.wordmanagefilesystem.Controller;

import com.example.wordmanagefilesystem.Pojo.Msg.MsgBody;
import com.example.wordmanagefilesystem.Pojo.Result;
import com.example.wordmanagefilesystem.Service.Implement.MsgImpl;
import com.example.wordmanagefilesystem.Tool.JWTTool;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("msg")
public class MsgController {

    @Autowired
    private MsgImpl msgImpl;

    @Autowired
    private JWTTool jwtTool;

    //更新或新增用户基础信息
    @PutMapping("msg")
    public Result updateOrInsertByExists(@RequestHeader("userToken") String token
            ,@RequestBody MsgBody msgBody){
        Claims claims = jwtTool.parseToken(token);
        Integer R = msgImpl.updateOrInsertByExists((Integer) claims.get("id"), msgBody);
        return new Result().success(R);
    }

    //单独更新或存储头像
    @PutMapping("img")
    public Result msgImgUpload(@RequestHeader("userToken") String token
            ,@RequestParam MultipartFile file){
        Claims claims = jwtTool.parseToken(token);
        Integer R = msgImpl.msgImgUpload((Integer) claims.get("id"), file);
        return new Result().success(R);
    }

    //查询用户基础信息
    @GetMapping()
    public Result getUserMsg(@RequestHeader("userToken") String token){
        Claims claims = jwtTool.parseToken(token);
        MsgBody msg = msgImpl.getUserMsg((Integer) claims.get("id"));
        return new Result().success(msg);
    }

}
