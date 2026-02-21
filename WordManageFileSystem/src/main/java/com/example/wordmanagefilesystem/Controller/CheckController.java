package com.example.wordmanagefilesystem.Controller;

import com.example.wordmanagefilesystem.Mapper.CheckMapper;
import com.example.wordmanagefilesystem.Pojo.Result;
import com.example.wordmanagefilesystem.Pojo.Word;
import com.example.wordmanagefilesystem.Service.CheckService;
import com.example.wordmanagefilesystem.Service.Implement.CheckImpl;
import com.example.wordmanagefilesystem.Service.Implement.CheckValidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("check")
public class CheckController {

    @Autowired
    private CheckImpl checkImpl;

    //根据前端抽查次数获得随即单词
    @GetMapping()
    public Result getRandomWordByPageSize(@RequestParam Integer pageSize){
        if (CheckValidUtil.isNull(pageSize)){
            throw new RuntimeException("根据前端抽查次数获得随即单词，为null！！");
        }
        Result r = new Result();
        Word[] words = checkImpl.getRandomWordsByPageSize(pageSize);
        return r.success(words);
    }
}
