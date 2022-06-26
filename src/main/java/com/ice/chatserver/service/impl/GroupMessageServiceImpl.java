package com.ice.chatserver.service.impl;

import com.ice.chatserver.dao.GroupMessageDao;
import com.ice.chatserver.pojo.GroupMessage;
import com.ice.chatserver.pojo.vo.GroupHistoryResultVo;
import com.ice.chatserver.pojo.vo.GroupMessageResultVo;
import com.ice.chatserver.pojo.vo.HistoryMsgRequestVo;
import com.ice.chatserver.service.GroupMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author ice2020x
 * @date 2021-12-19 14:32
 * @description: 群消息的逻辑实现类
 */
@Service
public class GroupMessageServiceImpl implements GroupMessageService {

    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    GroupMessageDao groupMessageDao;

    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 获取最近的群消息
     **/
    @Override
    public List<GroupMessageResultVo> getRecentGroupMessages(String roomId, Integer pageIndex, Integer pageSize) {
        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(roomId))
                .with(Sort.by(Sort.Direction.DESC, "_id"))
                .skip(pageIndex * pageSize)
                .limit(pageSize);
        return mongoTemplate.find(query, GroupMessageResultVo.class, "groupmessages");
    }


    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 获取群历史消息
     **/
    @Override
    public GroupHistoryResultVo getGroupHistoryMessages(HistoryMsgRequestVo groupHistoryVo) {
        // 创建复合查询对象
        System.out.println("groupHistoryVo:"+groupHistoryVo);
        Criteria cri1 = new Criteria();
        cri1.and("roomId").is(groupHistoryVo.getRoomId());
        //若查询条件是全部，则模糊匹配 message 或者 fileRawName
        //若查询条件不是全部，则设置搜索类型，并且模糊匹配 fileRawName
        Criteria cri2 = null;
        if (!"all".equals(groupHistoryVo.getType())) {
            //若查询类型是文件或图片，则模糊匹配原文件名
            cri1.and("messageType").is(groupHistoryVo.getType())
                    .and("fileRawName").regex(Pattern.compile("^.*" + groupHistoryVo.getQuery() + ".*$", Pattern.CASE_INSENSITIVE));
        } else {
            cri2 = new Criteria().orOperator(Criteria.where("message").regex(Pattern.compile("^.*" + groupHistoryVo.getQuery() + ".*$", Pattern.CASE_INSENSITIVE)),
                    Criteria.where("fileRawName").regex(Pattern.compile("^.*" + groupHistoryVo.getQuery() + ".*$", Pattern.CASE_INSENSITIVE)));
        }
        if (groupHistoryVo.getDate() != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(groupHistoryVo.getDate());
            calendar.add(Calendar.DATE, 1);
            Date tomorrow = calendar.getTime();
            System.out.println("today：" + groupHistoryVo.getDate() + ", tomorrow：" + tomorrow);
            cri1.and("time").gte(groupHistoryVo.getDate()).lt(tomorrow);
        }
        // 创建查询对象
        Query query = new Query();
        if (cri2 != null) {
            //最后两者是且的关系
            query.addCriteria(new Criteria().andOperator(cri1, cri2));
        } else {
            query.addCriteria(cri1);
        }
        // 统计总数
        long count = mongoTemplate.count(query, GroupMessageResultVo.class, "groupmessages");
        // 设置分页
        //页码
        query.skip(groupHistoryVo.getPageIndex() * groupHistoryVo.getPageSize());
        //每页显示数量
        query.limit(groupHistoryVo.getPageSize());
        List<GroupMessageResultVo> messageList = mongoTemplate.find(query, GroupMessageResultVo.class, "groupmessages");
        return new GroupHistoryResultVo(messageList, count);
    }



    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 获取群的最后以一消息
    **/
    @Override
    public GroupMessageResultVo getGroupLastMessage(String roomId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(roomId))
                .with(Sort.by(Sort.Direction.DESC, "_id"));
        GroupMessageResultVo res = mongoTemplate.findOne(query, GroupMessageResultVo.class, "groupmessages");
        if (res == null) {
            res = new GroupMessageResultVo();
        }
        return res;
    }

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 添加一条群消息
    **/
    @Override
    public void addNewGroupMessage(GroupMessage groupMessage) {
        groupMessageDao.save(groupMessage);
    }

}
