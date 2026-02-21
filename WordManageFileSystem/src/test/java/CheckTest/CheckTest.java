package CheckTest;

import com.example.wordmanagefilesystem.Mapper.CheckMapper;
import com.example.wordmanagefilesystem.Pojo.Word;
import com.example.wordmanagefilesystem.Service.Implement.WordImpl;
import com.example.wordmanagefilesystem.WordManageFileSystemApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest(classes = WordManageFileSystemApplication.class)
public class CheckTest {

    @Autowired
    private CheckMapper checkMapper;

    private static final Random r = new Random();

    @Test
    public void getRandomWordMainTest() {
        Word[] randomWordsByPageSize = getRandomWordsByPageSize(25);
        for (Word word : randomWordsByPageSize) {
            System.out.println(word);
        }
    }

    public Word[] getRandomWordsByPageSize(Integer pageSize){
        Integer page = getRandomFinallyPage(pageSize);
        Integer pageReal = (page - 1) * pageSize;
        List<Word> words = getRandomPageWord(pageReal, pageSize);
        Word[] wordsArr = wordListToArr(words);
        return fisherYatesAlgorithm(wordsArr);
    }

    public Word[] fisherYatesAlgorithm(Word[] wordsArr){
        for (int i = wordsArr.length - 1; i > 0; i--) {
            int random = r.nextInt(i);
            Word t = wordsArr[random];
            wordsArr[random] = wordsArr[i];
            wordsArr[i] = t;
        }
        return wordsArr;
    }

    public Word[] wordListToArr(List<Word> list){
        Word[] wordArr = new Word[list.size()];
        int index = 0;
        for (Word word : list){
            wordArr[index] = word;
            index++;
        }
        return wordArr;
    }

    public List<Word> getRandomPageWord(Integer page , Integer pageSize){
        return checkMapper.getLimitWord(page, pageSize);
    }

    public Integer getRandomFinallyPage(Integer pageSize) {
        Integer wordCount = checkMapper.getWordCount();
        Integer totalPage = wordCount / pageSize;
        if (wordCount % pageSize != 0) totalPage++;
        return r.nextInt(totalPage) + 1;
    }
}
