package com.example.wordmanagefilesystem.Controller;

import com.example.wordmanagefilesystem.Pojo.Report.AccuracyGroupByBody;
import com.example.wordmanagefilesystem.Pojo.Report.CheckEchartJson;
import com.example.wordmanagefilesystem.Pojo.Report.DataOverLook;
import com.example.wordmanagefilesystem.Pojo.Report.OverLook;
import com.example.wordmanagefilesystem.Pojo.Result;
import com.example.wordmanagefilesystem.Service.Implement.CheckValidUtil;
import com.example.wordmanagefilesystem.Service.Implement.ReportImpl;
import com.example.wordmanagefilesystem.Tool.JWTTool;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("report")
public class ReportController {

    @Autowired
    private ReportImpl reportImpl;

    private static final JWTTool jwtTool = new JWTTool();

    /*
    * 数据总览四项数据*/
    @GetMapping("/dataOverLook")
    public Result dataOverLook(){
        List<DataOverLook> dataOverLooks = reportImpl.dataOverLookHander();
        if (CheckValidUtil.isValid(dataOverLooks)){
            log.error("数据总览返回失败");
            return null;
        }
        Result result = new Result<>();
        return result.dataOverLookSuccess(dataOverLooks);
    }

    //"数据总览"文本数据
    @GetMapping("/overLookText")
    public Result overLookTextData(){
        OverLook overLookTextData = reportImpl.getOverLookTextData();
        if (CheckValidUtil.isNull(overLookTextData)){
            return null;
        }
        Result result = new Result<>();
        return result.overLookSuccess(overLookTextData);
    }

    /*
     * 准确率EChart图表数据处理*/
    @GetMapping("/accuracyEchart")
    public Result accuracyEchartJson(@RequestHeader("userToken") String token){
        Claims claims = jwtTool.parseToken(token);
        List<AccuracyGroupByBody> accuracyGroupByBodies = reportImpl.accuracyGroupByJson((Integer) claims.get("id"));
        Result result = new Result();
        if (CheckValidUtil.isValid(accuracyGroupByBodies)){
            log.warn("用户{} 准确率EChart图表数据 出现问题",(Integer) claims.get("id"));
            return result.defeat();
        }
        return result.accuracyEChartSuccess(accuracyGroupByBodies);
    }

    /*
     * 每日抽查单词可视化EChart数据*/
    @GetMapping("/checkEchart")
    public Result getCheckDataEchartJson(@RequestHeader("userToken") String token){
        Claims claims = jwtTool.parseToken(token);
        List<CheckEchartJson> checkDataEchartJson = reportImpl.getCheckDataEchartJson((Integer) claims.get("id"));
        Result result = new Result();
        if (CheckValidUtil.isValid(checkDataEchartJson)){
            log.warn("用户{} 每日抽查单词可视化EChart数据 出现问题",(Integer) claims.get("id"));
            return result.defeat();
        }
        return result.accuracyEChartSuccess(checkDataEchartJson);
    }
}
