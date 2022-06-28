package com.ice.chatserver.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.ice.chatserver.common.ConstValueEnum;
import com.ice.chatserver.common.SocketR;
import com.ice.chatserver.common.constant.SocketRConstant;
import com.ice.chatserver.filter.SensitiveFilter;
import com.ice.chatserver.pojo.*;
import com.ice.chatserver.pojo.vo.*;
import com.ice.chatserver.service.*;
import com.ice.chatserver.utils.DateUtil;
import com.ice.chatserver.utils.SocketIoServerMapUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ice2020x
 * @date 2021-12-19 16:52
 * @description: map：clientId -> uid（用户判断用户上下线），uid -> simpleUser（用于查询是否已经登录） 消息处理器，开启了事务支持
 */
@Slf4j
@Component
@Transactional(rollbackFor = Throwable.class)
public class SocketEventHandler {
    @Resource
    private SocketIOServer socketIOServer;
    
    @Resource
    private SystemService sysService;
    
    @Resource
    private GroupUserService groupUserService;
    
    @Resource
    private GoodFriendService goodFriendService;
    
    @Resource
    private ValidateMessageService validateMessageService;
    
    @Resource
    private GroupMessageService groupMessageService;
    
    @Resource
    private SingleMessageService singleMessageService;
    
    @Resource
    private UserService userService;
    
    @Resource
    private SensitiveFilter sensitiveFilter;
    
