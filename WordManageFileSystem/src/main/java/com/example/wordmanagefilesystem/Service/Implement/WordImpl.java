package com.example.wordmanagefilesystem.Service.Implement;

import StringHandler.XKStringHandler;
import StringHandler.XkCommon;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
import com.example.wordmanagefilesystem.Mapper.WordMapper;
import com.example.wordmanagefilesystem.Pojo.*;
import com.example.wordmanagefilesystem.Pojo.Check.CheckDataOriginal;
import com.example.wordmanagefilesystem.Pojo.Setting.SettingBody;
import com.example.wordmanagefilesystem.Service.WordService;
import com.example.wordmanagefilesystem.Tool.ListUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Executor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Service
public class WordImpl implements WordService {

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private SettingImpl settingImpl;

    @Autowired
    private ListUtil listUtil;

    /*
     * 缓存根据单词查询释义的map集合key为word*/
    private static Map<String, Word> wordCacheKeyWord = new ConcurrentHashMap<>();

    /*
     * 缓存根据单词查询释义的map集合key为id*/
    private static Map<Integer, Word> wordCacheKeyId = new ConcurrentHashMap<>();

    /*
     * 单词抽查有序缓存*/
    private static Map<Integer, Word> checkCacheKeyAuto = new ConcurrentHashMap<>();

    //调用通用工具类
    private static CommonTool commonTool = new CommonTool();

    //得到电脑线程数
    private static final int myThreadNumber = Runtime.getRuntime().availableProcessors();

    //创建线程池
    private static final ExecutorService executorService = Executors.newFixedThreadPool(myThreadNumber);

    /*缓存所有单词key为id*/
    @PostConstruct
    public void implementWordCacheByWordAndId() {
        List<Word> allWord = wordMapper.getAllWord();
        if (CheckValidUtil.isValid(allWord)) {
            log.error("缓存数据来源出现问题！！");
            return;
        }
        for (Word word : allWord) {
            if (!CheckValidUtil.isWordValid(word)) {
                continue;
            }
            wordCacheKeyWord.put(word.getWord(), word);
            wordCacheKeyId.put(word.getId(), word);
        }
        int publicVocabularyLengthById = wordCacheKeyId.size();
        int publicVocabularyLengthByWord = wordCacheKeyWord.size();
        log.info("通过id为key的公共单词缓存成功，数量为：{}", publicVocabularyLengthById);
        log.info("通过word为key的公共单词缓存成功，数量为：{}", publicVocabularyLengthByWord);
    }

