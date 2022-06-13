package com.ice.chatserver.pojo;

import com.ice.chatserver.annon.AutoIncKey;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
* @author ice2020x
* @Date: 2021/12/17
* @Description: 用户池，唯一id，每两个用户聊天有唯一一个，一个群聊有唯一一个
**/
@Data
@NoArgsConstructor
@Document("accountpool")
public class AccountPool {
    @AutoIncKey
    @Id
    private Long code = 10000000L; //用户或群聊标识（code字段在数据库中还是会以_id的名字存在）
    private Integer type; //1：用户；2：群聊
    private Integer status;  //1：已使用；0：未使用

    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date updateDate;
}
