package com.ice.chatserver.service;

import com.ice.chatserver.pojo.FileSystem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * @Description: 文件上传接口
 * @Author: ice2020x
 * @Date: 2021/10/4
 */
public interface FileService {

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 文件上传
    **/
    FileSystem upload(InputStream inputStream, String model, String originalFilename, FileSystem fileSystem);


    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 文件删除
    **/
    void removeFile(String url);

    FileSystem fileSystemById(String id);

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 删除数据库的文件记录
    **/
    void removeFileSystem(String url);

    public Byte[] downloadFile(@RequestParam("fileName") String fileName, HttpServletResponse resp);

}
