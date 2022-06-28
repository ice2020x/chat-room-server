package com.ice.chatserver.service.impl;

import com.ice.chatserver.dao.ValidateMessageDao;
import com.ice.chatserver.pojo.ValidateMessage;
import com.ice.chatserver.pojo.vo.SimpleGroup;
import com.ice.chatserver.pojo.vo.ValidateMessageResponseVo;
import com.ice.chatserver.pojo.vo.ValidateMessageResultVo;
import com.ice.chatserver.service.ValidateMessageService;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

//验证信息逻辑
@Service
public class ValidateMessageServiceImpl implements ValidateMessageService {
    @Resource
    private ValidateMessageDao validateMessageDao;
    
    @Resource
    private MongoTemplate mongoTemplate;
    
    @Override
    public List<ValidateMessageResponseVo> getMyValidateMessageList(String userId, Integer status, Integer validateType) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.lookup(
                        "groups",
                        "groupId",
                        "_id",
                        "groupList"
                ), Aggregation.match(Criteria.where("receiverId").is(new ObjectId(userId)))
        );
        List<ValidateMessageResultVo> validatemessages = mongoTemplate.aggregate(aggregation, "validatemessages", ValidateMessageResultVo.class).getMappedResults();
        System.out.println("查询我的验证消息列表结果为：" + validatemessages);
        List<ValidateMessageResponseVo> responseVoList = new ArrayList<>();
        ValidateMessageResponseVo item;
        for (ValidateMessageResultVo son : validatemessages) {
            item = new ValidateMessageResponseVo();
            BeanUtils.copyProperties(son, item);
            //如果有群组消息的话
            if (son.getGroupId() != null && son.getGroupList() != null && son.getGroupList().size() > 0) {
                item.setGroupInfo(new SimpleGroup());
                item.getGroupInfo().setGid(son.getGroupList().get(0).getGroupId().toString());
                item.getGroupInfo().setTitle(son.getGroupList().get(0).getTitle());
            }
            responseVoList.add(item);
        }
        return responseVoList;
    }
    
    //根据验证信息id查询一条记录
    @Override
    public ValidateMessage findValidateMessage(String roomId, Integer status, Integer validateType) {
        return validateMessageDao.findValidateMessageByRoomIdAndStatusAndValidateType(roomId, status, validateType);
    }
    
    //添加验证消息
    @Override
    public ValidateMessage addValidateMessage(ValidateMessage validateMessage) {
        //validateMessageDao.save(validateMessage);
        ValidateMessage res = findValidateMessage(validateMessage.getRoomId(), 0, validateMessage.getValidateType());
        System.out.println("查到的验证消息为：" + res);
        if (res != null) {
            validateMessage.setId(res.getId());
        }
        return validateMessageDao.save(validateMessage);
    }
    
    //更新好友验证消息状态
    @Override
    public void changeFriendValidateNewsStatus(String validateMessageId, Integer status) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(validateMessageId)));
        Update update = new Update();
        update.set("status", status);
        UpdateResult result = mongoTemplate.upsert(query, update, "validatemessages");
    }
    
    //更新群聊验证消息状态
    @Override
    public void changeGroupValidateNewsStatus(String validateMessageId, Integer status) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(validateMessageId)));
        Update update = new Update();
        update.set("status", status);
        UpdateResult result = mongoTemplate.upsert(query, update, "validatemessages");
    }
}
