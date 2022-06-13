package com.ice.chatserver.dao;

import com.ice.chatserver.pojo.Group;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupDao extends MongoRepository<Group, ObjectId> {
}
