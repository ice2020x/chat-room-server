package com.ice.chatserver.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author ice2020x
 * @date 2021-12-21 4:12
 * @description:
 */
@Data
public class MessageListVo  implements Comparable<MessageListVo>{

    private String id;
    private String photo;
    private String username;
    private String nickname;
    private  String friendBeiZu;

//    最后一条消息
    private String lastMessage;
//    最后一条消息的时间
    private Date time;

    @Override
    public int compareTo(MessageListVo o) {
        long l = this.time.getTime() - o.time.getTime();
        return Math.toIntExact(l);
    }
}
