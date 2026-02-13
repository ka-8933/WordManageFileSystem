package com.example.wordmanagefilesystem;

import com.example.wordmanagefilesystem.Mapper.WordMapper;
import com.example.wordmanagefilesystem.Pojo.Word;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class WordManageFileSystemApplicationTests {

    @Autowired
    private WordMapper wordMapper; // 能注入成功，说明接口被扫描到了

    @Test
    public void testGetAllWord() {
        List<Word> wordList = wordMapper.getAllWord();
        System.out.println("查询结果数量：" + wordList.size()); // 看数量
        for (Word word : wordList) {
            System.out.println(word); // 看每个对象的字段值
        }
    }

}
