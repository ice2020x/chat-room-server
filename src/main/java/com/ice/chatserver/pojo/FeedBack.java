package com.ice.chatserver.pojo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

//系统的反馈信息
@Data
@Document("feedbacks")
public class FeedBack {
    @Id
    private ObjectId id; //主键
    private ObjectId userId; //反馈人id
    private String username; //反馈人账号名
    private String feedBackContent; //反馈内容
    @CreatedDate
    private Date createDate = new Date();
    
    @LastModifiedDate
    private Date updateDate;
}
