package com.ice.chatserver.service;

import com.ice.chatserver.pojo.GoodFriend;
import com.ice.chatserver.pojo.vo.DelGoodFriendRequestVo;
import com.ice.chatserver.pojo.vo.MyFriendListResultVo;
import com.ice.chatserver.pojo.vo.RecentConversationVo;
import com.ice.chatserver.pojo.vo.SingleRecentConversationResultVo;

import java.util.List;

/**
* @author ice2020x
* @Date: 2021/12/19
* @Description: 维持好友关系的逻辑处理接口
**/
public interface GoodFriendService {

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 获取我的好友列表
    **/
    public List<MyFriendListResultVo> getMyFriendsList(String userId);


    public List<SingleRecentConversationResultVo> getRecentConversation(RecentConversationVo recentConversationVo);


    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 删除该好友表中对应的分组信息和备注信息，都要互相更改双方的分组信息
    **/
    public void deleteFriend(DelGoodFriendRequestVo requestVo);

    void addFriend(GoodFriend goodFriend);
}
