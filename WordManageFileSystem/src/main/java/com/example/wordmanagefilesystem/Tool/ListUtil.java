package com.example.wordmanagefilesystem.Tool;

import com.example.wordmanagefilesystem.Pojo.Word;
import com.example.wordmanagefilesystem.Service.Implement.CheckValidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ListUtil {

    /*
    * 将List数据拆分成指定份数 -> List<List<T>>*/

    //将单词拆分成指定份数
    public List<List<Word>> split(List<Word> words, Integer packageSize){ //1000个 ，100/每份
        if (CheckValidUtil.isValid(words)){ //不合理执行if
            log.warn("将单词拆分成指定份数，单词可能为空或null！！");
            List<List<Word>> splitWords = new ArrayList<>();
            splitWords.add(words);
            return splitWords;
        }
        if (packageSize > words.size()){
            log.warn("将单词拆分成指定份数，每份数大于单词总数！！");
            List<List<Word>> splitWords = new ArrayList<>();
            splitWords.add(words);
            return splitWords;
        }
        List<List<Word>> splitWords = new ArrayList<>();
        List<Word> getSplitList = new ArrayList<>();
        for (Word word : words){ //拿出所有单词遍历
            getSplitList.add(word);
            if (getSplitList.size() == packageSize){ //如果手动索引等于了指定大小
                splitWords.add(new ArrayList<>(getSplitList));
                getSplitList.clear();
            }
        }
        if (!getSplitList.isEmpty()){ //最后一批单独执行储存（很可能没储存进List）
            splitWords.add(new ArrayList<>(getSplitList));
        }
        return splitWords;
    }
}
