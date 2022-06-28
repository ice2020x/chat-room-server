package com.ice.chatserver.controller;

import com.ice.chatserver.common.R;
import com.ice.chatserver.pojo.ValidateMessage;
import com.ice.chatserver.pojo.vo.ValidateMessageResponseVo;
import com.ice.chatserver.service.ValidateMessageService;
import com.ice.chatserver.utils.JwtUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

//验证消息控制器
@RequestMapping("/validate")
@RestController
public class ValidateMessageController {
    @Resource
    private ValidateMessageService validateMessageService;
    
    //根据条件查询验证消息列表
    @GetMapping("/getMyValidateMessageList")
    @ResponseBody
    public R getMyValidateMessageList(HttpServletRequest request, Integer status, Integer validateType) {
        String currentUserId = JwtUtils.getCurrentUserId(request);
        if (currentUserId == null) {
            throw new RuntimeException("用户未登录");
        }
        System.out.println(currentUserId + ":" + status + ":" + validateType);
        List<ValidateMessageResponseVo> validateMessageList = validateMessageService.getMyValidateMessageList(currentUserId, status, validateType);
        return R.ok().data("validateMessageList", validateMessageList);
    }
    
    //根据条件查询某条验证信息
    @GetMapping("/getValidateMessage")
    public R getValidateMessage(String roomId, Integer status, Integer validateType) {
        ValidateMessage validateMessage = validateMessageService.findValidateMessage(roomId, status, validateType);
        return R.ok().data("validateMessage", validateMessage);
    }
}
