package com.ice.chatserver.controller;

import com.ice.chatserver.common.R;
import com.ice.chatserver.pojo.vo.GroupHistoryResultVo;
import com.ice.chatserver.pojo.vo.GroupMessageResultVo;
import com.ice.chatserver.pojo.vo.HistoryMsgRequestVo;
import com.ice.chatserver.service.GroupMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ice2020x
 * @date 2021-12-19 14:29
 * @description:
 */
@RequestMapping("/groupMessage")
@RestController
public class GroupMessageController {

    @Autowired
    private GroupMessageService groupMessageService;

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 获取最近的群消息
    **/
    @GetMapping("/getRecentGroupMessages")
    public R getRecentGroupMessages(String roomId, Integer pageIndex, Integer pageSize) {
        List<GroupMessageResultVo> recentGroupMessages = groupMessageService.getRecentGroupMessages(roomId, pageIndex, pageSize);
        return R.ok().data("recentGroupMessages", recentGroupMessages);
    }


    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 获取群历史消息
    **/
    @PostMapping("/historyMessages")
    public R getGroupHistoryMessages(@RequestBody HistoryMsgRequestVo historyMsgRequestVo) {
        GroupHistoryResultVo historyMessages = groupMessageService.getGroupHistoryMessages(historyMsgRequestVo);
        return R.ok().data("total", historyMessages.getCount()).data("msgList", historyMessages.getGroupMessages());
    }


    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 获取群最后一条消息
    **/
    @GetMapping("/lastMessage")
    public R getGroupLastMessage(String roomId) {
        GroupMessageResultVo groupLastMessage = groupMessageService.getGroupLastMessage(roomId);
        return R.ok().data("groupLastMessage", groupLastMessage);
    }
}
