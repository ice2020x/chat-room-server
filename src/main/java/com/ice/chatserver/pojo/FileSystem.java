package com.ice.chatserver.pojo;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author ice2020x
 * @date 2021-12-18 17:17
 * @description:
 */
@Data
@ToString
@Document(collection = "filesystem")
public class FileSystem {

    @Id
    private String fileId;
    //文件请求路径
    private String filePath;
    //文件大小
    private long fileSize;
    //文件名称
    private String fileName;
    //文件类型
    private String fileType;
}

