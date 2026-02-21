package com.example.wordmanagefilesystem.Pojo;

import com.example.wordmanagefilesystem.Pojo.Msg.MsgBody;
import com.example.wordmanagefilesystem.Pojo.Report.AccuracyGroupByBody;
import com.example.wordmanagefilesystem.Pojo.Report.DataOverLook;
import com.example.wordmanagefilesystem.Pojo.Report.OverLook;
import com.example.wordmanagefilesystem.Pojo.Setting.SettingBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private Integer hasSearchTotal;
    private Integer totalPage;
    private boolean isRight;
    private Integer currentPage;
    private T data;

    //全局异常
    public static Result exception(String message) {
        Result result = new Result();
        result.setCode(500);
        result.setMsg(null);
        result.setData(message);
        return result;
    }

    public static Result success(List<Word> words) {
        Result result = new Result();
        result.setCode(500);
        result.setMsg(null);
        result.setData(words);
        return result;
    }

    //setting数据
    public Result settingSuccess(SettingBody data){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("获得用户setting数据成功！！");
        result.setData(data);
        return result;
    }

    public Result settingError(SettingBody data){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("获得用户setting数据失败！！");
        result.setData(data);
        return result;
    }

    public Result settingSuccess(boolean success){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("更新/新增 用户setting数据成功！！");
        result.setData(success);
        return result;
    }

    //文件上传成功
    public Result uploadSuccess(String fileUrl){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("文件上传成功");
        result.setData(fileUrl);
        return result;
    }

    public Result uploadSuccess(Integer resultInteger){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("文件上传成功");
        result.setData(resultInteger);
        return result;
    }


    //修改失败
    public Result updateError(){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("数据有问题/修改失败！！");
        return result;
    }

    public Result<List<AccuracyGroupByBody>> accuracyEChartSuccess(List<AccuracyGroupByBody> data){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("准确率EChartJosn数据返回成功");
        result.setData(data);
        return result;
    }

    /*
    * 数据总览Result*/
    public Result<List<DataOverLook>> dataOverLookSuccess(List<DataOverLook> data){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("数据总览返回成功");
        result.setData(data);
        return result;
    }

    public Result<OverLook> overLookSuccess(OverLook overLook){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("数据总览返回成功");
        result.setData(overLook);
        return result;
    }


    public Result<Register> registerRepeat(Register register){
        Result result = new Result();
        result.setCode(400);
        result.setHasSearchTotal(null);
        result.setMsg("用户名重复！");
        result.setData(register);
        return result;
    }

    public Result<Register> registerSuccess(Register register){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("用户注册成功！");
        result.setData(register);
        return result;
    }

    public Result<Register> registerError(){
        Result result = new Result();
        result.setCode(400);
        result.setHasSearchTotal(null);
        result.setMsg("用户注册失败！");
        result.setData(null);
        return result;
    }

    public Result<T> success(List<Word> data , Integer hasSearchTotal , Integer totalPage , Integer currentPage){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(hasSearchTotal);
        result.setMsg("已查询或已成功！");
        result.setTotalPage(totalPage);
        result.setCurrentPage(currentPage);
        result.setData(data);
        return result;
    }


    public Result<T> success(Word[] words){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("已查询或已成功！");
        result.setData(words);
        return result;
    }


    public Result<T> success(String string){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("已查询或已成功！");
        result.setData(string);
        return result;
    }

    public Result<T> success(Integer R){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("已查询或已成功！");
        result.setData(R);
        return result;
    }

    public Result<T> success(MsgBody msgBody){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("已查询或已成功！");
        result.setData(msgBody);
        return result;
    }

    public Result<WordAccuracyCombineVocabulary> updateAccuracySuccess(
            WordAccuracyCombineVocabulary wordAccuracyCombineVocabulary , boolean isRight){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("已查询或已成功！");
        result.setRight(isRight);
        result.setData(wordAccuracyCombineVocabulary);
        return result;
    }

    public Result<T> success(Word word){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("已查询或已成功！");
        result.setData(word);
        return result;
    }

    public Result<T> success(Word word , Integer hasSearchTotal){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(hasSearchTotal);
        result.setMsg("已查询或已成功！");
        result.setData(word);
        return result;
    }

    public static Result<Integer> successIngeter(Integer integer){
        Result<Integer> result = new Result<>();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("已查询或已成功！");
        result.setData(integer);
        return result;
    }

    public Result<Integer> successInterge(Integer integer){
        Result<Integer> result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("已查询或已成功！");
        result.setData(integer);
        return result;
    }

    public Result<Double> successDouble(double doubleDate){
        Result<Double> result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("已查询或已成功！");
        result.setData(doubleDate);
        return result;
    }

    public Result<T> differentDataSuccess(DifferentData differentData){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("已查询或已成功！");
        result.setData(differentData);
        return result;
    }

    public Result<T> queryMeaningSuccess(String meaning){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("已查询或已成功！");
        result.setData(meaning);
        return result;
    }

    public Result<T> updateSuccess(WordAccuracyCombineVocabulary wordAccuracyCombineVocabulary ){
        Result result = new Result();
        result.setCode(200);
        result.setHasSearchTotal(null);
        result.setMsg("已查询或已成功！");
        result.setData(wordAccuracyCombineVocabulary);
        return result;
    }

    public Result<T> defeat(){
        Result result = new Result();
        result.setCode(500);
        result.setHasSearchTotal(null);
        result.setMsg("失败了！");
        result.setData(null);
        return result;
    }

    public Result loginSuccess(LoginMsg loginMsg ){
        Result result = new Result();
        result.setCode(200);
        result.setMsg("登录成功");
        result.setData(loginMsg);
        return result;
    }

    public Result loginDefeat(){
        Result result = new Result();
        result.setCode(200);
        result.setMsg("用户名或密码不正确");
        result.setData(null);
        return result;
    }

    public Result() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getHasSearchTotal() {
        return hasSearchTotal;
    }

    public void setHasSearchTotal(Integer hasSearchTotal) {
        this.hasSearchTotal = hasSearchTotal;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result(Integer code, String msg, Integer hasSearchTotal, boolean isRight, T data) {
        this.code = code;
        this.msg = msg;
        this.hasSearchTotal = hasSearchTotal;
        this.isRight = isRight;
        this.data = data;
    }
}
