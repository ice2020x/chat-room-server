package com.ice.chatserver.controller;

import com.ice.chatserver.common.R;
import com.ice.chatserver.common.ResultEnum;
import com.ice.chatserver.pojo.User;
import com.ice.chatserver.pojo.config.JwtInfo;
import com.ice.chatserver.pojo.vo.*;
import com.ice.chatserver.service.UserService;
import com.ice.chatserver.utils.JwtUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//用户控制器
@RequestMapping("/user")
@RestController
@CrossOrigin
public class UserController {
    @Resource
    UserService userService;
    
    //获取图像验证码
    @GetMapping("/getCode")
    public R getVerificationCode(HttpServletRequest request, HttpServletResponse response) {
        return userService.getVerificationCode(request, response);
    }
    
    //注册账号
    @PostMapping("/register")
    public R register(@RequestBody RegisterRequestVo rVo) {
        return userService.register(rVo);
    }
    
    //根据用户id获取用户详细信息
    @GetMapping("/getUserInfo/{uid}")
    public R getUserInfo(@PathVariable("uid") String uid, HttpServletRequest request) {
        return userService.getUserInfo(uid, request);
    }
    
    //添加分组
    @PostMapping("/addFenZu")
    public R addNewFenZu(@RequestBody NewFenZuRequestVo requestVo) {
        return userService.addNewFenZu(requestVo);
    }
    
    //修改好友备注
    @PostMapping("/updateFriendBeiZhu")
    public R modifyFriendBeiZhu(@RequestBody ModifyFriendBeiZhuRequestVo requestVo, HttpServletRequest request) {
        String currentUserId = JwtUtils.getCurrentUserId(request);
        if (currentUserId == null) {
            throw new RuntimeException("用户未登录");
        }
        return userService.modifyFriendBeiZhu(requestVo, currentUserId);
    }
    
    //修改好友分组
    @PostMapping("/modifyFriendFenZu")
    public R modifyFriendFenZu(@RequestBody ModifyFriendFenZuRequestVo requestVo) {
        return userService.modifyFriendFenZu(requestVo);
    }
    
    //删除分组
    @DeleteMapping("/delFenZu")
    public R deleteFenZu(@RequestBody DelFenZuRequestVo requestVo) {
        return userService.deleteFenZu(requestVo);
    }
    
    //修改分组名
    @PostMapping("/editFenZuName")
    public R editFenZu(@RequestBody EditFenZuRequestVo requestVo) {
        return userService.editFenZu(requestVo);
    }
    
    //修改用户的个性化设置
    @PostMapping("/updateUserConfigure")
    public R updateUserConfigure(@RequestBody UpdateUserConfigureRequestVo requestVo, HttpServletRequest request) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean res = userService.updateUserConfigure(requestVo, userId);
        if (res) {
            //同时携带用户信息去更新全局的用户信息
            User userInfo = (User) userService.getUserInfo(userId, request).getData().get("user");
            return R.ok().data("userInfo", userInfo);
        }
        else {
            return R.error();
        }
    }
    
    //更新用户信息
    @PostMapping("/updateUserInfo")
    public R updateUserInfo(@RequestBody UpdateUserInfoRequestVo requestVo, HttpServletRequest request) {
        String userId = getUserId(request);
        requestVo.setUserId(userId);
        Map<String, Object> resMap = userService.updateUserInfo(requestVo);
        if (resMap.size() > 0) {
            return R.error().code((Integer) resMap.get("code")).message((String) resMap.get("msg"));
        }
        else {
            return R.ok().message("修改成功");
        }
    }
    
    //更改密码
    @PostMapping("/updateUserPwd")
    public R updateUserPwd(@RequestBody UpdateUserPwdRequestVo requestVo) {
        Map<String, Object> resMap = userService.updateUserPwd(requestVo);
        Integer code = (Integer) resMap.get("code");
        if (code.equals(ResultEnum.SUCCESS.getCode())) {
            return R.ok().message((String) resMap.get("msg"));
        }
        else {
            return R.error().code(code).message((String) resMap.get("msg"));
        }
    }
    
    //搜索用户
    @PostMapping("/preFetchUser")
    public R searchUser(@RequestBody SearchRequestVo requestVo, HttpServletRequest request) {
        JwtInfo infoByJwtToken = JwtUtils.getInfoByJwtToken(request);
        String userId = infoByJwtToken.getUserId();
        HashMap<String, Object> userList = userService.searchUser(requestVo, userId);
        System.out.println("搜索的用户信息返回的结果为：" + userList);
        return R.ok().data(userList);
    }
    
    //获取当前登录的用户的uid
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
