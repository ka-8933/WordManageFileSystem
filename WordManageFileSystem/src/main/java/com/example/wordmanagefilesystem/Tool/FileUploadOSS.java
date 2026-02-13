package com.example.wordmanagefilesystem.Tool;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
@Component
public class FileUploadOSS {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    /**
     * 上传文件的方法（你真正要用的方法！）
     * @param objectName 文件在OSS中的路径，如 "documents/test.docx"
     * @param file 要上传的文件
     * @return 文件访问URL
     */
    public String uploadFile(String objectName, MultipartFile file) {
        OSS ossClient = null;
        try {
            // 1. 创建OSS客户端
            ossClient = new OSSClientBuilder()
                    .build(endpoint , accessKeyId ,accessKeySecret);

            // 2. 获取文件输入流
            InputStream inputStream = file.getInputStream();

            // 3. 上传文件（这就是上传的核心代码！）
            ossClient.putObject(bucketName, objectName, inputStream);

            // 4. 生成文件URL
            String cleanEndpoint = endpoint.replace("https://", "").replace("http://", "");
            String fileUrl = String.format("https://%s.%s/%s", bucketName, cleanEndpoint, objectName);

            return fileUrl;

        } catch (Exception e) {
            throw new RuntimeException("文件上传失败", e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 自动生成文件名的上传方法
     */
    public String uploadFile(MultipartFile file) {
        // 自动生成文件名：时间戳 + 原始文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = "uploads/" + System.currentTimeMillis() + extension;

        String result = uploadFile(objectName, file);
        if (result == null){
            log.error("OSS文件上传uploadFile函数，文件返回结果为null");
            return null;
        }
        return result;
    }
}