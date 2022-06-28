package com.ice.chatserver.service;

import com.ice.chatserver.pojo.GoodFriend;
import com.ice.chatserver.pojo.vo.DelGoodFriendRequestVo;
import com.ice.chatserver.pojo.vo.MyFriendListResultVo;
import com.ice.chatserver.pojo.vo.RecentConversationVo;
import com.ice.chatserver.pojo.vo.SingleRecentConversationResultVo;

import java.util.List;

public interface GoodFriendService {
    public List<MyFriendListResultVo> getMyFriendsList(String userId);
    
    public List<SingleRecentConversationResultVo> getRecentConversation(RecentConversationVo recentConversationVo);
    
    public void deleteFriend(DelGoodFriendRequestVo requestVo);
    
    void addFriend(GoodFriend goodFriend);
}
