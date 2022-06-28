package com.ice.chatserver.service;

import com.ice.chatserver.common.R;
import com.ice.chatserver.pojo.User;
import com.ice.chatserver.pojo.vo.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserService {
    
    R register(RegisterRequestVo rVo);
    
    R getVerificationCode(HttpServletRequest request, HttpServletResponse response);
    
    public R getUserInfo(String userId, HttpServletRequest request);
    
    R addNewFenZu(NewFenZuRequestVo requestVo);
    
    R modifyFriendBeiZhu(ModifyFriendBeiZhuRequestVo requestVo, String userId);
    
    public R modifyFriendFenZu(ModifyFriendFenZuRequestVo requestVo);
    
    R deleteFenZu(DelFenZuRequestVo requestVo);
    
    R editFenZu(EditFenZuRequestVo requestVo);
    
    void updateOnlineTime(long onlineTime, String uid);
    
    boolean updateUserConfigure(UpdateUserConfigureRequestVo requestVo, String userId);
    
    Map<String, Object> updateUserInfo(UpdateUserInfoRequestVo requestVo);
    
    public Map<String, Object> updateUserPwd(UpdateUserPwdRequestVo requestVo);
    
    public List<User> getUserList();
    
    public List<User> getUsersBySignUpTime(String lt, String rt);
    
    public void changeUserStatus(String uid, Integer status);
    
    HashMap<String, Object> searchUser(SearchRequestVo requestVo, String userId);
}
