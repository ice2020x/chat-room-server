package com.ice.chatserver.service;

import com.ice.chatserver.pojo.Group;
import com.ice.chatserver.pojo.vo.*;

import java.util.List;

public interface GroupService {

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 根据id查询一调记录
    **/
    Group getGroupInfo(String groupId);

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 客户端搜索群聊
    **/
    List<SearchGroupResponseVo> searchGroup(SearchRequestVo requestVo, String userId);

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 创建群聊消息
    **/
    String createGroup(CreateGroupRequestVo requestVo);

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 获取所有群聊
    **/
    List<SearchGroupResultVo> getAllGroup();

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 退出群聊
    **/
    void quitGroup(QuitGroupRequestVo requestVo);
}
