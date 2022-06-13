package com.ice.chatserver.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author ice2020x
* @Date: 2021/12/18
* @Description: 群聊消息
**/
@Data
@NoArgsConstructor
@Document("groupmessages")
public class GroupMessage {
    @Id
    private ObjectId id;
    private String roomId; // => groupId
    private ObjectId senderId; // 发送者Id
    private String senderName; // 发送者登录名
    private String senderNickname;// 发送者昵称
    private String senderAvatar; // 发送者头像
    private String fileRawName; //文件的原始名字
    private String message;// 消息内容
    private String messageType;// 消息的类型：emoji/text/img/file/sys
    // 判断已经读取的用户，在发送消息时默认发送方已读取，判断多少人未读
    private List<String> isReadUser = new ArrayList<>();

//    消息的发送时间
    @CreatedDate
    private Date createDate;
    //    修改时间
    @LastModifiedDate
    private Date updateDate;
}
