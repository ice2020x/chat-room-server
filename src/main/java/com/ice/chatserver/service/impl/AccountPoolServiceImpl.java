package com.ice.chatserver.service.impl;

import com.ice.chatserver.dao.AccountPoolDao;
import com.ice.chatserver.pojo.AccountPool;
import com.ice.chatserver.service.AccountPoolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AccountPoolServiceImpl implements AccountPoolService {
    @Resource
    private AccountPoolDao accountPoolDao;
    
    public void saveAccount(AccountPool accountPool) {
        accountPoolDao.save(accountPool);
    }
}