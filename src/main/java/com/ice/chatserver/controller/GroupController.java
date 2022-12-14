package com.ice.chatserver.controller;

import com.ice.chatserver.common.R;
import com.ice.chatserver.common.ResultEnum;
import com.ice.chatserver.pojo.Group;
import com.ice.chatserver.pojo.config.JwtInfo;
import com.ice.chatserver.pojo.vo.*;
import com.ice.chatserver.service.GroupService;
import com.ice.chatserver.service.GroupUserService;
import com.ice.chatserver.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//群聊控制器
@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    GroupService groupService;
    
    @Autowired
    GroupUserService groupUserService;
    
    //根据用户名获取我的群聊列表
    @GetMapping("/getMyGroupList")
    public R getMyGroupList(HttpServletRequest request) {
        JwtInfo infoByJwtToken = JwtUtils.getInfoByJwtToken(request);
        List<MyGroupResultVo> myGroupList = groupUserService.getGroupUsersByUserName(infoByJwtToken.getUsername());
        System.out.println("我的群聊列表为：" + myGroupList);
        System.out.println(myGroupList);
        return R.ok().data("myGroupList", myGroupList);
    }
    
    
    //根据ids获取群聊列表
    @PostMapping("/recentGroup")
    public R getRecentGroup(@RequestBody RecentGroupVo recentGroupVo) {
        System.out.println("最近的群聊列表请求参数为：" + recentGroupVo);
        List<MyGroupResultVo> recentGroups = groupUserService.getRecentGroup(recentGroupVo);
        System.out.println("最近的群聊列表为：" + recentGroups);
        return R.ok().data("recentGroups", recentGroups);
    }
    
    
    //获取群聊的详情
    @GetMapping("/getGroupInfo")
    public R getGroupInfo(String groupId) {
        Group groupInfo = groupService.getGroupInfo(groupId);
        System.out.println("查询出的群消息为：" + groupInfo);
        List<MyGroupResultVo> groupUsers = groupUserService.getGroupUsersByGroupId(groupId);
        System.out.println("群聊详情为：" + groupUsers);
        return R.ok().data("groupInfo", groupInfo).data("users", groupUsers);
    }
    
    //搜索群聊
    @PostMapping("/preFetchGroup")
    public R searchGroup(@RequestBody SearchRequestVo requestVo) {
        // 这个 principal 跟校验token时保存认证信息有关
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<SearchGroupResponseVo> groupResponseVos = groupService.searchGroup(requestVo, userId);
        return R.ok().data("groupList", groupResponseVos);
    }
    
    //创建群聊
    @PostMapping("/createGroup")
    public R createGroup(@RequestBody CreateGroupRequestVo requestVo) {
        String groupCode = groupService.createGroup(requestVo);
        return R.ok().data("groupCode", groupCode);
    }
    
    //获取所有群聊
    @GetMapping("/finAll")
    public R getAllGroup() {
        List<SearchGroupResultVo> allGroup = groupService.getAllGroup();
        return R.ok().data("allGroup", allGroup);
    }
    
    //退出群聊
    @PostMapping("/quitGroup")
    public R quitGroup(@RequestBody QuitGroupRequestVo requestVo) {
        System.out.println("退出群聊的请求参数为：" + requestVo);
        // principal跟校验token时保存认证信息有关
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userId.equals(requestVo.getUserId())) {
            //不是本人，非法操作
            return R.error().resultEnum(ResultEnum.ILLEGAL_OPERATION);
        }
        groupService.quitGroup(requestVo);
        return R.ok().message("操作成功");
    }
}