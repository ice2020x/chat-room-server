package com.ice.chatserver.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.ice.chatserver.dao.FileSystemRepository;
import com.ice.chatserver.handler.OssProperties;
import com.ice.chatserver.pojo.FileSystem;
import com.ice.chatserver.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private OssProperties ossProperties;

    @Autowired
    private FileSystemRepository fileSystemRepository;

    @Override
    public FileSystem upload(InputStream inputStream, String model, String originalFilename, FileSystem fileSystem) {
        String endpoint = ossProperties.getEndpoint();
        String accessKeyId = ossProperties.getAccessKeyId();
        String accessKeySecret = ossProperties.getAccessKeySecret();
        String bucketName = ossProperties.getBucketName();


        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        if(!ossClient.doesBucketExist(bucketName)){
            ossClient.createBucket(bucketName);
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }
//        构建objectName
        String fileName =UUID.randomUUID().toString();
        String fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));
        String key = model+"/"+fileName+fileExt;

        ossClient.putObject(bucketName,key,inputStream);
        ossClient.shutdown();
        fileSystem.setFilePath("https://"+bucketName+"."+endpoint+"/"+key);
        fileSystem.setFileName(fileName+fileExt);
        fileSystem.setFileType(fileExt.substring(1));

        fileSystemRepository.save(fileSystem);

        return fileSystem;
    }

    @Override
    public void removeFile(String url) {
        String endpoint = ossProperties.getEndpoint();
        String accessKeyId = ossProperties.getAccessKeyId();
        String accessKeySecret = ossProperties.getAccessKeySecret();
        String bucketName = ossProperties.getBucketName();
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String host = "https://"+bucketName+"."+endpoint+"/";
        String objectName = url.substring(host.length());
        ossClient.deleteObject(bucketName,objectName);
        ossClient.shutdown();
    }

    @Override
    public FileSystem fileSystemById(String id) {
        Optional<FileSystem> byId = fileSystemRepository.findById(id);
        return byId.orElse(null);
    }

    @Override
    public void removeFileSystem(String url) {
        fileSystemRepository.deleteByFilePath(url);
    }

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 文件下载
    **/
    @Override
    public Byte[] downloadFile(String fileName, HttpServletResponse resp) {
        String endpoint = ossProperties.getEndpoint();
        String accessKeyId = ossProperties.getAccessKeyId();
        String accessKeySecret = ossProperties.getAccessKeySecret();
        String bucketName = ossProperties.getBucketName();

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        if(!ossClient.doesBucketExist(bucketName)){
            ossClient.createBucket(bucketName);
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }

        return new Byte[0];
    }

    static private class DateFormatterUtils {
        public static String  getFormatterDate(Date date) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            return simpleDateFormat.format(date);
        }
    }
}
