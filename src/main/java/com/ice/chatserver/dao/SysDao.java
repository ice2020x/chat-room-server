package com.ice.chatserver.dao;

import com.ice.chatserver.pojo.SystemUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysDao extends MongoRepository<SystemUser, ObjectId> {
}