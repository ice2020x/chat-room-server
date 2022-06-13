package com.ice.chatserver.auth;

import com.ice.chatserver.common.R;
import com.ice.chatserver.common.ResultEnum;
import com.ice.chatserver.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @author ice2020x
* @Date: 2021/12/18
* @Description: 未认证时执行这个类,提示用户未登录
**/
public class UnAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        ResponseUtil.out(response, R.error().resultEnum(ResultEnum.USER_NEED_AUTHORITIES));
    }
}
