package com.ice.chatserver.service;

import com.ice.chatserver.pojo.SingleMessage;
import com.ice.chatserver.pojo.vo.HistoryMsgRequestVo;
import com.ice.chatserver.pojo.vo.IsReadMessageRequestVo;
import com.ice.chatserver.pojo.vo.SingleHistoryResultVo;
import com.ice.chatserver.pojo.vo.SingleMessageResultVo;

import java.util.List;

/**
* @author ice2020x
* @Date: 2021/12/19
* @Description: 单人聊天的消息
**/
public interface SingleMessageService {

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 获取好友之间的最后一条聊天记录
    **/
    SingleMessageResultVo getLastMessage(String roomId);

    SingleMessageResultVo getLastMessageOrNull(String roomId);

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 获取好友间的单聊信息
    **/
    List<SingleMessageResultVo> getRecentMessage(String roomId, Integer pageIndex, Integer pageSize);

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 当用户在切换会话阅读消息后，标记该消息已读
    **/
    void userIsReadMessage(IsReadMessageRequestVo ivo);

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 获取单聊的历史记录
    **/
    SingleHistoryResultVo getSingleHistoryMsg(HistoryMsgRequestVo historyMsgVo);

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 添加一条单聊记录
    **/
    void addNewSingleMessage(SingleMessage message);
}
