package com.ice.chatserver.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMessageResultVo {
    private String id;
    private String roomId;
    private String senderId;
    private String senderName;
    private String senderNickname;
    private String senderAvatar;
    private Date time = new Date();
    //文件的原始名字
    private String fileRawName;
    private String message;
    private String messageType = "text";
    private List<String> isReadUser = new ArrayList<>();
}