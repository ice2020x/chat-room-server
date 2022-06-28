package com.ice.chatserver.service;

import com.ice.chatserver.pojo.SuperUser;

import java.util.Map;

public interface SuperUserService {
    
    public Map<String, Object> superUserLogin(SuperUser superUser);
    
    public void notExistThenAddSuperUser(SuperUser superUser);
    
    public void addSuperUser(SuperUser superUser);
    
    public SuperUser existSuperUser(String account);
}