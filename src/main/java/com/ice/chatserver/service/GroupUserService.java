package com.ice.chatserver.service;

import com.ice.chatserver.pojo.vo.MyGroupResultVo;
import com.ice.chatserver.pojo.vo.RecentGroupVo;
import com.ice.chatserver.pojo.vo.ValidateMessageResponseVo;

import java.util.List;

public interface GroupUserService {
    
    List<MyGroupResultVo> getGroupUsersByUserName(String username);
    
    List<MyGroupResultVo> getRecentGroup(RecentGroupVo recentGroupVo);
    
    List<MyGroupResultVo> getGroupUsersByGroupId(String groupId);
    
    void addNewGroupUser(ValidateMessageResponseVo validateMessage);
}