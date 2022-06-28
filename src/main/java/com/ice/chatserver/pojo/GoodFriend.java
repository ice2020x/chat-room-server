package com.ice.chatserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("goodfriends")
public class GoodFriend {
    @Id
    private ObjectId id;
    //请求者
    private ObjectId userM;
    //被请求者
    private ObjectId userY;
    //加好友的时间
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date updateDate;
}
