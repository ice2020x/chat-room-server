package com.ice.chatserver.controller;

import com.ice.chatserver.common.R;
import com.ice.chatserver.common.ResultEnum;
import com.ice.chatserver.pojo.SuperUser;
import com.ice.chatserver.service.SuperUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

//管理员控制器
@RestController
@RequestMapping("/superuser")
public class SuperUserController {
    @Resource
    private SuperUserService superUserService;
    
    //管理员登录
    @PostMapping("/login")
    public R superUserLogin(@RequestBody SuperUser superUser) {
        Map<String, Object> resMap = superUserService.superUserLogin(superUser);
        Integer code = (Integer) resMap.get("code");
        if (code.equals(ResultEnum.LOGIN_SUCCESS.getCode())) {
            return R.ok().resultEnum(ResultEnum.LOGIN_SUCCESS)
                    .data("userInfo", resMap.get("userInfo"))
                    .data("token", resMap.get("token"));
        }
        else {
            return R.error().code(code).message((String) resMap.get("msg"));
        }
    }
}
