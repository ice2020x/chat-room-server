package com.ice.chatserver.service;

import com.ice.chatserver.dao.SuperUserDao;
import com.ice.chatserver.pojo.SuperUser;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.Map;

/**
* @author ice2020x
* @Date: 2021/12/18
* @Description: 系统用户处理逻辑的类
**/
public interface SuperUserService {

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 系统用户登录
    **/
    public Map<String, Object> superUserLogin(SuperUser superUser);

    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 检查有没有系统用户，如果没有添加一个
     **/
    public void notExistThenAddSuperUser(SuperUser superUser);


    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 添加系统用户
     **/
    public void addSuperUser(SuperUser superUser);

    /**
     * @author ice2020x
     * @Date: 2021/12/19
     * @Description: 根据系统用户名查找系统用户
     **/
    public SuperUser existSuperUser(String account);

}
