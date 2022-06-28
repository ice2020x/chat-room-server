package com.ice.chatserver.pojo;


import com.ice.chatserver.pojo.config.BrowserSetting;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Document(collection = "users")
public class User {
    public User() {
    }
    
    @Id
    private ObjectId userId;
    //专门用一个字符串来保存用户的uid，对应 userId
    private String uid;
    @Indexed(unique = true)
    //字段唯一
    private String username;
    private String password;
    @Indexed(unique = true)
    //    字段唯一accountpool + 10000000L
    private String code;
    //头像
    private String photo;
    //    签名
    private String signature = "";
    private String nickname = "";
    private String email = "";
    private Province province = new Province();
    private City city = new City();
    private Town town = new Town();
    // 0 男 1 女 3 保密（默认值）
    private Integer sex = 3;
    //聊天框透明度
    private Double opacity = 0.75D;
    private Integer blur = 10; //模糊度
    private String notifySound = "default"; //提示音
    private String bgColor = "#fff"; //背景颜色
    
    // 注册时间
    @CreatedDate
    private Date signUpTime;
    // 最后一次登录
    @LastModifiedDate
    private Date lastLogin;
    // 0：正常可用，1：冻结不可用，2：注销不可用
    private Integer status = 0;
    
    private Integer age = 18;
    //在线时长
    private Long onlineTime = 0L;
    private BrowserSetting loginSetting; //登录设备信息
    // 最后一次登录
    private Date lastLoginTime = new Date();
    //分组信息，默认添加“我的好友” 这个分组
    private Map<String, ArrayList<String>> friendFenZu = new HashMap<String, ArrayList<String>>() {
        {
            put("bestFriend", new ArrayList<>());
        }
    };
    //好友备注信息,好友id为key
    private Map<String, String> friendBeiZhu = new HashMap<>();
    // 是否是我的朋友
    private boolean myFriend;
    // 是否是他的朋友
    private boolean hisFriend;
}