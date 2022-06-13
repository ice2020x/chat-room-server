package com.ice.chatserver.service.Impl;

import com.ice.chatserver.common.ResultEnum;
import com.ice.chatserver.dao.SuperUserDao;
import com.ice.chatserver.pojo.SuperUser;
import com.ice.chatserver.service.SuperUserService;
import com.ice.chatserver.utils.JwtUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ice2020x
 * @date 2021-12-18 21:52
 * @description: 管理员处理逻辑的类实现类
 */
@Service
public class SuperUserServiceImpl implements SuperUserService {

    @Resource
    private SuperUserDao superUserDao;

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 检查有没有系统用户，如果没有添加一个
    **/
    @Override
    public void notExistThenAddSuperUser(SuperUser superUser) {
        SuperUser existSuperUser = existSuperUser(superUser.getAccount());
        if (existSuperUser == null) {
            addSuperUser(superUser);
        }
    }

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 添加系统用户
    **/
    @Override
    public void addSuperUser(SuperUser superUser) {
        //对密码进行加密
        superUser.setPassword(bCryptPasswordEncoder.encode(superUser.getPassword()));
        superUserDao.save(superUser);
    }

    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 根据系统用户名查找系统用户
    **/
    @Override
    public SuperUser existSuperUser(String account) {
        Query query = new Query();
        query.addCriteria(Criteria.where("account").is(account));
        return mongoTemplate.findOne(query, SuperUser.class);
    }


    /**
    * @author ice2020x
    * @Date: 2021/12/19
    * @Description: 系统用户登录
    **/
    @Override
    public Map<String, Object> superUserLogin(SuperUser superUser) {
        Map<String, Object> map = new HashMap<>();
        Integer code = null;
        String msg = null;
        SuperUser user = existSuperUser(superUser.getAccount());
        if (user == null) {
            msg = ResultEnum.ACCOUNT_NOT_FOUND.getMessage();
            code = ResultEnum.ACCOUNT_NOT_FOUND.getCode();
        } else {
            //使用matches方法（参数1：不经过加密的密码，参数2：已加密密码）
            if (!bCryptPasswordEncoder.matches(superUser.getPassword(), user.getPassword())) {
                code = ResultEnum.OLD_PASSWORD_ERROR.getCode();
                msg = ResultEnum.OLD_PASSWORD_ERROR.getMessage();
            } else {
                // 登录成功，不仅要创建token，而且要将其和用户信息一起返回
                code = ResultEnum.LOGIN_SUCCESS.getCode();
                msg = ResultEnum.LOGIN_SUCCESS.getMessage();
                String jwt = JwtUtils.createJwt(user.getSid().toString(), user.getAccount());
                map.put("userInfo", user);
                map.put("token", jwt);
            }
        }
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }
}
