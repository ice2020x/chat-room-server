package com.ice.chatserver.service.Impl;

import com.ice.chatserver.dao.AccountPoolDao;
import com.ice.chatserver.pojo.AccountPool;
import com.ice.chatserver.service.AccountPoolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ice2020x
 * @date 2021-12-19 11:26
 * @description:
 */
@Service
public class AccountPoolServiceImpl implements AccountPoolService {
    @Resource
    private AccountPoolDao accountPoolDao;

    public void saveAccount(AccountPool accountPool) {
        accountPoolDao.save(accountPool);
    }
}
