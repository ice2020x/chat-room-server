package com.ice.chatserver.service;

import com.ice.chatserver.pojo.ValidateMessage;
import com.ice.chatserver.pojo.vo.ValidateMessageResponseVo;

import java.util.List;

public interface ValidateMessageService {
    
    public List<ValidateMessageResponseVo> getMyValidateMessageList(String userId, Integer status, Integer validateType);
    
    public ValidateMessage findValidateMessage(String roomId, Integer status, Integer validateType);
    
    public ValidateMessage addValidateMessage(ValidateMessage validateMessage);
    
    public void changeFriendValidateNewsStatus(String validateMessageId, Integer status);
    
    public void changeGroupValidateNewsStatus(String validateMessageId, Integer status);
}