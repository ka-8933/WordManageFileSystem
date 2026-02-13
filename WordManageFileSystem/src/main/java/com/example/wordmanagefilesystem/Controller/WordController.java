package com.example.wordmanagefilesystem.Controller;

import com.example.wordmanagefilesystem.Pojo.*;
import com.example.wordmanagefilesystem.Pojo.Setting.SettingBody;
import com.example.wordmanagefilesystem.Service.Implement.CheckValidUtil;
import com.example.wordmanagefilesystem.Service.Implement.SettingImpl;
import com.example.wordmanagefilesystem.Service.Implement.WordImpl;
import com.example.wordmanagefilesystem.Tool.JWTTool;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/word")
public class WordController {

    @Autowired
    private WordImpl wordImpl;

    @Autowired
    private SettingImpl settingImpl;

    private static final JWTTool jwtTool = new JWTTool();

    //用户所有单词的分页查询
    @GetMapping("/page")
    public Result getAllWordPage(@RequestParam Integer page, @RequestParam Integer pageSize
            , @RequestHeader("userToken") String token) {
        Claims claims = jwtTool.parseToken(token);
        Result result = new Result();
        if (wordImpl.isUserSettingIsSavePageBoot((Integer) claims.get("id"))){ //开启的话
            List<Word> words = wordImpl.getAllWordPage(page, pageSize, (Integer) claims.get("id"));
            int hasSearchTotal = page * pageSize;
            Integer queryOfficialsWordTotalPage = wordImpl.getQueryOfficialsWordTotalPage(pageSize);
            //得到用户的保留页码
            SettingBody currentPage = settingImpl.getUserSettingData((Integer) claims.get("id"));
            return result.success(words, hasSearchTotal , queryOfficialsWordTotalPage , currentPage.getSavePage());
        }
        List<Word> words = wordImpl.getAllWordPage(page, pageSize, (Integer) claims.get("id"));
        int hasSearchTotal = page * pageSize;
        Integer queryOfficialsWordTotalPage = wordImpl.getQueryOfficialsWordTotalPage(pageSize);
        return result.success(words, hasSearchTotal , queryOfficialsWordTotalPage , page);
    }

    //分页查询下一页
    @GetMapping("/nextPage")
    public Result getNextPageWords(@RequestParam Integer page, @RequestParam Integer pageSize
            , @RequestHeader("userToken") String token){
        Claims claims = jwtTool.parseToken(token);
        Result result = new Result();
        List<Word> words = wordImpl.getNextPageWords(page, pageSize, (Integer) claims.get("id"));
        return result.success(words , null , null , null);
    }

    //分页查询上一页
    @GetMapping("/onPage")
    public Result getOnPageWords(@RequestParam Integer page, @RequestParam Integer pageSize
            , @RequestHeader("userToken") String token){
        Claims claims = jwtTool.parseToken(token);
        Result result = new Result();
        List<Word> words = wordImpl.getOnPageWords(page, pageSize, (Integer) claims.get("id"));
        return result.success(words , null , null , null);
    }

    @GetMapping("/publicById")
    public Result getPublicWordById(@RequestParam Integer id) {
        Word publicWordById = wordImpl.findPublicWordById(id);
        log.info("通过 {} 查询到单词 {}", id, publicWordById);
        Result result = new Result();
        return result.success(publicWordById);
    }

    /*
     * 通过是否答对修改用户此单词的准确率,
     * 前端传回抽查单词的数据，后端进行数据更新*/
    @PutMapping("/updateAccuracy")
    public Result updateAccuracyByIsCorrect(@RequestBody UpdateAccuracyJson updateAccuracyJson
            ,@RequestHeader("userToken") String token){
        Integer userId = (Integer) jwtTool.parseToken(token).get("id");
        updateAccuracyJson.setUserId(userId);
        WordAccuracyCombineVocabulary wordAccuracyCombineVocabulary = wordImpl.updateAccuracy(updateAccuracyJson);
        Boolean correct = wordImpl.isCorrect(updateAccuracyJson.getWord(), updateAccuracyJson.getInputMeaning());
        Result result = new Result();
        return result.updateAccuracySuccess(wordAccuracyCombineVocabulary , correct);
    }

    /*
    * 根据单词id删除公共单词*/
    @DeleteMapping("/deletePublicWord")
    public Result deletePublicWordById(@RequestParam Integer id){
        String msg = wordImpl.deletePublicWordById(id);
        Result result = new Result();
        Result success = result.success(msg);
        return success;
    }

    /*
    * 新增公共单词*/
    @PostMapping("/publicAdd")
    public Integer addPublicWord(@RequestBody Word word){
        if (!CheckValidUtil.isWordValid(word)){
            return null;
        }
        return wordImpl.addPublicWord(word);
    }

