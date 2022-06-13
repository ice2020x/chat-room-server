package com.ice.chatserver.service;

import com.ice.chatserver.pojo.vo.MyGroupResultVo;
import com.ice.chatserver.pojo.vo.RecentGroupVo;
import com.ice.chatserver.pojo.vo.ValidateMessageResponseVo;

import java.util.List;

/**
 * @author ice2020x
 * @date 2021-12-19 11:40
 * @description:
 */
public interface GroupUserService {

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 根据用户名获取我的群聊列表
    **/
    List<MyGroupResultVo> getGroupUsersByUserName(String username);

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 获取最近的群聊
    **/
    List<MyGroupResultVo> getRecentGroup(RecentGroupVo recentGroupVo);

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 根据群id去找所有的群成员信息
    **/
    List<MyGroupResultVo> getGroupUsersByGroupId(String groupId);


    void addNewGroupUser(ValidateMessageResponseVo validateMessage);
}
