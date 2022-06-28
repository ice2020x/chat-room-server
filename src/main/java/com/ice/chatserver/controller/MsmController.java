package com.ice.chatserver.controller;

import com.ice.chatserver.common.R;
import com.ice.chatserver.service.MsmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

//短信验证控制器
@RestController
@RequestMapping("/sms")
public class MsmController {
    @Autowired
    private MsmService msmService;
    
    //发送短信验证码
    @GetMapping("/send/{phone}")
    public R sendMsm(HttpServletRequest request, @PathVariable("phone") String phone) {
        msmService.sendMsg(request, phone);
        return R.ok().message("发送短信验证码成功");
    }
}
