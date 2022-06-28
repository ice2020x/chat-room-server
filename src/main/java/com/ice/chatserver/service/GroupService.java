package com.ice.chatserver.service;

import com.ice.chatserver.pojo.Group;
import com.ice.chatserver.pojo.vo.*;

import java.util.List;

public interface GroupService {
    Group getGroupInfo(String groupId);
    
    List<SearchGroupResponseVo> searchGroup(SearchRequestVo requestVo, String userId);
    
    String createGroup(CreateGroupRequestVo requestVo);
    
    List<SearchGroupResultVo> getAllGroup();
    
    void quitGroup(QuitGroupRequestVo requestVo);
}