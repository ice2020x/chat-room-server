package com.ice.chatserver.service;

import com.ice.chatserver.pojo.FeedBack;
import com.ice.chatserver.pojo.SensitiveMessage;
import com.ice.chatserver.pojo.SystemUser;
import com.ice.chatserver.pojo.vo.FeedBackResultVo;
import com.ice.chatserver.pojo.vo.SensitiveMessageResultVo;
import com.ice.chatserver.pojo.vo.SystemUserResponseVo;

import java.util.List;

/**
 * @author ice2020x
 * @date 2021-12-18 19:49
 * @description:
 */
public interface SystemService {

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 获取系统用户列表
    **/
    public List<SystemUserResponseVo> getSysUsers();

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 获取反馈列表
    **/
    public List<FeedBackResultVo> getFeedbackList();

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 获取敏感词消息列表
    **/
    public List<SensitiveMessageResultVo> getSensitiveMessageList();

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 添加一个敏感词消息
    **/
    public void addSensitiveMessage(SensitiveMessage sensitiveMessage);

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 添加一个反馈消息
    **/
    public void addFeedBack(FeedBack feedBack);

    public void notExistThenAddSystemUser(SystemUser user);
}
