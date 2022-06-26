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

/**
 * @author ice2020x
 * @date 2021-12-19 0:21
 * @description:
 */
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
//            如果有群组消息的话
            if (son.getGroupId() != null && son.getGroupList() != null && son.getGroupList().size() > 0) {
                item.setGroupInfo(new SimpleGroup());
                item.getGroupInfo().setGid(son.getGroupList().get(0).getGroupId().toString());
                item.getGroupInfo().setTitle(son.getGroupList().get(0).getTitle());
            }
            responseVoList.add(item);
        }
        return responseVoList;
    }

    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 根据id查询一条记录
     **/
    @Override
    public ValidateMessage findValidateMessage(String roomId, Integer status, Integer validateType) {
        return validateMessageDao.findValidateMessageByRoomIdAndStatusAndValidateType(roomId, status, validateType);
    }

    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 添加一个验证消息
     **/
    @Override
    public ValidateMessage addValidateMessage(ValidateMessage validateMessage) {
        //查出未处理状态
        // 这里逻辑错误 进行先验证再保存
//        validateMessageDao.save(validateMessage);
        ValidateMessage res = findValidateMessage(validateMessage.getRoomId(), 0, validateMessage.getValidateType());
        System.out.println("查到的验证消息为：" + res);
        // 下面的逻辑就是 要么添加 要么更新  有id就是更新 应该是这样的
        if (res != null) {
            // 已存在的情况进行更新 设置了id 应该就是更新了
            validateMessage.setId(res.getId());
        }
        return validateMessageDao.save(validateMessage);
    }

    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 更新状态
     **/
    @Override
    public void changeFriendValidateNewsStatus(String validateMessageId, Integer status) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(validateMessageId)));
        Update update = new Update();
        update.set("status", status);
        UpdateResult result = mongoTemplate.upsert(query, update, "validatemessages");
        // System.out.println("是否更新成功？" + result);
    }

    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 为什么写两个方法，单纯不想多加一个参数，代码几乎一模一样
     **/
    @Override
    public void changeGroupValidateNewsStatus(String validateMessageId, Integer status) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(validateMessageId)));
        Update update = new Update();
        update.set("status", status);
        UpdateResult result = mongoTemplate.upsert(query, update, "validatemessages");
        // System.out.println("是否更新成功？" + result);
    }

}
