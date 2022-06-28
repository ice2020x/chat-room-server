package com.ice.chatserver.service.impl;

import com.ice.chatserver.common.ConstValueEnum;
import com.ice.chatserver.dao.AccountPoolDao;
import com.ice.chatserver.dao.GroupDao;
import com.ice.chatserver.dao.GroupUserDao;
import com.ice.chatserver.pojo.AccountPool;
import com.ice.chatserver.pojo.Group;
import com.ice.chatserver.pojo.GroupUser;
import com.ice.chatserver.pojo.vo.*;
import com.ice.chatserver.service.GroupService;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupDao groupDao;
    
    
    @Resource
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private AccountPoolDao accountPoolDao;
    
    @Autowired
    private GroupUserDao groupUserDao;
    
    //获取群聊信息
    @Override
    public Group getGroupInfo(String groupId) {
        Optional<Group> res = groupDao.findById(new ObjectId(groupId));
        return res.orElse(null);
    }
    
    //搜索群聊，分页加模糊查询
    @Override
    public List<SearchGroupResponseVo> searchGroup(SearchRequestVo requestVo, String uid) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.lookup(
                        "users",
                        "holderUserId",
                        "_id",
                        "holderUsers"
                ), Aggregation.match(
                        Criteria.where(requestVo.getType()).regex(Pattern.compile("^.*" + requestVo.getSearchContent() + ".*$", Pattern.CASE_INSENSITIVE))
                ), Aggregation.skip((long) (requestVo.getPageIndex() - 1) * requestVo.getPageSize()),
                Aggregation.limit((long) requestVo.getPageSize()),
                Aggregation.sort(Sort.Direction.DESC, "_id")
        );
        List<SearchGroupResultVo> results = mongoTemplate.aggregate(aggregation, "groups", SearchGroupResultVo.class).getMappedResults();
        List<SearchGroupResponseVo> groups = new ArrayList<>();
        SearchGroupResponseVo item;
        for (SearchGroupResultVo son : results) {
            //群主账号不为当前登录的账号，就可以搜索得到
            if (!son.getHolderUsers().get(0).getUid().equals(uid)) {
                item = new SearchGroupResponseVo();
                BeanUtils.copyProperties(son, item);
                item.setGid(son.getId());
                BeanUtils.copyProperties(son.getHolderUsers().get(0), item.getHolderUserInfo());
                groups.add(item);
            }
        }
        return groups;
    }
    
    //添加群聊
    @Override
    public String createGroup(CreateGroupRequestVo requestVo) {
        AccountPool accountPool = new AccountPool();
        //群聊账号
        accountPool.setType(2);
        //已使用，删除或注销都要设置为未使用
        accountPool.setStatus(1);
        accountPoolDao.save(accountPool);
        Group group = new Group();
        if (requestVo.getTitle() != null) {
            group.setTitle(requestVo.getTitle());
        }
        if (requestVo.getDesc() != null) {
            group.setDesc(requestVo.getDesc());
        }
        if (requestVo.getImg() != null) {
            group.setImg(requestVo.getImg());
        }
        group.setHolderName(requestVo.getHolderName());
        group.setHolderUserId(new ObjectId(requestVo.getHolderUserId()));
        //设置生成的code+偏移量
        group.setCode(String.valueOf(accountPool.getCode() + ConstValueEnum.INITIAL_NUMBER));
        groupDao.save(group);
        //  建立群主消息
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group.getGroupId());
        groupUser.setUserId(group.getHolderUserId());
        groupUser.setUsername(group.getHolderName());
        groupUser.setHolder(1);
        groupUserDao.save(groupUser);
        // 更新groups中gid的字段
        Update update = new Update();
        update.set("gid", group.getGroupId().toString());
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(group.getGroupId()));
        mongoTemplate.upsert(query, update, Group.class);
        return group.getCode();
    }
    
    //获取群聊列表
    @Override
    public List<SearchGroupResultVo> getAllGroup() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.lookup(
                        "users",
                        "holderUserId",
                        "_id",
                        "holderUsers"
                )
        );
        AggregationResults<SearchGroupResultVo> groups = mongoTemplate.aggregate(aggregation, "groups", SearchGroupResultVo.class);
        return groups.getMappedResults();
    }
    
    //退出群聊
    @Override
    @Transactional
    public void quitGroup(QuitGroupRequestVo requestVo) {
        //根据是否为群主进行分类
        // 是群主
        if (requestVo.getHolder() == 1) {
            //1、先删除该群中所有信息：(groupmessages)
            delGroupAllMessagesByGroupId(requestVo.getGroupId());
            //2、再删除该群的所有成员：（groupusers）
            delGroupAllUsersByGroupId(requestVo.getGroupId());
            //3、最后删除群账号：（groups）
            groupDao.deleteById(new ObjectId(requestVo.getGroupId()));
            // 不是群主
        }
        else {
            //1、先删除与当前用户发送的所有群信息
            delGroupMessagesByGroupIdAndSenderId(requestVo.getGroupId(), requestVo.getUserId());
            //2、再删除在该群的当前用户
            delGroupUserByGroupIdAndUserId(requestVo.getGroupId(), requestVo.getUserId());
            //3、群成员个数减1
            decrGroupUserNum(requestVo.getGroupId());
        }
    }
    
    //删除群聊所有聊天记录
    private void delGroupAllMessagesByGroupId(String groupId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(groupId));
        DeleteResult groupmessages = mongoTemplate.remove(query, "groupmessages");
        // System.out.println("删除该群所有消息是否成功？" + groupmessages.getDeletedCount());
    }
    
    //删除群聊的所有成员
    private void delGroupAllUsersByGroupId(String groupId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("groupId").is(new ObjectId(groupId)));
        DeleteResult groupusers = mongoTemplate.remove(query, "groupusers");
        System.out.println("删除该群所有成员是否成功？" + groupusers.getDeletedCount());
    }
    
    //根据群聊ID和发送者uid删除用户所发的群聊消息
    private void delGroupMessagesByGroupIdAndSenderId(String groupId, String senderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(groupId).and("senderId").is(new ObjectId(senderId)));
        DeleteResult groupmessages = mongoTemplate.remove(query, "groupmessages");
        System.out.println("删除该用户所发的群消息是否成功？" + groupmessages.getDeletedCount());
    }
    
    //删除群成员
    private void delGroupUserByGroupIdAndUserId(String groupId, String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("groupId").is(new ObjectId(groupId)).and("userId").is(new ObjectId(userId)));
        DeleteResult groupusers = mongoTemplate.remove(query, "groupusers");
        System.out.println("删除该群成员是否成功？" + groupusers.getDeletedCount());
    }
    
    //使群聊人数减一
    private void decrGroupUserNum(String gid) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(gid)));
        Update update = new Update();
        //该群人数减去1
        update.inc("userNum", -1);
        UpdateResult groups = mongoTemplate.upsert(query, update, "groups");
        System.out.println("该群人数递减1是否成功？" + groups.getModifiedCount());
    }
}