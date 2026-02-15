package com.example.wordmanagefilesystem.Service.Implement;

import com.example.wordmanagefilesystem.Mapper.TestMapper;
import com.example.wordmanagefilesystem.Pojo.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestImpl {

    @Autowired
    private TestMapper testMapper;

    public List<Word> getTestWord(){
        Word testWord = testMapper.getTestWord();
        List<Word> words = new ArrayList<Word>();
        words.add(testWord);
        return words;
    }
}
