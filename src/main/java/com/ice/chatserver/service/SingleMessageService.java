package com.ice.chatserver.service;

import com.ice.chatserver.pojo.SingleMessage;
import com.ice.chatserver.pojo.vo.HistoryMsgRequestVo;
import com.ice.chatserver.pojo.vo.IsReadMessageRequestVo;
import com.ice.chatserver.pojo.vo.SingleHistoryResultVo;
import com.ice.chatserver.pojo.vo.SingleMessageResultVo;

import java.util.List;

public interface SingleMessageService {
    
    SingleMessageResultVo getLastMessage(String roomId);
    
    SingleMessageResultVo getLastMessageOrNull(String roomId);
    
    List<SingleMessageResultVo> getRecentMessage(String roomId, Integer pageIndex, Integer pageSize);
    
    void userIsReadMessage(IsReadMessageRequestVo ivo);
    
    SingleHistoryResultVo getSingleHistoryMsg(HistoryMsgRequestVo historyMsgVo);
    
    void addNewSingleMessage(SingleMessage message);
}