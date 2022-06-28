package com.ice.chatserver.service.impl;

import com.ice.chatserver.dao.SysDao;
import com.ice.chatserver.pojo.FeedBack;
import com.ice.chatserver.pojo.SensitiveMessage;
import com.ice.chatserver.pojo.SystemUser;
import com.ice.chatserver.pojo.vo.FeedBackResultVo;
import com.ice.chatserver.pojo.vo.SensitiveMessageResultVo;
import com.ice.chatserver.pojo.vo.SystemUserResponseVo;
import com.ice.chatserver.service.SystemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

//系统逻辑实现类
@Service
public class SystemServiceImpl implements SystemService {
    
    @Resource
    private MongoTemplate mongoTemplate;
    
    @Autowired
    SysDao sysDao;
    
    //项目一启动自动检查
    @Override
    public void notExistThenAddSystemUser(SystemUser user) {
        Query query = new Query();
        query.addCriteria(Criteria.where("code").is(user.getCode()));
        SystemUser one = mongoTemplate.findOne(query, SystemUser.class);
        if (one == null) {
            sysDao.save(user);
        }
    }
    
    //获取系统用户列表
    @Override
    public List<SystemUserResponseVo> getSysUsers() {
        List<SystemUser> systemUsers = sysDao.findAll();
        List<SystemUserResponseVo> res = new ArrayList<>();
        SystemUserResponseVo item;
        for (SystemUser son : systemUsers) {
            item = new SystemUserResponseVo();
            BeanUtils.copyProperties(son, item);
            item.setSid(son.getId().toString());
            res.add(item);
        }
        return res;
    }
    
    //新增反馈
    @Override
    public void addFeedBack(FeedBack feedBack) {
        mongoTemplate.insert(feedBack, "feedbacks");
    }
    
    //添加敏感消息
    @Override
    public void addSensitiveMessage(SensitiveMessage sensitiveMessage) {
        mongoTemplate.insert(sensitiveMessage, "sensitivemessages");
    }
    
    //获取敏感消息列表
    @Override
    public List<SensitiveMessageResultVo> getSensitiveMessageList() {
        return mongoTemplate.findAll(SensitiveMessageResultVo.class, "sensitivemessages");
    }
    
    //获取反馈列表
    @Override
    public List<FeedBackResultVo> getFeedbackList() {
        return mongoTemplate.findAll(FeedBackResultVo.class, "feedbacks");
    }
}
