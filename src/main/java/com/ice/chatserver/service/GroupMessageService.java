package com.ice.chatserver.service;

import com.ice.chatserver.pojo.GroupMessage;
import com.ice.chatserver.pojo.vo.GroupHistoryResultVo;
import com.ice.chatserver.pojo.vo.GroupMessageResultVo;
import com.ice.chatserver.pojo.vo.HistoryMsgRequestVo;

import java.util.List;

public interface GroupMessageService {
    
    List<GroupMessageResultVo> getRecentGroupMessages(String roomId, Integer pageIndex, Integer pageSize);
    
    GroupHistoryResultVo getGroupHistoryMessages(HistoryMsgRequestVo historyMsgRequestVo);
    
    GroupMessageResultVo getGroupLastMessage(String roomId);
    
    void addNewGroupMessage(GroupMessage groupMessage);
}