package com.ice.chatserver.pojo.vo;

import com.ice.chatserver.pojo.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MyFriendListVo {
    private String id;
    private Date createDate;
    private String userM;
    private String userY;
    private List<User> uList;
    private List<User> uList1;
    private List<User> uList2;
}