    @Resource
    private OnlineUserService onlineUserService;
    
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 客户端连接的时候触发
     **/
    @OnConnect
    public void eventOnConnect(SocketIOClient client) {
        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
        System.out.println("客户端:连接参数" + urlParams);
        System.out.println("客户端唯一标识为：" + client.getSessionId());
        log.info("链接开启，urlParams：{}", urlParams);
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 打印当前服务器的一个用户状态
     **/
    private void printMessage() {
        log.info("当前在线客户端为：{}", SocketIoServerMapUtil.getClientToUidMap());
        log.info("在线用户的信息为：{}", SocketIoServerMapUtil.getUidToUserMap());
        log.info("当前在线用户人数为：{}", onlineUserService.countOnlineUser());
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 清除用户登录状态
     **/
    private void cleanLoginInfo(String clientId) {
        SimpleUser simpleUser = onlineUserService.getSimpleUserByClientId(clientId);
        if (simpleUser != null) {
            onlineUserService.removeClientAndUidInSet(clientId, simpleUser.getUid());
            //设置下线用户的在线时长
            long onlineTime = DateUtil.getTimeDelta(simpleUser.getLastLoginTime(), new Date());
            userService.updateOnlineTime(onlineTime, simpleUser.getUid());
        }
        printMessage();
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 浏览器关闭或者关掉这个网站都会触发
     **/
    @OnDisconnect
    public void eventOnDisConnect(SocketIOClient client) {
        log.info("eventOnDisConnect ---> 客户端唯一标识为：{}", client.getSessionId());
        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
        //清除用户登录信息
        cleanLoginInfo(client.getSessionId().toString());
        log.info("链接关闭，urlParams：{}", urlParams);
        log.info("剩余在线人数：{}", SocketIoServerMapUtil.getUidToUserMap().size());
        log.info("剩余在线人数：{}", onlineUserService.countOnlineUser());
        socketIOServer.getBroadcastOperations().sendEvent("onlineUser", onlineUserService.getOnlineUidSet());
    }
    
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 用户上线了
     **/
    @OnEvent("goOnline")
    public void online(SocketIOClient client, User user) {
        log.info("online -------------> user：{}", user);
        String clientId = client.getSessionId().toString();
        SimpleUser simpleUser = new SimpleUser();
        BeanUtils.copyProperties(user, simpleUser);
        onlineUserService.addClientIdToSimpleUser(clientId, simpleUser);
        printMessage();
        //广播所有在线用户
        socketIOServer.getBroadcastOperations().sendEvent("onlineUser", onlineUserService.getOnlineUidSet());
    }
    
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 用户下线了
     **/
    @OnEvent("leave")
    public void leave(SocketIOClient client) {
        log.info("leave --------------> client：{}", client);
        cleanLoginInfo(client.getSessionId().toString());
        socketIOServer.getBroadcastOperations().sendEvent("onlineUser", onlineUserService.getOnlineUidSet());
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/20
     * @Description: 是否读了这个消息
     **/
    @OnEvent("isReadMsg")
    public void isReadMsg(SocketIOClient client, UserIsReadMsgRequestVo requestVo) {
        log.info("isReadMsg ---> requestVo：{}", requestVo);
        if (requestVo.getRoomId() != null) {
            //给同一房间的发送消息，这样才能同步状态
            //实际上同一房间只有2个客户端，对于这个1v1
            Collection<SocketIOClient> clients = socketIOServer.getRoomOperations(requestVo.getRoomId()).getClients();
            for (SocketIOClient item : clients) {
                if (item != client) {
                    item.sendEvent("isReadMsg", requestVo);
                }
            }
        }
    }
    
    @OnEvent("join")
    public void join(SocketIOClient client, CurrentConversationVo conversationVo) {
        log.info("加入房间号码：{} -------------> conversationVo：{}", conversationVo.getRoomId(), conversationVo);
        //当前登录的客户端加入到指定房间
        client.joinRoom(conversationVo.getRoomId());
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 发送一个新的消息
     **/
    @OnEvent("sendNewMessage")
    public void sendNewMessage(SocketIOClient client, NewMessageVo newMessageVo) {
        log.info("sendNewMessage -------------> newMessageVo：{}", newMessageVo);
        if (newMessageVo.getConversationType().equals(ConstValueEnum.FRIEND)) {
            SingleMessage singleMessage = new SingleMessage();
            BeanUtils.copyProperties(newMessageVo, singleMessage);
            singleMessage.setSenderId(new ObjectId(newMessageVo.getSenderId()));
            System.out.println("待插入的单聊消息为：" + singleMessage);
            singleMessageService.addNewSingleMessage(singleMessage);
        }
        else if (newMessageVo.getConversationType().equals(ConstValueEnum.GROUP)) {
            GroupMessage groupMessage = new GroupMessage();
            BeanUtils.copyProperties(newMessageVo, groupMessage);
            groupMessage.setSenderId(new ObjectId(newMessageVo.getSenderId()));
            System.out.println("待插入的群聊消息为：" + groupMessage);
            groupMessageService.addNewGroupMessage(groupMessage);
        }
        //通知该房间收到消息接受到消息
        //实际上同一房间只有2个客户端
        Collection<SocketIOClient> clients = socketIOServer.getRoomOperations(newMessageVo.getRoomId()).getClients();
        System.out.println("clients" + clients);
        for (SocketIOClient item : clients) {
            System.out.println("item:" + item);
            if (item != client) {
                log.info("receiveMessage------------->");
                item.sendEvent("receiveMessage", newMessageVo);
            }
        }
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 发送验证消息，注意要防止重复添加，若status为0时
     **/
    @OnEvent("sendValidateMessage")
    public void sendValidateMessage(SocketIOClient client, ValidateMessage validateMessage) {
        log.info("sendValidateMessage -------------> validateMessage：{}", validateMessage);
        String[] res = sensitiveFilter.filter(validateMessage.getAdditionMessage());
        String filterContent = "";
        if (res != null) {
            filterContent = res[0];
            //添加敏感词消息记录
            if (res[1].equals("1")) {
                SensitiveMessage sensitiveMessage = new SensitiveMessage();
                sensitiveMessage.setRoomId(validateMessage.getRoomId());
                sensitiveMessage.setSenderId(validateMessage.getSenderId().toString());
                sensitiveMessage.setSenderName(validateMessage.getSenderName());
                sensitiveMessage.setMessage(validateMessage.getAdditionMessage());
                sensitiveMessage.setType(ConstValueEnum.VALIDATE);
                sensitiveMessage.setTime(validateMessage.getTime());
                sysService.addSensitiveMessage(sensitiveMessage);
            }
        }
        validateMessage.setAdditionMessage(filterContent);
        ValidateMessage addValidateMessage = validateMessageService.addValidateMessage(validateMessage);
        //验证消息添加成功了才通知房间消息
        if (addValidateMessage != null) {
            //实际上同一房间只有2个客户端
            Collection<SocketIOClient> clients = socketIOServer.getRoomOperations(validateMessage.getRoomId()).getClients();
            for (SocketIOClient item : clients) {
                if (item != client) {
                    item.sendEvent("receiveValidateMessage", validateMessage);
                }
            }
            client.sendEvent("sendValidateMessage", SocketR.ok().event(SocketRConstant.EventCode.VALIDATE_MESSAGE).data("data", validateMessage));
        }
        else {
            client.sendEvent("sendValidateMessage", SocketR.error().event(SocketRConstant.EventCode.VALIDATE_MESSAGE).data("data", validateMessage));
        }
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 同意添加好友
     **/
    @OnEvent("sendAgreeFriendValidate")
    public void sendAgreeFriendValidate(SocketIOClient client, ValidateMessageResponseVo validateMessage) {
        log.info("sendAgreeFriendValidate -------------> validateMessage：{}", validateMessage);
        try {
            GoodFriend goodFriend = new GoodFriend();
            goodFriend.setUserM(new ObjectId(validateMessage.getSenderId()));
            goodFriend.setUserY(new ObjectId(validateMessage.getReceiverId()));
            goodFriendService.addFriend(goodFriend);
            
            // 用户同意加好友之后改变验证消息的状态
            validateMessageService.changeFriendValidateNewsStatus(validateMessage.getId(), 1);
            //原本是接收者房间
            String roomId = validateMessage.getRoomId();
            //接收者
            String receiverId = validateMessage.getReceiverId();
            //发送者
            String senderId = validateMessage.getSenderId();
            //把接受者id替换为发送者id回到同意加好友的这方房间去更新我的好友列表
            String senderRoomId = roomId.replaceAll(receiverId, senderId);
            //向roomId传送验证消息，不再通知接收者房间里的客户端，因为前端通过eventbus处理了
            //实际上同一房间只有2个客户端
            Collection<SocketIOClient> clients = socketIOServer.getRoomOperations(senderRoomId).getClients();
            for (SocketIOClient item : clients) {
                if (item != client) {
                    //通知发送者房间，除了当前客户端
                    item.sendEvent("receiveAgreeFriendValidate", validateMessage);
                }
            }
            client.sendEvent("sendAgreeFriendValidate", SocketR.ok().event(SocketRConstant.EventCode.VALIDATE_MESSAGE).data("data", validateMessage));
        } catch (Exception e) {
            client.sendEvent("sendAgreeFriendValidate", SocketR.error().event(SocketRConstant.EventCode.VALIDATE_MESSAGE).data("data", validateMessage));
        }
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 拒绝好友请求
     **/
    @OnEvent("sendDisAgreeFriendValidate")
    public void sendDisAgreeFriendValidate(SocketIOClient client, ValidateMessageResponseVo validateMessage) {
        log.info("sendDisAgreeFriendValidate ---> validateMessage：{}", validateMessage);
        try {
            validateMessageService.changeFriendValidateNewsStatus(validateMessage.getId(), 2);
            client.sendEvent("sendDisAgreeFriendValidate", SocketR.ok().event(SocketRConstant.EventCode.VALIDATE_MESSAGE).data("data", validateMessage));
        } catch (Exception e) {
            client.sendEvent("sendDisAgreeFriendValidate", SocketR.error().event(SocketRConstant.EventCode.VALIDATE_MESSAGE).data("data", validateMessage));
        }
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 删除好友转发一下消息
     **/
    @OnEvent("sendDelGoodFriend")
    public void sendDelGoodFriend(SocketIOClient client, CurrentConversationVo conversationVo) {
        log.info("sendDelGoodFriend ---> conversationVo：{}", conversationVo);
        //转发一下消息
        //获取当前删除者的id
        //String uid = SocketIoServerMapUtil.getUid(client.getSessionId().toString());
        String uid = onlineUserService.getSimpleUserByClientId(client.getSessionId().toString()).getUid();
        //把会话id改为删除好友者，该会话的其他属性值不用管，这样传给被删除人的客户端时就显示为 被删除人也删除 删除人
        conversationVo.setId(uid);
        //实际上同一房间只有2个客户端
        Collection<SocketIOClient> clients = socketIOServer.getRoomOperations(conversationVo.getRoomId()).getClients();
        for (SocketIOClient item : clients) {
            if (item != client) {
                //通知被删好友去更新他的好友列表
                item.sendEvent("receiveDelGoodFriend", conversationVo);
            }
        }
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 同意进群
     **/
    @OnEvent("sendAgreeGroupValidate")
    public void sendAgreeGroupValidate(SocketIOClient client, ValidateMessageResponseVo validateMessage) {
        log.info("sendAgreeGroupValidate --------------> validateMessage：{}", validateMessage);
        //添加群成员
        groupUserService.addNewGroupUser(validateMessage);
        //改变群验证消息的状态为1
        validateMessageService.changeGroupValidateNewsStatus(validateMessage.getId(), 1);
        String roomId = validateMessage.getRoomId();
        String receiverId = validateMessage.getReceiverId();
        String senderId = validateMessage.getSenderId();
        //把接受者id替换为发送者id回到同意加好友的这方房间去更新我的群列表
        String senderRoomId = roomId.replaceAll(receiverId, senderId);
        //应该通知 请求加群的人 去更新他的群列表
        //这里应该换回系统通知，因为客户端房间里只加入了系统通知，新加的群号还没加入到房间呢
        //实际上同一房间只有2个客户端
        Collection<SocketIOClient> clients = socketIOServer.getRoomOperations(senderRoomId).getClients();
        for (SocketIOClient item : clients) {
            if (item != client) {
                //只通知发送者房间
                item.sendEvent("receiveAgreeGroupValidate", validateMessage);
            }
        }
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 拒绝进群
     **/
    @OnEvent("sendDisAgreeGroupValidate")
    public void sendDisAgreeGroupValidate(SocketIOClient client, ValidateMessageResponseVo validateMessage) {
        log.info("sendDisAgreeGroupValidate ---> validateMessage：{}", validateMessage);
        validateMessageService.changeFriendValidateNewsStatus(validateMessage.getId(), 2);
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 解散群或者退出群聊，则转发通知与这群关联的所有在线客户端 去更新我的群列表和最近会话中的群列表
     **/
    @OnEvent("sendQuitGroup")
    public void sendQuitGroup(SocketIOClient client, CurrentConversationVo conversationVo) {
        log.info("sendQuitGroup ---> conversationVo：{}", conversationVo);
        //实际上同一房间只有2个客户端
        Collection<SocketIOClient> clients = socketIOServer.getRoomOperations(conversationVo.getRoomId()).getClients();
        for (SocketIOClient item : clients) {
            if (item != client) {
                item.sendEvent("receiveQuitGroup", conversationVo);
            }
        }
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 转发申请
     **/
    @OnEvent("apply")
    public void apply(SocketIOClient client, CurrentConversationVo conversationVo) {
        log.info("apply ---> roomId：{}", conversationVo.getRoomId());
        System.out.println("apply user to，myNickname：" + conversationVo.getMyNickname());
        //实际上同一房间只有2个客户端
        Collection<SocketIOClient> clients = socketIOServer.getRoomOperations(conversationVo.getRoomId()).getClients();
        for (SocketIOClient item : clients) {
            if (item != client) {
                item.sendEvent("apply", conversationVo);
            }
        }
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 转发回复
     **/
    @OnEvent("reply")
    public void reply(SocketIOClient client, CurrentConversationVo conversationVo) {
        log.info("reply ---> roomId：{}", conversationVo.getRoomId());
        System.out.println("reply，myNickname：" + conversationVo.getMyNickname());
        //实际上同一房间只有2个客户端
        Collection<SocketIOClient> clients = socketIOServer.getRoomOperations(conversationVo.getRoomId()).getClients();
        for (SocketIOClient item : clients) {
            if (item != client) {
                item.sendEvent("reply", conversationVo);
            }
        }
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 转发 answer
     **/
    @OnEvent("1v1answer")
    public void answer(SocketIOClient client, CurrentConversationVo conversationVo) {
        log.info("1v1answer ---> roomId：{}", conversationVo.getRoomId());
        System.out.println("1v1answer，myNickname：" + conversationVo.getMyNickname());
        //实际上同一房间只有2个客户端
        Collection<SocketIOClient> clients = socketIOServer.getRoomOperations(conversationVo.getRoomId()).getClients();
        for (SocketIOClient item : clients) {
            if (item != client) {
                item.sendEvent("1v1answer", conversationVo);
            }
        }
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 转发 ICE，选取最佳的链接方式
     **/
    @OnEvent("1v1ICE")
    public void ICE(SocketIOClient client, CurrentConversationVo conversationVo) {
        log.info("1v1ICE -------------> roomId：{}", conversationVo.getRoomId());
        // System.out.println("1v1ICE，myNickname：" + conversationVo.getMyNickname());
        //实际上同一房间只有2个客户端
        Collection<SocketIOClient> clients = socketIOServer.getRoomOperations(conversationVo.getRoomId()).getClients();
        for (SocketIOClient item : clients) {
            if (item != client) {
                item.sendEvent("1v1ICE", conversationVo);
            }
        }
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 转发 Offer
     **/
    @OnEvent("1v1offer")
    public void offer(SocketIOClient client, CurrentConversationVo conversationVo) {
        log.info("1v1offer -------------> roomId：{}", conversationVo.getRoomId());
        System.out.println("1v1offer，myNickname：" + conversationVo.getMyNickname());
        Collection<SocketIOClient> clients = socketIOServer.getRoomOperations(conversationVo.getRoomId()).getClients();
        for (SocketIOClient item : clients) {
            if (item != client) {
                item.sendEvent("1v1offer", conversationVo);
            }
        }
    }
    
    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 转发 hangup
     **/
    @OnEvent("1v1hangup")
    public void hangup(SocketIOClient client, CurrentConversationVo conversationVo) {
        log.info("1v1hangup ---> roomId：{}", conversationVo.getRoomId());
        System.out.println("1v1hangup，myNickname：" + conversationVo.getMyNickname());
        Collection<SocketIOClient> clients = socketIOServer.getRoomOperations(conversationVo.getRoomId()).getClients();
        for (SocketIOClient item : clients) {
            if (item != client) {
                item.sendEvent("1v1hangup", conversationVo);
            }
        }
    }
    
}
