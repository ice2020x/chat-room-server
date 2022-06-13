package com.ice.chatserver.dao;

import com.ice.chatserver.pojo.GoodFriend;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface GoodFriendDao extends MongoRepository<GoodFriend, ObjectId> {
}

