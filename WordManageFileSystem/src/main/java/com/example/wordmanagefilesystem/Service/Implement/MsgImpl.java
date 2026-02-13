package com.example.wordmanagefilesystem.Service.Implement;

import com.example.wordmanagefilesystem.Tool.FileUploadOSS;
import com.example.wordmanagefilesystem.Tool.JWTTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wordmanagefilesystem.Mapper.MsgMapper;
import com.example.wordmanagefilesystem.Pojo.Msg.MsgBody;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class MsgImpl {

    @Autowired
    private MsgMapper msgMapper;

    @Autowired
    private FileUploadOSS fileUploadOSS;
    
    //添加用户基础信息
    public Integer addUserMsg(Integer userId , MsgBody msgBody){
        if(CheckValidUtil.isNull(userId) || CheckValidUtil.isNull(msgBody)){
            log.warn("添加用户基础信息，用户id或传入数据出问题！！");
            return 0;
        }
        Integer insertResult = msgMapper.insertUserMsg(userId, msgBody.getName()
                , msgBody.getPhoneNumber(), msgBody.getBirthDay() , null
                , msgBody.getGrade() , msgBody.getGender() , msgBody.getCity());
        if (insertResult > 0){
            log.info("添加用户基础信息，成功！！");
            return 1;
        }
        log.error("添加用户基础信息，失败！！");
        return 0;
    }

    //更新用户基础信息
    @Transactional
    public Integer updateOrInsertByExists(Integer userId , MsgBody msgBody){
        if(CheckValidUtil.isNull(userId)){
            log.warn("更新用户基础信息，用户id出问题！！");
            return 0;
        }
        if (isUserMsgExists(userId)){ //存过
            Integer insertResult = msgMapper.updateUserMsg(msgBody.getName()
                    , msgBody.getPhoneNumber(), msgBody.getBirthDay()
                    , msgBody.getGrade(), msgBody.getGender()
                    , msgBody.getCity(), userId);
            if (insertResult > 0){
                log.info("更新用户基础信息，成功！！");
                return 1;
            }
            log.error("更新用户基础信息，失败！！");
        }else { //没存过
            if (msgBody == null){ //信息等于null时
                Integer integer = msgMapper.msgNullInsert(userId); //全null插入
                if (integer > 0){
                    log.info("更新用户基础信息，传空值成功！！");
                    return 1;
                }
                log.error("更新用户基础信息，传空值失败！！");
                return 0;
            }
            //没存过，但是有信息（）信息不等于null
            log.info("用户{}还没有存过msg信息，正在新增该用户信息" , userId);
            Integer integer = addUserMsg(userId, msgBody);
            if (integer == 1){ //新增成功
                return 1;
            }
            return 0;
        }
        return 0;
    }

    //头像上传
    @Transactional
    public Integer msgImgUpload(Integer userId , MultipartFile img){
        if (CheckValidUtil.isNull(userId) || CheckValidUtil.isNull(img)){
            log.warn("头像上传 , 用户名或头像文件出问题！！");
            return 0;
        }
        if (isUserMsgExists(userId)) { //存在数据
            String imgURL = fileUploadOSS.uploadFile(img);
            log.info("头像上传，头像URL为{}" , imgURL);
            Integer integer = msgMapper.imgAloneUpdate(imgURL, userId);
            if (integer > 0){
                log.info("头像上传，数据库上传成功！！");

                return 1;
            }
            log.error("头像上传，数据库上传失败！！");
            return 0;
        }else { //不存在，先创建，再单独上传
            Integer integer = msgMapper.msgNullInsert(userId); //全null插入
            if (integer > 0){ //创建好后，单独上传
                log.info("头像上传，创建数据，创建成功！！");
                String imgURL = fileUploadOSS.uploadFile(img);
                log.info("头像上传，头像URL为{}" , imgURL);
                Integer integer2 = msgMapper.imgAloneUpdate(imgURL, userId);
                if (integer2 > 0){
                    log.info("头像上传，数据库上传成功！！");
                    return 1;
                }
                log.error("头像上传，数据库上传失败！！");
                return 0;
            }
            log.error("头像上传，创建失败！！");
            return 0;
        }
    }

    //查询用户基础信息
    public MsgBody getUserMsg(Integer userId){
        if (CheckValidUtil.isNull(userId)){
            log.warn("查询用户基础信息，用户id或传入数据出问题！！");
            return null;
        }
        MsgBody userMsg = msgMapper.getUserMsg(userId);
        if (CheckValidUtil.isNull(userMsg)){
            log.warn("查询用户基础信息，查询到的数据出现问题");
            return null;
        }
        return userMsg;
    }

    //判断用户msg是否insert过
    public boolean isUserMsgExists(Integer userId){
        return msgMapper.isUserMsgExists(userId) ? true : false;
    }
}
