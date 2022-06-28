package com.ice.chatserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ice.chatserver.common.R;
import com.ice.chatserver.pojo.UserSetting;
import com.ice.chatserver.pojo.config.JwtInfo;
import com.ice.chatserver.service.UserSettingService;
import com.ice.chatserver.utils.JwtUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

//用户设置控制器
@RequestMapping("/user/setting")
@RestController
public class UserSettingController {
    @Resource
    UserSettingService userSettingService;
    
    //获取当前用户设置
    @GetMapping("/")
    public R getCurrentUserSetting(HttpServletRequest request) {
        final String userId = getUserId(request);
        final UserSetting userSetting = userSettingService.getOne(new QueryWrapper<UserSetting>().eq("user_id", userId));
        if (userSetting == null) {
            // 当前用户没有对应的用户设置数据
            UserSetting newSetting = new UserSetting();
            newSetting.setUserId(userId);
            userSettingService.save(newSetting);
            return R.ok().data("setting", newSetting);
        }
        return R.ok().data("setting", userSetting);
    }
    
    //更新当前用户设置
    @PutMapping("/update")
    public R updateCurrentUserSetting(HttpServletRequest request, @RequestBody UserSetting userSetting) {
        final String userId = getUserId(request);
        final UserSetting oldSetting = userSettingService.getOne(new QueryWrapper<UserSetting>().eq("user_id", userId));
        if (oldSetting == null) {
            // 当前用户没有对应的用户设置数据
            userSetting.setUserId(userId);
            userSettingService.save(userSetting);
            return R.ok().data("setting", userSetting);
        }
        userSettingService.update(userSetting, new UpdateWrapper<UserSetting>().eq("user_id", userId));
        return R.ok().data("setting", userSetting);
    }
    
    //获取当前用户ID
    private String getUserId(HttpServletRequest request) {
        JwtInfo infoByJwtToke = JwtUtils.getInfoByJwtToken(request);
        if (ObjectUtils.isEmpty(infoByJwtToke)) {
            throw new RuntimeException("用户未登录");
        }
        final String userId = infoByJwtToke.getUserId();
        if (StringUtils.isBlank(userId)) {
            throw new RuntimeException("用户未登录");
        }
        return userId;
    }
}