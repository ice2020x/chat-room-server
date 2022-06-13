package com.ice.chatserver.service;


import com.ice.chatserver.common.R;

import javax.servlet.http.HttpServletRequest;


public interface MsmService {
    /**
    * @author ice2020x
    * @Date: 2021/12/21
    * @Description: 发送短信
    **/
    R sendMsg(HttpServletRequest request, String phone);
}
