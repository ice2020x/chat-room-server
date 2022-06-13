package com.ice.chatserver.controller;

import com.ice.chatserver.common.R;
import com.ice.chatserver.common.ResultEnum;
import com.ice.chatserver.pojo.User;
import com.ice.chatserver.pojo.config.JwtInfo;
import com.ice.chatserver.pojo.vo.*;
import com.ice.chatserver.service.UserService;
import com.ice.chatserver.utils.JwtUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ice2020x
 * @date 2021-12-18 12:46
 * @Description: 用户的控制器
 */
@RequestMapping("/user")
@RestController
@CrossOrigin
public class UserController {

    @Resource
    UserService userService;


    /**
    * @author ice2020x
    * @Date: 2022/6/14
    * @Description:
    **/
    @GetMapping("/getCode")
    public R getVerificationCode(HttpServletRequest request, HttpServletResponse response) {
        return userService.getVerificationCode(request, response);
    }

    /**
     * @author ice2020x
     * @Date: 2021/12/18
     * @Description: 注册
     **/
    @PostMapping("/register")
    public R register(@RequestBody RegisterRequestVo rVo) {
        return userService.register(rVo);
    }

    /**
     * @author ice2020x
     * @Date: 2021/12/20
     * @Description: 根据用户id获取用户详细信息
     **/
    @GetMapping("/getUserInfo/{uid}")
    public R getUserInfo(@PathVariable("uid") String uid) {
        return userService.getUserInfo(uid);
    }


    /**
    * @author ice2020x
    * @Date: 2021/12/20
    * @Description: 添加分组
    **/
    @PostMapping("/addFenZu")
    public R addNewFenZu(@RequestBody NewFenZuRequestVo requestVo) {
        return userService.addNewFenZu(requestVo);
    }

    /**
     * @author ice2020x
     * @Date: 2021/12/18
     * @Description: 修改备注信息
     **/
    @PostMapping("/updateFriendBeiZhu")
    public R modifyFriendBeiZhu(@RequestBody ModifyFriendBeiZhuRequestVo requestVo) {
        return userService.modifyFriendBeiZhu(requestVo);
    }

    /**
     * @author ice2020x
     * @Date: 2021/12/18
     * @Description: 修改好友分组成功
     **/
    @PostMapping("/modifyFriendFenZu")
    public R modifyFriendFenZu(@RequestBody ModifyFriendFenZuRequestVo requestVo) {
        return userService.modifyFriendFenZu(requestVo);
    }

    /**
    * @author ice2020x
    * @Date: 2021/12/20
    * @Description: 删除分组
    **/
    @DeleteMapping("/delFenZu")
    public R deleteFenZu(@RequestBody DelFenZuRequestVo requestVo) {
        return userService.deleteFenZu(requestVo);
    }

    /**
     * @author ice2020x
     * @Date: 2021/12/18
     * @Description: 更新分组名
     **/
    @PostMapping("/editFenZuName")
    public R editFenZu(@RequestBody EditFenZuRequestVo requestVo) {
        return userService.editFenZu(requestVo);
    }

    /**
     * @author ice2020x
     * @Date: 2021/12/18
     * @Description: 更新用户的配置信息
     **/
    @PostMapping("/updateUserConfigure")
    public R updateUserConfigure(@RequestBody UpdateUserConfigureRequestVo requestVo) {

        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean res = userService.updateUserConfigure(requestVo, userId);
        if (res) {
            //同时携带用户信息去更新全局的用户信息
            User userInfo = (User) userService.getUserInfo(userId).getData().get("user");
            return R.ok().data("userInfo", userInfo);
        } else {
            return R.error();
        }
    }

    @PostMapping("/updateUserInfo")
    public R updateUserInfo(@RequestBody UpdateUserInfoRequestVo requestVo) {
        Map<String, Object> resMap = userService.updateUserInfo(requestVo);
        if (resMap.size() > 0) {
            return R.error().code((Integer) resMap.get("code")).message((String) resMap.get("msg"));
        } else {
            return R.ok().message("修改成功");
        }
    }



    @PostMapping("/updateUserPwd")
    public R updateUserPwd(@RequestBody UpdateUserPwdRequestVo requestVo) {
        // System.out.println("更新密码的请求参数为：" + requestVo);
        Map<String, Object> resMap = userService.updateUserPwd(requestVo);
        Integer code = (Integer) resMap.get("code");
        if (code.equals(ResultEnum.SUCCESS.getCode())) {
            return R.ok().message((String) resMap.get("msg"));
        } else {
            return R.error().code(code).message((String) resMap.get("msg"));
        }
    }


    /**
     * @author ice2020x
     * @Date: 2021/12/20
     * @Description: 客户端搜索
     **/
    @PostMapping("/preFetchUser")
    public R searchUser(@RequestBody SearchRequestVo requestVo,HttpServletRequest request) {
        JwtInfo infoByJwtToken = JwtUtils.getInfoByJwtToken(request);
        String userId = infoByJwtToken.getUserId();
        HashMap<String,Object> userList = userService.searchUser(requestVo, userId);
        System.out.println("搜索的用户信息返回的结果为：" + userList);
        return R.ok().data(userList);
    }


}