    /*
     *更新缓存方法，防止数据不统一，放入线程池，避免更新与出现冲突*/
    public void refreshWordCacheByWordAndId() {
        Runnable refreshWordThread = () -> {
            try {
                splitWordAndImplRefresh();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        executorService.execute(refreshWordThread);
    }

    //将单词分割并且执行更新
    private void splitWordAndImplRefresh() throws InterruptedException {
        wordCacheKeyWord.clear();
        wordCacheKeyId.clear();
        List<Word> allWord = wordMapper.getAllWord();
        List<List<Word>> split = listUtil.split(allWord, 100);
        for (List<Word> wordList : split) { //取出每一组单词
            for (Word word : wordList) { //遍历每一组每一个单词
                if (!CheckValidUtil.isWordValid(word)) {
                    continue;
                }
                wordCacheKeyId.put(word.getId(), word);
                wordCacheKeyWord.put(word.getWord(), word);
            }
            int publicVocabularyLengthById = wordCacheKeyId.size();
            int publicVocabularyLengthByWord = wordCacheKeyWord.size();
            log.info("通过id为key的批次公共单词缓存更新成功，当前缓存数量为：{}", publicVocabularyLengthById);
            log.info("通过word为key的批次公共单词缓存更新成功，当前缓存数量为：{}", publicVocabularyLengthByWord);
            Thread.sleep(200);
        }
    }

    /*
     * 单词抽查有序缓存*/
    @PostConstruct
    public void implementCheckWordCache() {
        List<Word> allWord = wordMapper.getAllWord();
        if (CheckValidUtil.isValid(allWord)) {
            log.warn("单词抽查有序缓存 单词获取失败！");
            return;
        }
        int autoId = 1;
        for (Word word : allWord) {
            if (!CheckValidUtil.isWordValid(word)) {
                log.warn("单词抽查有序缓存 循环到的单词不合理，已跳过");
                continue;
            }
            checkCacheKeyAuto.put(autoId, word);
            autoId++;
        }
        log.info("单词抽查有序缓存 缓存成功");
    }

    //单词抽查有序缓存 更新函数
    public void refreshCheckWordCache() {
        checkCacheKeyAuto.clear();
        List<Word> allWord = wordMapper.getAllWord();
        if (CheckValidUtil.isValid(allWord)) {
            log.warn("单词抽查有序缓存 单词获取失败！");
            return;
        }
        int autoId = 1;
        for (Word word : allWord) {
            if (!CheckValidUtil.isWordValid(word)) {
                log.warn("单词抽查有序缓存 循环到的单词不合理，已跳过");
                continue;
            }
            checkCacheKeyAuto.put(autoId, word);
            autoId++;
        }
        log.info("单词抽查有序缓存 缓存成功");
    }

    /*CRUD*/

    //分页查询
    @Override
    public List<Word> getAllWordPage(Integer page, Integer pageSize, Integer userId) {
        SettingBody settingData = settingImpl.getUserSettingData(userId);
        if (CheckValidUtil.isNull(settingData)) {
            log.warn("分页查询settingData出现问题");
            Integer start = (page - 1) * pageSize;
            List<Word> words = wordMapper.getUserAllWordPage(start, pageSize, userId);
            return words;
        }
        if (isUserSettingIsSavePageBoot(userId)) { //如果此时用户setting的是否保存当前页数是true
            Integer start = (settingData.getSavePage() - 1) * pageSize;
            log.info("分页查询：用户{} ，开始数量{}", userId, start);
            List<Word> words = wordMapper.getUserAllWordPage(start, pageSize, userId);
            return words;
        }
        Integer start = (page - 1) * pageSize;
        List<Word> words = wordMapper.getUserAllWordPage(start, pageSize, userId);
        return words;
    }

    //获得下一页单词
    public List<Word> getNextPageWords(Integer page, Integer pageSize, Integer userId) {
        SettingBody settingData = settingImpl.getUserSettingData(userId); // 得到用户setting数据
        if (CheckValidUtil.isNull(settingData)) {
            log.warn("分页查询settingData出现问题");
            Integer start = (page - 1) * pageSize;
            List<Word> words = wordMapper.getUserAllWordPage(start, pageSize, userId);
            return words;
        }
        if (isUserSettingIsSavePageBoot(userId)) { // setting保存页数为true
            Integer start = settingData.getSavePage() * pageSize;
            log.info("分页查询：用户{} ，开始数量{}", userId, start);
            List<Word> words = wordMapper.getUserAllWordPage(start, pageSize, userId);
            return words;
        }
        Integer start = (page - 1) * pageSize;
        List<Word> words = wordMapper.getUserAllWordPage(start, pageSize, userId);
        return words;
    }

    //获得下一页单词
    public List<Word> getOnPageWords(Integer page, Integer pageSize, Integer userId) {
        SettingBody settingData = settingImpl.getUserSettingData(userId); // 得到用户setting数据
        if (CheckValidUtil.isNull(settingData)) {
            log.warn("分页查询settingData出现问题");
            Integer start = (page - 1) * pageSize;
            List<Word> words = wordMapper.getUserAllWordPage(start, pageSize, userId);
            return words;
        }
        if (isUserSettingIsSavePageBoot(userId)) { // setting保存页数为true
            Integer start = (settingData.getSavePage() - 2) * pageSize;
            log.info("分页查询：用户{} ，开始数量{}", userId, start);
            List<Word> words = wordMapper.getUserAllWordPage(start, pageSize, userId);
            return words;
        }
        Integer start = (page - 1) * pageSize;
        List<Word> words = wordMapper.getUserAllWordPage(start, pageSize, userId);
        return words;
    }

    //判断用户的setting保存当前页面按钮是否开启
    public boolean isUserSettingIsSavePageBoot(Integer userId) {
        SettingBody settingData = settingImpl.getUserSettingData(userId);
        if (CheckValidUtil.isNull(settingData)) {
            log.warn("判断用户的setting保存当前页面按钮是否开启,出现问题!");
            return false;
        }
        if (settingData.getIsSavePage() == 1) {
            return true;
        }
        return false;
    }

    //得到用户查询单词的用页数（根据每页数量计算）
    public Integer getQueryOfficialsWordTotalPage(Integer pageSize) {
        Integer officialsQuatity = getOfficialsQuatity();
        Integer totalPage = officialsQuatity / pageSize;
        if ((officialsQuatity % pageSize) > 0) {
            totalPage = totalPage + 1;
        }
        return totalPage;
    }

    //修改公共单词
    @Override
    public Integer updatePublicVocabulary(WordWithUserId wordWithUserId) {
        if (CheckValidUtil.isNull(wordWithUserId)) {
            log.error("修改公共单词时word 或 wordId 可能不合理！");
        }
        if (wordWithUserId.getUserId() != 1) {
            log.error("您不是管理员，不可处理公共单词");
            throw new RuntimeException("您不是管理员，不可处理公共单词！！");
        }
        try {
            Integer result = wordMapper.updatePublicVocabulary(wordWithUserId.getWord(), wordWithUserId.getMeaning(),
                    wordWithUserId.getPartOfSpeech(), wordWithUserId.getBelongGrade(),
                    wordWithUserId.getSimilarWord(), wordWithUserId.getPhrase(), wordWithUserId.getId());
            if (result == 0) {
                throw new IllegalStateException("更新错误：没有找到{} 单词" + wordWithUserId.getWord());
            }
            //更新缓存
            refreshWordCacheByWordAndId();
            refreshCheckWordCache();
            return result;
        } catch (Exception e) {
            log.error("公共单词修改发生错误{}", e.getMessage());
            throw new ResourceAccessException("公共单词修改发生错误:" + e.getMessage());
        }
    }

    //添加公共单词
    @Override
    public Integer addPublicWord(Word word) {
        Integer resultRow = wordMapper.addPublicWord(word.getWord(), word.getMeaning()
                , word.getPartOfSpeech(), word.getBelongGrade(), word.getSimilarWord(), word.getPhrase());
        if (CheckValidUtil.isNull(resultRow)) {
            return null;
        }
        refreshWordCacheByWordAndId();
        refreshCheckWordCache();
        return resultRow;
    }

    //删除公共单词 根据单词id
    @Override
    public String deletePublicWordById(Integer id) {
        Integer result = wordMapper.deletePublicWordById(id);

        if (CheckValidUtil.isNull(result)) {
            log.error("删除公共单词的结果为null{}", result);
            return null;
        }
        if (result > 0) {
            //通过缓存获取单词word
            Word word = wordCacheKeyId.get(id);
            if (CheckValidUtil.isWordValid(word)) {
                String word1 = word.getWord();
                //及时更新缓存
                refreshWordCacheByWordAndId();
                refreshCheckWordCache();
                return "公共单词 " + word1 + " 删除成功！";
            }
        }
        return null;
    }

    /*
     * 首页四组单词数据处理*/

    //获取用户所有新增单词数量
    @Override
    public Integer getAllAddWordQuatity(Integer userId) {
        if (userId == 0 || userId < 0) return null;
        try {
            return wordMapper.getAllAddWordQuatity(userId);
        } catch (Exception e) {
            log.error("新增数据出现错误：" + e.getMessage());
        }
        return null;
    }

    //得到用户所有单词的平均准确率
    @Override
    public Double getUserAverageAccuracy(Integer userId) {
        Double userAverageAccuracy = wordMapper.getUserAverageAccuracy(userId);
        return userAverageAccuracy == null ?
                0.0 : Double.parseDouble(String.format("%.2f", userAverageAccuracy));
    }

    @Override
    public String queryMeaningByWord(String word) {
        return null;
    }

    //获取用户公共单词小于准确率60的总数
    @Override
    public Integer getLessScoreQuatity(Integer userId) {
        if (userId == 0 || userId < 0) return null;
        try {
            return wordMapper.getLessScoreQuatity(userId);
        } catch (Exception e) {
            log.error("易错数据出现错误" + e.getMessage());
        }
        return null;
    }

    //官方收录单词总数
    @Override
    public Integer getOfficialsQuatity() {
        Integer officialsQuatity = wordMapper.getOfficialsQuatity();
        if (CheckValidUtil.isNull(officialsQuatity)) {
            return 0;
        }
        return officialsQuatity;
    }

    /*
     * 每一天的凌晨2点自动更新每个用户 Yesterday 数据表里的数据*/

    @Scheduled(cron = "0 0 0 * * ?")  //每一天的2点执行（标明@Scheduled的方法不能有参数）
    public void refreshYesterdayDataByTimer() throws InterruptedException {
        List<Integer> allUserId = wordMapper.getAllUserId();
        if (CheckValidUtil.isValid(allUserId)) {
            return;
        }
        int successCount = 0;
        int defeatCount = 0;
        for (Integer userId : allUserId) {
            try {
                updateYesterdayDataAfterOneDayTime(userId);
                //通过用户id得到用户名
                String username = wordMapper.getUsernameByUserId(userId);
                log.info("用户{}的数据更新成功", username);
                successCount++;
            } catch (Exception e) {
                log.error("Yesterday定时任务用户{} 更改失败", userId);
                defeatCount++;
                successCount++;
            }
        }
        log.info("一共更新{}条数据，{}条更新失败，更新完毕！", successCount, defeatCount);
    }

    /*
     * 更新用户yesterday过去数据为实时数据的方法*/
    public void updateYesterdayDataAfterOneDayTime(Integer userId) {
        try {
            //先得到用户所有数据
            double userAverageAccuracy = getUserAverageAccuracy(userId);
            Integer allAddWordQuatity = getAllAddWordQuatity(userId);
            Integer officialsQuatity = getOfficialsQuatity();
            Integer lessScoreQuatity = getLessScoreQuatity(userId);
            LocalDateTime now = LocalDateTime.now();
            //更新数据库
            wordMapper.updateYesterdayDateToCurrentData(userAverageAccuracy,
                    allAddWordQuatity, officialsQuatity, lessScoreQuatity, now, userId);
        } catch (Exception e) {
            log.error("Yesterday数据更新失败提示：" + e.getMessage());
            //直接抛出异常
            String username = wordMapper.getUsernameByUserId(userId);
            throw new RuntimeException("昨天用户" + username + "，更新失败");
        }
    }

    /*
     * 实现用户Yesterday表与实时用户数据对比，得出 单词准确率 新增单词数
     * 收录总单词数量 易错单词数量 的上升下降幅度（%）*/
    @Override
    public DifferentData yesterdayAndCurrentDataDifferent(Integer userId) {
        YesterdayData yesterdayData = wordMapper.getYesterdayDataByUserId(userId); //这里已经有了数据 4 项
        if (CheckValidUtil.isNull(yesterdayData)) {
            return null;
        }
        //获得相应的原始数据
        //准确率差距数据
        String accuracyDifferent = getAccuracyDifferent(yesterdayData, userId);
        //新增单词差距数据
        String newWordDifferent = getNewWordDifferent(yesterdayData, userId);
        //官方单词总数实时数据
        String officialsDifferent = getOfficialsDifferent(yesterdayData, userId);
        //易错单词差距数据
        String lessDifferent = getLessDifferent(yesterdayData, userId);
        //这里创建DifferentData来封装所有实时差距数据
        DifferentData differentData = new DifferentData(accuracyDifferent, newWordDifferent,
                officialsDifferent, lessDifferent);
        log.info("用户{}的实时数据为：{}", userId, differentData);
        return differentData;
    }

    /*获取准确率差异数据*/
    private String getAccuracyDifferent(YesterdayData yesterdayData, Integer userId) {
        double userAverageAccuracy = getUserAverageAccuracy(userId);
        if (CheckValidUtil.isNull(userAverageAccuracy)) {
            return "0%";
        }
        String accuracyDifferent = accuracyDifferentHandle(yesterdayData, userAverageAccuracy);
        return accuracyDifferent;
    }

    /*获取新增单词差异数据*/
    private String getNewWordDifferent(YesterdayData yesterdayData, Integer userId) {
        Integer allAddWordQuatity = getAllAddWordQuatity(userId);
        if (CheckValidUtil.isNull(allAddWordQuatity)) {
            return "0%";
        }
        Integer yesterdayNewWord = yesterdayData.getYesterdayNewWord();
        String newWordDifferent = differentCommonhandleDataIsInteger(yesterdayNewWord, allAddWordQuatity);
        return newWordDifferent;
    }

    /*获取官方单词差异数据*/
    private String getOfficialsDifferent(YesterdayData yesterdayData, Integer userId) {
        Integer officialsQuatity = getOfficialsQuatity();
        if (CheckValidUtil.isNull(officialsQuatity)) {
            return "0%";
        }
        Integer yesterdayOfficialsTotal = yesterdayData.getYesterdayOfficialsTotal();
        String officialsDifferent = differentCommonhandleDataIsInteger(yesterdayOfficialsTotal, officialsQuatity);
        return officialsDifferent;
    }

    /*获取易错单词差异数据*/
    private String getLessDifferent(YesterdayData yesterdayData, Integer userId) {
        Integer lessScoreQuatity = getLessScoreQuatity(userId);
        if (CheckValidUtil.isNull(lessScoreQuatity)) {
            return "0%";
        }
        Integer yesterdayLessWord = yesterdayData.getYesterdayLessWord();
        String lessDifferent = differentCommonhandleDataIsInteger(yesterdayLessWord, lessScoreQuatity);
        return lessDifferent;
    }

    /*
     * 通过用户yesterday的数据和实时数据，来处理差距数值
     * 准确率差异数值处理器*/
    private String accuracyDifferentHandle(YesterdayData yesterdayData, double currentAccuracy) {
        double yesterdayAccuracy = yesterdayData.getYesterdayAccuracy();
        if (yesterdayAccuracy == 0) {
            return "0%";
        }
        //处理两个数据
        log.info("yesterdayAccuracy 的数据为{}", yesterdayAccuracy);
        log.info("currentAccuracy 的数据为{}", currentAccuracy);
        return yesterdayAccuracy > currentAccuracy ?
                String.valueOf(commonTool.doubleToBigDecimalNoDecimal((double) currentAccuracy - yesterdayAccuracy)) + "%" :
                String.valueOf(commonTool.doubleToBigDecimalNoDecimal((double) yesterdayAccuracy - currentAccuracy)) + "%";
    }

    /*
     * 处理都为整数的差距数值（类似于common），具体的yesterdayData 和 currentData 数据由 yesterdayAndCurrentDatadifferent
     * 直接获得，这里返回String是因为最后要处理为含百分号和负号的数据
     * 整数差异数据处理器*/
    private String differentCommonhandleDataIsInteger(Integer yesterdayData, Integer currentData) {
        if (currentData == 0 || yesterdayData == 0) {
            return "0%";
        }
        log.info("yesterdayData的数据为{}", yesterdayData);
        log.info("currentData的数据为{}", currentData);
        //直接处理两数据
        return yesterdayData > currentData ? //假如是 10 ：5
                String.valueOf(commonTool.doubleToBigDecimalNoDecimal(-(1 - (double) currentData / yesterdayData))) + "%" :
                String.valueOf(commonTool.doubleToBigDecimalNoDecimal((1 - (double) yesterdayData / currentData))) + "%";
    }

    /*
     * 单词查询处理：
     * */
    @Override
    public List<Word> queryWordByCondition(QueryWordBody queryWordBody , Integer userId) {
        Integer start = (queryWordBody.getPage() - 1) * queryWordBody.getSize();
        List<Word> words = wordMapper.queryWordByCondition(queryWordBody.getWord(), queryWordBody.getMeaning()
                , queryWordBody.getWordClass(), queryWordBody.getSelectDegrad(), userId, start,
                queryWordBody.getSize());
        log.info("根据英文查询单词为：{}" , words);
        if (words == null || words.isEmpty()) {
            throw new RuntimeException("根据英文查询单词为null！！");
        }
        return words;
    }

    /*查询单词是，如果单词释义不为空（有释义），那么就直接拆从缓存查询*/
    @Override
    public List<Word> queryWordByCache(String meaning) {
        XKStringHandler xkStringHandler = new XkCommon();
        List<Word> words = new ArrayList<>();   //由于前端 data 数据要求以集合（List<Word>）的形式响应
        for (int i = 1; i < wordCacheKeyId.size(); i++) {
            Word word = wordCacheKeyId.get(i);
            //重点：如果遍历缓存时遍历到null时，就会出现空指针异常
            if (word == null) {
                continue;
            }
            String[] meaningArr = xkStringHandler.separateWordMeaning(word.getMeaning());
            for (int j = 0; j < meaningArr.length; j++) {
                if (meaning.equals(meaningArr[j])) {
                    log.info("查询缓存单词通过释义为{}", word);
                    words.add(word);
                }
            }
        }
        log.info("查询缓存单词通过释义为 {}", words);
        return words;
    }

    /*
     * 单词抽查*/

    /*
     * 单词验证，验证抽查的单词意思和用户输入的意思是否有一样的*/
    @Override
    public Boolean isCorrect(String word, String inputMeaning) {
        try {
            Word wordAll = wordCacheKeyWord.get(word);
            if (!CheckValidUtil.isWordValid(wordAll)) {
                log.error("通过单词{} ，获得此单词所有信息失败", word);
                return false;
            }
            XKStringHandler xkStringHandler = new XkCommon();
            String[] meaningSeparate = xkStringHandler.separateWordMeaning(wordAll.getMeaning());
            log.info("抽查的单词{} 的所有释义为{}", word, meaningSeparate);
            for (String singleMeaning : meaningSeparate) {
                if (inputMeaning.equals(singleMeaning)) {
                    return true;
                }
            }
            return false;
        } catch (ResourceAccessException e) {
            log.error("验证抽查的单词{}意思出现问题:{}", word, e.getMessage());
        }
        return false;
    }

    //根据前端传回的数据更新单词准确率
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WordAccuracyCombineVocabulary updateAccuracy(UpdateAccuracyJson updateAccuracyJson) {
        if (CheckValidUtil.isValid(updateAccuracyJson.getWord()) ||
                CheckValidUtil.isNull(updateAccuracyJson.getUserId()) || updateAccuracyJson.getUserId() < 0) {
            return null;
        }
        Boolean correct = isCorrect(updateAccuracyJson.getWord(), updateAccuracyJson.getInputMeaning());
        log.info("单词{} ，用户{} 输入释义为 {} 结果为{}", updateAccuracyJson.getWord(), updateAccuracyJson.getUserId(),
                updateAccuracyJson.getInputMeaning(), correct);
        try {
            if (!wordAccuracyIsExists(updateAccuracyJson.getWord(), updateAccuracyJson.getUserId())) { //如果是false （不存在）
                return createNewUserAccuracy(updateAccuracyJson.getWord(), updateAccuracyJson.getUserId(), correct);
            } else {
                return updateUserWordAccuracy(updateAccuracyJson.getWord(), updateAccuracyJson.getUserId(), correct);
            }
        } catch (Exception e) {
            log.info("用户抽查更新单词出错{}", e.getMessage());
        }
        return null;
    }

    /*
     * 创建新的用户单词准确率*/
    private WordAccuracyCombineVocabulary createNewUserAccuracy(String word, Integer userId, Boolean isRight) {
        Integer createAffect = wordMapper.addNewwordAccuracy(word, userId, isRight);//直接插入新的数据
        if (createAffect < 0) {
            log.error("用户{}创建单词{}准确率没有成功", userId, word);
            return null;
        }
        log.info("用户 {} 的单词{} 正确率新建成功！！", userId, word);
        return wordMapper.getWordAccuracyByWordAndUserId(word, userId);
    }

    /*
     * 更新用户单词准确率*/
    private WordAccuracyCombineVocabulary updateUserWordAccuracy(String word, Integer userId, Boolean isRight) {
        BigDecimal accuracyAfterhandler = commonTool.bigdecimalAccuracy(wordMapper.getWordLatestAccuracy(word, userId, isRight));
        Integer updateAffect = wordMapper.updateWordAcuuracyTable(isRight, accuracyAfterhandler, userId, word);
        if (updateAffect < 0) {
            log.error("用户{}更新单词{}没有成功", userId, word);
            return null;
        }
        log.info("用户 {} 的单词{} 正确率修改成功！！", userId, word);
        return wordMapper.getWordAccuracyByWordAndUserId(word, userId);
    }

    /*
     * 单词抽查*/
    //随机单词抽查
    @Override
    public Word checkPublicVocabularyByRanDom() {
        int checkCacheKeyAutoSiz = checkCacheKeyAuto.size();
        log.info("缓存单词长度为{}", checkCacheKeyAutoSiz);
        if (CheckValidUtil.isNull(checkCacheKeyAutoSiz)) {
            log.error("随机抽查时，key为id的单词缓存出现问题");
            return null;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(checkCacheKeyAutoSiz) + 1; //这样区间就是（0 ， size] 了
        Word word = checkCacheKeyAuto.get(randomIndex);
        if (!CheckValidUtil.isWordValid(word)) {
            log.error("获取随机单词出错{}", word);
            return null;
        }
        return word;
    }

    //抽查(CheckData)最后统计，插入或新建数据
    public Integer checkEndDataStatistic(CheckEndBody checkEndBody) {
        LocalDateTime now = LocalDateTime.now();
        if (isUserCheckDataExists(checkEndBody.getUserId()) == true) { //创造了
            CheckDataOriginal checkDataOriginal = getCheckDataOriginal(checkEndBody.getUserId()); //初始数据
            Integer mistake = (checkDataOriginal.getTotal() + checkEndBody.getTotal()) -
                    (checkDataOriginal.getRightNum() + checkEndBody.getRight());
            //更新最新数据
            Integer result = wordMapper.updateCheckData((checkDataOriginal.getTotal() + checkEndBody.getTotal())
                    , (checkDataOriginal.getRightNum() + checkEndBody.getRight())
                    , mistake, now, checkEndBody.getUserId());
            checkDataError(result);
            return result;
        } //没创建
        Integer newMistake = checkEndBody.getTotal() - checkEndBody.getRight();
        //插入新数据
        Integer result = wordMapper.insertCheckData(checkEndBody.getUserId(), (checkEndBody.getTotal())
                , (checkEndBody.getRight()), newMistake, now);
        checkDataError(result);
        return result;
    }

    //每日更新用户CheckData
    @Scheduled(cron = "0 0 0 * * ?")
    public void initializationUserCheckDataEveryDay() {
        List<Integer> allUserId = wordMapper.getAllUserId();
        LocalDateTime now = LocalDateTime.now();
        Runnable runnable = () -> {
            do {
                System.out.println(100);
            } while (allUserId.size() > 0);
        };
        new Thread(runnable).start();
        if (CheckValidUtil.isValid(allUserId)) {
            log.error("每日更新用户CheckData 获取所有用户id失败");
            return;
        }
        for (Integer userId : allUserId) {
            Integer result = wordMapper.updateCheckData(0, 0, 0, now, userId);
            if (result > 0) {
                log.info("每日更新用户CheckData,用户{}每日更新成功", userId);
            } else {
                log.warn("每日更新用户CheckData,用户{}每日更新出现问题", userId);
            }
        }
        log.info("每日更新用户CheckData更新完毕！！！");
    }

    //获得CheckData原来的用户数据
    private CheckDataOriginal getCheckDataOriginal(Integer userId) {
        CheckDataOriginal checkDataOriginal = wordMapper.getCheckDataOriginal(userId);
        if (CheckValidUtil.isNull(checkDataOriginal)) {
            log.warn("获得CheckData原来的用户数据，出现问题");
            return null;
        }
        return checkDataOriginal;
    }

    //CheckData插入更新判断
    public void checkDataError(Integer param) {
        if (param > 0) {
            log.info("CheckData 更新成功！！");
        } else {
            log.error("CheckData 更新失败！！");
        }
    }

    //判断用户有没有创建过CheckData数据
    public Boolean isUserCheckDataExists(Integer userId) {
        Long exists = wordMapper.isUserCheckDataExists(userId);
        if (CheckValidUtil.isNull(exists)) {
            log.warn("获得是否用户{}CheckData有数据出现问题！！", userId);
            return false;
        }
        return exists > 0 ? true : false; //创建了就返回true
    }

    /*
     * 获得随机单词通过id*/
    @Override
    public Word getRandomWordById(Integer id) {
        try {
            int publicVocabularyLength = wordCacheKeyId.size();
            if (publicVocabularyLength <= 0) {
                log.info("公共单词的数量为0，不支持随机");
                return null;
            }
            Random random = new Random(publicVocabularyLength);
            Word wordById = wordCacheKeyId.get(random.nextInt());
            if (CheckValidUtil.isNull(wordById)) {
                log.info("获得随机单词出现错误");
                return null;
            }
            log.info("");
            return wordById;
        } catch (Exception e) {
            throw new ResourceAccessException("获得随机单词出现错误!!" + e.getMessage());
        }
    }

    /*通过id获取公共单词*/
    @Override
    public Word findPublicWordById(Integer id) {
        Word word = wordCacheKeyId.get(id);
        if (!CheckValidUtil.isWordValid(word)) {
            log.info("通过id为{}获取公共单词不合理", id);
            return null;
        }
        return word;
    }

    /*
     * 添加私人单词表*/
//    public Word addPrivateWord(Word word , Integer userId){
//
//    }

    /*
     * 用户的正确率表不是一开始数据就全部有的，所以如果用户中的某个单词传来，不能直接更新这个单词的
     * 准确率 ， 需要先判断这个单词它是否之前在此用户创建过 ， 如果创建过就可以直接更新word_Acuuracy表
     * 如果没创建过，那么就要先创建，再修改*/
    private boolean wordAccuracyIsExists(String word, Integer userId) {
        boolean isExists = wordMapper.wordIsCreateWordAccuracyTable(word, userId);
        if (CheckValidUtil.isNull(isExists)) {
            log.info("用户{}抽查的单词{} 查询的准确率出现问题！", userId, word);
            return false;
        }
        return isExists;
    }

    //程序关闭时处理一系列方法
    @PreDestroy
    private void processShutDownImplFunc() throws InterruptedException {
        if (executorService != null){
            try {
                executorService.shutdown();
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                    log.info("线程池60秒未更新完毕，强制关闭！！");
                } else {
                    log.info("线程池更新完毕！！");
                }
            }catch (InterruptedException e){
                executorService.shutdownNow();
                log.error("线程池关闭出现问题{}" , e);
                log.info("线程池强制更新完毕！！");
            }
        }
        return;
    }

}
