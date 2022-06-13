package com.ice.chatserver.service;

import com.ice.chatserver.common.R;
import com.ice.chatserver.pojo.User;
import com.ice.chatserver.pojo.vo.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ice2020x
 * @Description: 用户逻辑处理类
 */
public interface UserService {

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 用户注册
    **/
    R register(RegisterRequestVo rVo);

    /**
     * @Description: 获取验证码
     * @Author: ice2020x
     * @Date: 2021/11/11
     */
    R getVerificationCode(HttpServletRequest request, HttpServletResponse response);


    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 根据用户id获取用户详细信息
    **/
    public R getUserInfo(String userId);

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 添加新的分组
    **/
    R addNewFenZu(NewFenZuRequestVo requestVo);

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 修改备注信息
    **/
    R modifyFriendBeiZhu(ModifyFriendBeiZhuRequestVo requestVo);

    /**
     * @author ice2020x
     * @Date: 2021/12/18
     * @Description: 修改好友分组
     **/
    public R modifyFriendFenZu(ModifyFriendFenZuRequestVo requestVo);

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 删除分组
    **/
    R deleteFenZu(DelFenZuRequestVo requestVo);

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 更新分组名
    **/
    R editFenZu(EditFenZuRequestVo requestVo);



    void updateOnlineTime(long onlineTime, String uid);

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 更新用户的配置
    **/
    boolean updateUserConfigure(UpdateUserConfigureRequestVo requestVo, String userId);


    Map<String, Object> updateUserInfo(UpdateUserInfoRequestVo requestVo);

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 更新密码
    **/
    public Map<String, Object> updateUserPwd(UpdateUserPwdRequestVo requestVo);


    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 获取所有用户
    **/
    public List<User> getUserList();

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 根据注册时间获取用户
    **/
    public List<User> getUsersBySignUpTime(String lt, String rt);
    
    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 修改用户状态
    **/
    public void changeUserStatus(String uid, Integer status);

    /**
    * @author ice2020x
    * @Date: 2021/12/20
    * @Description: 客户端搜索
    *
     * @return*/
    HashMap<String, Object> searchUser(SearchRequestVo requestVo, String userId);
}
