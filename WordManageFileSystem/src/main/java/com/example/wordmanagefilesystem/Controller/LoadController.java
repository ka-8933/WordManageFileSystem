package com.example.wordmanagefilesystem.Controller;

import com.example.wordmanagefilesystem.Pojo.Result;
import com.example.wordmanagefilesystem.Service.Implement.CheckValidUtil;
import com.example.wordmanagefilesystem.Tool.FileUploadOSS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/load")
public class LoadController {

    @Autowired
    private FileUploadOSS FileUploadOOS;

    //文件上传
    @PostMapping()
    public Result fileUpload(@RequestParam("file") MultipartFile file){
        Result result = new Result();
        if (CheckValidUtil.isNull(file)){
            log.error("文件上传，file 不合理！！");
            return result.defeat();
        }
        String fileUrl = FileUploadOOS.uploadFile(file);
        if (CheckValidUtil.isValid(fileUrl)){
            log.error("文件上传，得到的String 不合理！！");
            return result.defeat();
        }
        return result.uploadSuccess(fileUrl);
    }
}
