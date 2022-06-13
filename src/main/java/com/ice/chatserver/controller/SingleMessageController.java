package com.ice.chatserver.controller;

import com.ice.chatserver.common.R;
import com.ice.chatserver.pojo.vo.HistoryMsgRequestVo;
import com.ice.chatserver.pojo.vo.IsReadMessageRequestVo;
import com.ice.chatserver.pojo.vo.SingleHistoryResultVo;
import com.ice.chatserver.pojo.vo.SingleMessageResultVo;
import com.ice.chatserver.service.SingleMessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ice2020x
 * @date 2021-12-19 15:01
 * @description: 单对单聊天的消息
 */
@RequestMapping("/singleMessage")
@RestController
public class SingleMessageController {
    @Resource
    private SingleMessageService singleMessageService;


    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 获取好友之间的最后一条聊天记录
     **/
    @GetMapping("/getLastMessage")
    public R getLastMessage(String roomId) {
        SingleMessageResultVo lastMessage = singleMessageService.getLastMessage(roomId);
        return R.ok().data("singleLastMessage", lastMessage);
    }


    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 获取最近的单聊消息
     **/
    @GetMapping("/getRecentSingleMessages")
    public R getRecentSingleMessages(String roomId, Integer pageIndex, Integer pageSize) {
        System.out.println(roomId+":"+pageIndex+":"+pageSize);
        List<SingleMessageResultVo> recentMessage = singleMessageService.getRecentMessage(roomId, pageIndex, pageSize);
        return R.ok().data("recentMessage", recentMessage);
    }

    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 当用户在切换会话阅读消息后，标记该消息已读
     **/
    @PostMapping("/isRead")
    public R userIsReadMessage(@RequestBody IsReadMessageRequestVo ivo) {
        singleMessageService.userIsReadMessage(ivo);
        return R.ok();
    }

    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 获取单聊历史记录
     **/
    @PostMapping("/historyMessage")
    public R getSingleHistoryMessages(@RequestBody HistoryMsgRequestVo historyMsgVo) {
        System.out.println("查看历史消息的请求参数为：" + historyMsgVo);
        SingleHistoryResultVo singleHistoryMsg = singleMessageService.getSingleHistoryMsg(historyMsgVo);
        return R.ok().data("total", singleHistoryMsg.getTotal()).data("msgList", singleHistoryMsg.getMsgList());
    }
}
