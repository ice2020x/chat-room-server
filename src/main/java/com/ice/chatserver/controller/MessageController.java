package com.ice.chatserver.controller;

import com.ice.chatserver.common.R;
import com.ice.chatserver.dao.UserDao;
import com.ice.chatserver.pojo.User;
import com.ice.chatserver.pojo.config.JwtInfo;
import com.ice.chatserver.pojo.vo.*;
import com.ice.chatserver.service.*;
import com.ice.chatserver.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author ice2020x
 * @date 2021-12-21 2:54
 * @description: 所有消息的一个联查控制类
 */
@RequestMapping("/message")
@RestController
public class MessageController {

    @Autowired
    SingleMessageService singleMessageService;

    @Autowired
    GroupMessageService groupMessageService;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupUserService groupUserService;
    @Autowired
    GoodFriendService goodFriendService;
    @Autowired
    UserDao userDao;

    @GetMapping("/messageListItem")
    public R messageListItem(HttpServletRequest request) {
//        获取所有群聊
        JwtInfo infoByJwtToke = JwtUtils.getInfoByJwtToken(request);
//        JwtInfo infoByJwtToke = new JwtInfo();
//        infoByJwtToke.setUserId("61be0e6ce7fd6865cbcd74ca");
//        infoByJwtToke.setUsername("1234567");
        List<MyGroupResultVo> myGroupList = groupUserService.getGroupUsersByUserName(infoByJwtToke.getUsername());
        List<MessageListVo> messageListVos = new ArrayList<MessageListVo>();
        for (MyGroupResultVo myGroupResultVo : myGroupList) {
            GroupMessageResultVo groupLastMessage = groupMessageService.getGroupLastMessage(myGroupResultVo.getGroupId());
            if (groupLastMessage != null) {
                MessageListVo messageListVo = new MessageListVo();
                messageListVo.setLastMessage(groupLastMessage.getMessage());
                messageListVo.setId(groupLastMessage.getRoomId());
                messageListVo.setNickname(groupLastMessage.getSenderNickname());
                messageListVo.setPhoto(groupLastMessage.getSenderAvatar());
                messageListVo.setUsername(groupLastMessage.getSenderName());
                messageListVo.setTime(groupLastMessage.getTime());
                messageListVos.add(messageListVo);
            }
            System.out.println(groupLastMessage);
        }
        List<MyFriendListResultVo> myFriendsList = goodFriendService.getMyFriendsList(infoByJwtToke.getUserId());
        System.out.println("myFriendsList:"+myFriendsList);
        for (MyFriendListResultVo myFriendListResultVo : myFriendsList) {
            System.out.println("myFriendListResultVo:"+myFriendListResultVo);
            System.out.println("myFriendListResultVo.getRoomId():"+myFriendListResultVo.getRoomId());
            System.out.println("getRoomId:"+myFriendListResultVo.getRoomId());
            SingleMessageResultVo lastMessage = singleMessageService.getLastMessageOrNull(myFriendListResultVo.getRoomId());
            System.out.println("lastMessage:"+lastMessage);
            if (lastMessage != null) {
                MessageListVo messageListVo = new MessageListVo();
                messageListVo.setLastMessage(lastMessage.getMessage());
                messageListVo.setId(lastMessage.getRoomId());
                String roomId = lastMessage.getRoomId();
                String userId = infoByJwtToke.getUserId();
                String replace = roomId.replace(userId, "");
                User user = userDao.findById(new ObjectId(replace)).orElse(null);
                assert user != null;
                messageListVo.setNickname(user.getNickname());
                messageListVo.setPhoto(user.getPhoto());
                messageListVo.setUsername(user.getUsername());
                messageListVo.setTime(lastMessage.getTime());
                messageListVos.add(messageListVo);
            }
        }
        Collections.sort(messageListVos);
        return R.ok().message("获取消息列表成功").data("list",messageListVos);
    }

}
