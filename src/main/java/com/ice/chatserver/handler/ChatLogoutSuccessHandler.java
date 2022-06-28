package com.ice.chatserver.handler;

import com.alibaba.fastjson.JSON;
import com.ice.chatserver.common.R;
import com.ice.chatserver.common.ResultEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//退出登录
public class ChatLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.getWriter().write(JSON.toJSONString(R.ok().resultEnum(ResultEnum.USER_LOGOUT_SUCCESS)));
    }
}
