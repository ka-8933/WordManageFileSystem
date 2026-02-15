package com.example.wordmanagefilesystem.Service;

import com.example.wordmanagefilesystem.Pojo.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WordService {
    List<Word> getAllWordPage(Integer page , Integer pageSize , Integer userId);
    public Integer updatePublicVocabulary(WordWithUserId wordWithUserId );
    public String deletePublicWordById(Integer id);
    public Integer addPublicWord(Word word);
    public WordAccuracyCombineVocabulary updateAccuracy(UpdateAccuracyJson updateAccuracyJson);
    Double getUserAverageAccuracy(Integer userId);
    String queryMeaningByWord(String word);
    //获得随机单词
    Word getRandomWordById(Integer id);
    //通过id获取公共单词
    public Word findPublicWordById(Integer id);
    //获取用户所有新增单词
    public Integer getAllAddWordQuatity(Integer userId);
    //获取用户公共单词小于准确率60的总数
    public Integer getLessScoreQuatity(Integer userId);
    //得到官方收录单词总数
    public Integer getOfficialsQuatity();
    //数据差异化处理
    public DifferentData yesterdayAndCurrentDataDifferent(Integer userId);
    public List<Word> queryWordByCondition(QueryWordBody queryWordBody , Integer userId);
    public List<Word> queryWordByCache(String meaning);
    //获得随机单词
    public Word checkPublicVocabularyByRanDom();
    //判断输入单词是否正确
    public Boolean isCorrect(String word, String inputMeaning);
}
