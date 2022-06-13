package com.ice.chatserver.controller;

import com.ice.chatserver.common.R;
import com.ice.chatserver.pojo.ValidateMessage;
import com.ice.chatserver.pojo.vo.ValidateMessageResponseVo;
import com.ice.chatserver.service.ValidateMessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ice2020x
 * @date 2021-12-18 22:56
 * @description: 验证消息的类
 */
@RequestMapping("/validate")
@RestController
public class ValidateMessageController {

    @Resource
    private ValidateMessageService validateMessageService;

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 根据条件查询验证消息列表
    **/
    @GetMapping("/getMyValidateMessageList")
    @ResponseBody
    public R getMyValidateMessageList(String userId,Integer status, Integer validateType) {
        System.out.println(userId+":"+status+":"+validateType);
        List<ValidateMessageResponseVo> validateMessageList = validateMessageService.getMyValidateMessageList(userId,status,validateType);
        return R.ok().data("validateMessageList", validateMessageList);
    }

    
    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 根据条件查询某条验证信息
    **/
    @GetMapping("/getValidateMessage")
    public R getValidateMessage(String roomId, Integer status, Integer validateType) {
        ValidateMessage validateMessage = validateMessageService.findValidateMessage(roomId, status, validateType);
        return R.ok().data("validateMessage", validateMessage);
    }
}
