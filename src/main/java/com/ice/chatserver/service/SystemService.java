package com.ice.chatserver.service;

import com.ice.chatserver.pojo.FeedBack;
import com.ice.chatserver.pojo.SensitiveMessage;
import com.ice.chatserver.pojo.SystemUser;
import com.ice.chatserver.pojo.vo.FeedBackResultVo;
import com.ice.chatserver.pojo.vo.SensitiveMessageResultVo;
import com.ice.chatserver.pojo.vo.SystemUserResponseVo;

import java.util.List;

public interface SystemService {
    
    public List<SystemUserResponseVo> getSysUsers();
    
    public List<FeedBackResultVo> getFeedbackList();
    
    public List<SensitiveMessageResultVo> getSensitiveMessageList();
    
    public void addSensitiveMessage(SensitiveMessage sensitiveMessage);
    
    public void addFeedBack(FeedBack feedBack);
    
    public void notExistThenAddSystemUser(SystemUser user);
}