package com.example.wordmanagefilesystem.Controller;

import com.example.wordmanagefilesystem.Pojo.Result;
import com.example.wordmanagefilesystem.Pojo.Word;
import com.example.wordmanagefilesystem.Service.Implement.TestImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private TestImpl testImpl;

    @PostMapping()
    public Result getTestWord(){
        List<Word> testWord1 = testImpl.getTestWord();
        Result result = new Result();
        return result.success(testWord1);
    }
}
