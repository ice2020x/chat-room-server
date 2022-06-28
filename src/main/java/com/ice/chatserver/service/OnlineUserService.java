package com.ice.chatserver.service;

import com.ice.chatserver.pojo.vo.SimpleUser;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface OnlineUserService {
    
    public void addClientIdToSimpleUser(String clientId, SimpleUser simpleUser);
    
    public Set<Object> getOnlineUidSet();
    
    public SimpleUser getSimpleUserByClientId(String clientId);
    
    public void removeClientAndUidInSet(String clientId, String uid);
    
    public int countOnlineUser();
    
    public boolean checkCurUserIsOnline(String uid);
}