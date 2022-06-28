package com.ice.chatserver.controller;

import com.ice.chatserver.common.R;
import com.ice.chatserver.common.ResultEnum;
import com.ice.chatserver.pojo.config.JwtInfo;
import com.ice.chatserver.pojo.vo.DelGoodFriendRequestVo;
import com.ice.chatserver.pojo.vo.MyFriendListResultVo;
import com.ice.chatserver.pojo.vo.RecentConversationVo;
import com.ice.chatserver.pojo.vo.SingleRecentConversationResultVo;
import com.ice.chatserver.service.GoodFriendService;
import com.ice.chatserver.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 好友控制器
 */
@RestController
@RequestMapping("/goodFriend")
public class GoodFriendController {
    @Autowired
    private GoodFriendService goodFriendService;
    
    //查询我的好友列表
    @GetMapping("/getMyFriendsList")
    public R getMyFriendsList(HttpServletRequest request) {
        JwtInfo infoByJwtToken = JwtUtils.getInfoByJwtToken(request);
        assert infoByJwtToken != null;
        List<MyFriendListResultVo> myFriendsList = goodFriendService.getMyFriendsList(infoByJwtToken.getUserId());
        System.out.println("我的好友列表为：" + myFriendsList);
        return R.ok().data("myFriendsList", myFriendsList);
    }
    
    //查询最近好友列表
    @PostMapping("/recentConversationList")
    public R getRecentConversationList(@RequestBody RecentConversationVo recentConversationVo) {
        List<SingleRecentConversationResultVo> resultVoList = goodFriendService.getRecentConversation(recentConversationVo);
        return R.ok().data("singleRecentConversationList", resultVoList);
    }
    
    //删除好友
    @DeleteMapping("/deleteGoodFriend")
    public R deleteGoodFriend(@RequestBody DelGoodFriendRequestVo requestVo) {
        //principal跟校验token时保存认证信息有关
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userId.equals(requestVo.getUserM())) {
            //不是本人，非法操作
            return R.error().resultEnum(ResultEnum.ILLEGAL_OPERATION);
        }
        goodFriendService.deleteFriend(requestVo);
        return R.ok().message("删除好友成功");
    }
}
