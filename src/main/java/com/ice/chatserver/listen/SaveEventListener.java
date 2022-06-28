package com.ice.chatserver.listen;

import com.ice.chatserver.annon.AutoIncKey;
import com.ice.chatserver.pojo.AccountPool;
import com.ice.chatserver.pojo.SeqInfo;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;

//监听设置某个集合的主键值加1 处理那个注解
@Component
public class SaveEventListener extends AbstractMongoEventListener<AccountPool> {
    @Resource
    private MongoTemplate mongoTemplate;
    
    @Override
    public void onBeforeConvert(BeforeConvertEvent<AccountPool> event) {
        final Object source = event.getSource();
        ReflectionUtils.doWithFields(source.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);
            //判断字段是否被自定义注解标识
            if (field.isAnnotationPresent(AutoIncKey.class)) {
                //设置id
                field.set(source, getNextId(source.getClass().getSimpleName()));
            }
        });
    }
    
    private Long getNextId(String collName) {
        Query query = new Query(Criteria.where("collName").is(collName));
        Update update = new Update();
        update.inc("seqId", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        SeqInfo seq = mongoTemplate.findAndModify(query, update, options, SeqInfo.class);
        assert seq != null;
        return seq.getSeqId();
    }
}
