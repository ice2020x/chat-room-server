package com.ice.chatserver.pojo;

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
@Document("groups")
public class Group {
    @Id
    private ObjectId groupId; // 群标识
    //    对应 groupId
    private String gid;
    //    群名称
    private String title = "";
    //    //群描述
    private String desc = "";
    //群图片
    private String img = "/img/zwsj5.png";
    //    群号，唯一标识
    private String code;
    // 群成员数量，避免某些情况需要多次联表查找，如搜索；所以每次加入一人，数量加一，默认为1
    private Integer userNum = 1;
    
    private String holderName; // 群主账号，在user实体中对应name字段
    private ObjectId holderUserId; //群人员的id，作为关联查询
    //    创建时间
    @CreatedDate
    private Date createDate;
    //    修改时间
    @LastModifiedDate
    private Date updateDate;
}
