package com.ice.chatserver.service;

import com.ice.chatserver.pojo.vo.SimpleUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author ice2020x
 * @Description: 做一个信息的维护，string 数据结构维护一个 clientId -> user 用 set 维护所有的 uid，封装redis操作
 */
@Service
public interface OnlineUserService {


    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 用户一但登录，client关联唯一user 添加入用户列表
    **/
    public void addClientIdToSimpleUser(String clientId, SimpleUser simpleUser);

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 获取用户在线人数set列表
    **/
    public Set<Object> getOnlineUidSet();

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 获取客户id对应的user
    **/
    public SimpleUser getSimpleUserByClientId(String clientId);

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 移除uid与设备的关联
    **/
    public void removeClientAndUidInSet(String clientId, String uid);

    /**
     * @author ice2020x
     * @Date: 2021/12/18
     * @Description: 获取用户在线列表的个数
     * @result: 在线列表的人数
     **/
    public int countOnlineUser();

    /**
     * @author ice2020x
     * @Date: 2021/12/18
     * @Param: 用户的userid
     * @return: 用户是否在线
     * @Description: 检查用户是否在线
     **/
    public boolean checkCurUserIsOnline(String uid);
}