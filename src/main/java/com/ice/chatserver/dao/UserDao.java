package com.ice.chatserver.dao;

import com.ice.chatserver.pojo.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends MongoRepository<User, ObjectId> {
    User findUserByUsernameOrCode(String username, String code);
    User findUserByUsername(String username);
}