package com.ice.chatserver.dao;

import com.ice.chatserver.pojo.GroupMessage;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupMessageDao extends MongoRepository<GroupMessage, ObjectId> {

}