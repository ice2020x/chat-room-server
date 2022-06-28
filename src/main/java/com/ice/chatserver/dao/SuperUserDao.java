package com.ice.chatserver.dao;

import com.ice.chatserver.pojo.SuperUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SuperUserDao extends MongoRepository<SuperUser, ObjectId> {

}