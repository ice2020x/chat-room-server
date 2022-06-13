package com.ice.chatserver.pojo.vo;

import com.ice.chatserver.pojo.Group;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ice2020x
 */
@Data
public class CurrentConversationVo implements Serializable {
    //    对方的uid
    private String id;
    private String beiZhu;

    private String card;

    //    发起类型，例如FRIEND(朋友)
    private String conversationType;
    //    群id
    private String groupId;
    //    群信息
    private Group groupInfo;

    private Integer holder;
    //    创建时间
    private String createDate;
    //    群则为true
    private Boolean isGroup;

    // 最新消息
    private SingleMessageResultVo lastNews;
    //    最新时间
    private Date lastNewsTime;

    private Integer manager;
    //    房间号
    private String roomId;

    private Date time;
    //    对方的用户名
    private String username;
    private SimpleUser userInfo;
    private String userId;


    private Long level;

    //    我的头像
    private String myAvatar;
    //    我的id
    private String myId;
    //    我的昵称
    private String myNickname;
    //    对方的昵称
    private String nickname;


    //    对方的在线时长
    private Long onlineTime;
    //    对方的头像
    private String photo;
    //    对方的提示音
    private String signature;

    //    webRtcType 例如 audio 语音聊天 artBoard 白板协坐
    private String webRtcType;
    //    disagree，agree，busy
    private String type;
    //    sdp协议对象 必须弄成一个object对象
    private Object sdp;

    public CurrentConversationVo() {
    }
}
