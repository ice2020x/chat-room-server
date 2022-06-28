package com.ice.chatserver.filter;

import com.ice.chatserver.common.GlobalException;
import com.ice.chatserver.common.ResultEnum;
import com.ice.chatserver.service.OnlineUserService;
import com.ice.chatserver.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

//BasicAuthenticationFilter继承于OncePerRequestFilter => 确保在一次请求只通过一次filter，而不需要重复执行。认证之后进行授权过滤
public class JwtPreAuthFilter extends BasicAuthenticationFilter {
    private static Logger logger = LoggerFactory.getLogger(JwtPreAuthFilter.class);
    
    private OnlineUserService onlineUserService;
    
    public JwtPreAuthFilter(AuthenticationManager authenticationManager, OnlineUserService onlineUserService) {
        super(authenticationManager);
        this.onlineUserService = onlineUserService;
    }
    
    //读取Token
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String tokenHeader = request.getHeader(JwtUtils.TOKEN_HEADER);
        System.out.println("tokenHeader：" + tokenHeader);
        // 若请求头中没有Authorization信息则直接放行
        if (tokenHeader == null /*|| !tokenHeader.startsWith(JwtUtils.TOKEN_PREFIX)*/) {
            chain.doFilter(request, response);
            return;
        }
        // 若请求头中有token，则进行解析，且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        super.doFilterInternal(request, response, chain);
    }
    
    //读取Token信息，创建UsernamePasswordAuthenticationToken对象
    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            //解析token
            Claims claims = JwtUtils.parseJwt(token);
            System.out.println("解析得到的token信息为：" + claims);
            //用户唯一标识id，这里跟创建token时有关
            String userId = claims.getSubject();
            if (userId != null) {
                return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
            }
        } catch (ExpiredJwtException e) {
            logger.error("Token过期了，获取到的过期凭证为：{}", e.getClaims());
            //若token过期了，则清除 uid->user 表对应的用户信息，以防下次登录时还显示用户在线
            String userId = e.getClaims().getSubject();
            //SocketIoServerMapUtil.removeUser(userId);
            //移除在线用户列表中登录过期凭证的用户id
            onlineUserService.removeClientAndUidInSet("", userId);
            throw new GlobalException(ResultEnum.TOKEN_VALIDATION_EXPIRED);
        } catch (Exception e) {
            logger.error("非法的token：", e);
            throw new GlobalException(ResultEnum.ILLEGAL_LOGIN);
        }
        return null;
    }
}
