package com.ice.chatserver.service;

import com.ice.chatserver.pojo.GroupMessage;
import com.ice.chatserver.pojo.vo.GroupHistoryResultVo;
import com.ice.chatserver.pojo.vo.GroupMessageResultVo;
import com.ice.chatserver.pojo.vo.HistoryMsgRequestVo;

import java.util.List;

/**
* @author ice2020x
* @Date: 2021/12/19
* @Description: 群消息的逻辑处理类
**/
public interface GroupMessageService {


    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 获取最近的群消息
    **/
    List<GroupMessageResultVo> getRecentGroupMessages(String roomId, Integer pageIndex, Integer pageSize);

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 获取群历史消息
    **/
    GroupHistoryResultVo getGroupHistoryMessages(HistoryMsgRequestVo historyMsgRequestVo);

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 获取群的最哈后一条信息
    **/
    GroupMessageResultVo getGroupLastMessage(String roomId);

    void addNewGroupMessage(GroupMessage groupMessage);
}