    /*
    * 根据单词，释义，阶段，词性，分别查询单词*/
    @PostMapping("/queryWord")
    public Result queryWordByCondition(@RequestBody QueryWordBody queryWordBody){
        if (CheckValidUtil.isNull(queryWordBody)){
            return null;
        }
        Result result = new Result();
        Integer hasSearchTotal = queryWordBody.getPage() * queryWordBody.getSize();
        if (queryWordBody.getMeaning() != null){
            List<Word> words = wordImpl.queryWordByCache(queryWordBody.getMeaning());
            return result.success(words, hasSearchTotal , 1 , 1);
        }else {
            List<Word> words = wordImpl.queryWordByCondition(queryWordBody);
            return result.success(words, hasSearchTotal , 1 , 1);
        }
    }

    /*
     * 处理首页四个单词数据*/
    @GetMapping("/newWord")
    public Integer getAllAddWordQuatity(@RequestHeader("userToken") String token) {
        Claims claims = jwtTool.parseToken(token);
        Integer allAddWordQuatity = wordImpl.getAllAddWordQuatity((Integer) claims.get("id"));
        return allAddWordQuatity;
    }

    //用户的平均准确率
    @GetMapping("/accuracy")
    public double getUserAverageAccuracy(@RequestHeader("userToken") String token) {
        if (CheckValidUtil.isValid(token)){
            log.warn("用户的平均准确率 token值有问题");
            return 0;
        }
        Claims claims = jwtTool.parseToken(token);
        double userAverageAccuracy = wordImpl.getUserAverageAccuracy((Integer) claims.get("id"));
        return userAverageAccuracy;
    }

    //获取用户公共单词小于准确率60的总数
    @GetMapping("/lessScore")
    public Integer getLessScoreQuatity(@RequestHeader("userToken") String token) {
        Claims claims = jwtTool.parseToken(token);
        Integer lessScoreQuatity = wordImpl.getLessScoreQuatity((Integer) claims.get("id"));
        return lessScoreQuatity;
    }

    //得到官方收录单词总数
    @GetMapping("/wordTotal")
    public Integer getOfficialsQuatity() {
        Integer wordTotal = wordImpl.getOfficialsQuatity();
        return wordTotal;
    }


//    @PostMapping("addPrivateWord")
//    public Result addWord(@RequestBody Word word , @RequestParam Integer userId){
//        Word word1 = wordImpl.addPrivateWord(word , userId);
//
//    }

    /*
     * 得到释义通过单词*/
    @GetMapping("/wordGetMeaning")
    public Result queryMeaningByWord(@RequestParam String word) {
        String meaning = wordImpl.queryMeaningByWord(word);
        Result result = new Result();
        return result.queryMeaningSuccess(meaning);
    }

    /*
     * 数据差异化处理数据*/
    @GetMapping("/differentData")
    public Result getCurrentDifferentData(@RequestHeader("userToken") String token) {
        Claims claims = jwtTool.parseToken(token);
        DifferentData differentData = wordImpl.yesterdayAndCurrentDataDifferent((Integer) claims.get("id"));
        log.info("数据差异化实时更新{}", differentData);
        Result result = new Result();
        return result.differentDataSuccess(differentData);
    }

    /*修改公共单词*/
    @PostMapping("/update")
    public Integer updatePublicVocabulary(@RequestBody WordWithUserId wordWithUserId) {
        log.info("用户{} 更新了单词{}", wordWithUserId.getUserId(), wordWithUserId.getWord());
        Integer integer = wordImpl.updatePublicVocabulary(wordWithUserId);
        return integer;
    }

    /*获取随机单词*/
    @GetMapping("/random")
    public Result getRandomWord(){
        Word word = wordImpl.checkPublicVocabularyByRanDom();
        log.info("获得随机单词为：{}" , word);
        Result result = new Result();
        return result.success(word);
    }

    //抽查最后数据插入
    @PostMapping("/accuracyStatistic")
    public Result checkEndDataStatistic(@RequestHeader("userToken") String token ,@RequestBody CheckEndBody checkEndBody){
        Result result1 = new Result();
        if (CheckValidUtil.isValid(token)){
            log.warn("抽查最后数据插入,token为null");
            return result1.defeat();
        }
        Integer id = (Integer) jwtTool.parseToken(token).get("id");
        checkEndBody.setUserId(id);
        if (CheckValidUtil.isNull(checkEndBody)){
            log.error("抽查最后结果插入数据出错！！");
            return result1.updateError();
        }
        Integer result = wordImpl.checkEndDataStatistic(checkEndBody);
        return result1.successInterge(result);
    }

}
