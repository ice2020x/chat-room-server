package com.ice.chatserver.pojo.vo;

import com.ice.chatserver.pojo.Group;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MyGroupResultVo {
    private String id;
    private String userId;
    private String username;
    private Integer manager;
    private Integer holder;
    private String card;
    private Date time;
    private List<Group> groupInfo;
    private String groupId;
    private SimpleUser userInfo;
}
