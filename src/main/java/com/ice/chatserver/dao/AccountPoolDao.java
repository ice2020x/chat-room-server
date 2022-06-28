package com.ice.chatserver.dao;

import com.ice.chatserver.pojo.AccountPool;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountPoolDao extends MongoRepository<AccountPool, String> {

}
