package com.ice.chatserver.service;


import com.ice.chatserver.pojo.ValidateMessage;
import com.ice.chatserver.pojo.vo.ValidateMessageResponseVo;

import java.util.List;

/**
* @author ice2020x
* @Date: 2021/12/18
* @Description: 验证消息
**/
public interface ValidateMessageService {

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 获取该用户的验证信息
    **/
    public List<ValidateMessageResponseVo> getMyValidateMessageList(String userId,Integer status, Integer validateType);


    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 根据条件获取一个验证消息
    **/
    public ValidateMessage findValidateMessage(String roomId, Integer status, Integer validateType);

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 添加一个验证消息
    **/
    public ValidateMessage addValidateMessage(ValidateMessage validateMessage);

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 更新好友验证消息的状态
    **/
    public void changeFriendValidateNewsStatus(String validateMessageId, Integer status);

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 更新群聊验证消息的状态
    **/
    public void changeGroupValidateNewsStatus(String validateMessageId, Integer status);
}
