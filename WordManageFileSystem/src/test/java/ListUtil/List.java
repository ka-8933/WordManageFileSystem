package ListUtil;

import com.example.wordmanagefilesystem.Pojo.Word;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class List {

    public java.util.List<java.util.List<Word>> split(java.util.List<Word> words, Integer packageSize){
        java.util.List<java.util.List<Word>> splitWords = new ArrayList<>();
        int splitIndex = 1;
        for (Word word : words){
            java.util.List<Word> getSplitList = new ArrayList<>();
            if (splitIndex == packageSize){
                splitWords.add(getSplitList);
                getSplitList.clear();
                splitIndex = 1;
            }
            getSplitList.add(word);
            splitIndex ++;
        }
        return splitWords;
    }
}
