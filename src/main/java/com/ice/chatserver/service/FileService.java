package com.ice.chatserver.service;

import com.ice.chatserver.pojo.FileSystem;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

//文件上传接口
public interface FileService {
    //文件上传
    FileSystem upload(InputStream inputStream, String model, String originalFilename, FileSystem fileSystem);
    
    //文件删除
    void removeFile(String url);
    
    FileSystem fileSystemById(String id);
    
    //删除数据库的文件记录
    void removeFileSystem(String url);
    
    //下载文件
    public Byte[] downloadFile(@RequestParam("fileName") String fileName, HttpServletResponse resp);
}