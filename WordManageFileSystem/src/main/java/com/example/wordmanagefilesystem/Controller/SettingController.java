package com.example.wordmanagefilesystem.Controller;

import com.example.wordmanagefilesystem.Pojo.Result;
import com.example.wordmanagefilesystem.Pojo.Setting.SettingBody;
import com.example.wordmanagefilesystem.Service.Implement.SettingImpl;
import com.example.wordmanagefilesystem.Tool.JWTTool;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("setting")
public class SettingController {

    @Autowired
    private SettingImpl settingImpl;

    @Autowired
    private JWTTool jwtTool;

    /*
    *用户设置 */

    //查询用户setting
    @GetMapping()
    public Result getUserSettingData(@RequestHeader("userToken") String token){
        Result result = new Result();
        Claims claims = jwtTool.parseToken(token);
        SettingBody settingData = settingImpl.getUserSettingData((Integer) claims.get("id"));
        if (settingData == null){
            return result.settingError(settingData);
        }
        return result.settingSuccess(settingData);
    }

    //插入用户setting
    @PostMapping()
    public Result insertUserSetting(@RequestHeader("userToken") String token
            , @RequestParam Integer isSavePage , @RequestParam Integer savePage){
        Result result = new Result();
        Claims claims = jwtTool.parseToken(token);
        boolean isSuccess = settingImpl.insertUserSetting((Integer) claims.get("id"), isSavePage, savePage);
        return result.settingSuccess(isSuccess);
    }

    //修改用户setting
    @PutMapping()
    public Result updateUserSetting(@RequestBody SettingBody settingBody , @RequestHeader("userToken") String token){
        Result result = new Result();
        Claims claims = jwtTool.parseToken(token);
        boolean isSuccess = settingImpl.updateUserSetting(settingBody.getIsSavePage()
                , settingBody.getSavePage(), (Integer) claims.get("id"));
        return result.settingSuccess(isSuccess);
    }
}
