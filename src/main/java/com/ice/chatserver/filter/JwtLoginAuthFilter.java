package com.ice.chatserver.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.ice.chatserver.auth.JwtAuthUser;
import com.ice.chatserver.common.R;
import com.ice.chatserver.common.ResultEnum;
import com.ice.chatserver.pojo.User;
import com.ice.chatserver.pojo.vo.LoginRequestVo;
import com.ice.chatserver.service.OnlineUserService;
import com.ice.chatserver.utils.JwtUtils;
import com.ice.chatserver.utils.ResponseUtil;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JwtLoginAuthFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    private MongoTemplate mongoTemplate;

    private OnlineUserService onlineUserService;

    public JwtLoginAuthFilter(AuthenticationManager authenticationManager, MongoTemplate mongoTemplate, OnlineUserService onlineUserService) {
        this.authenticationManager = authenticationManager;
        this.mongoTemplate = mongoTemplate;
        this.onlineUserService = onlineUserService;
        this.setFilterProcessesUrl("/user/login");
    }

    //登录验证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestVo lvo = new ObjectMapper().readValue(request.getInputStream(), LoginRequestVo.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(lvo.getUsername(), lvo.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    //登录验证成功后调用，验证成功后将生成Token，并重定向到用户主页home
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        try {
            //这里可以再次使用request
            LoginRequestVo lvo = new ObjectMapper().readValue(request.getInputStream(), LoginRequestVo.class);
             System.out.println("验证成功后========================================登录请求参数为：" + lvo);
            //查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象，这里是JwtAuthUser
            JwtAuthUser jwtUser = (JwtAuthUser) authResult.getPrincipal();
             System.out.println("JwtAuthUser：" + jwtUser.toString());

            if (jwtUser.getStatus() == 1 || jwtUser.getStatus() == 2) {
                ResponseUtil.out(response, R.error().resultEnum(ResultEnum.ACCOUNT_IS_FROZEN_OR_CANCELLED));
            }
            /*else if (SocketIoServerMapUtil.getUidToUserMap().containsKey(jwtUser.getUserId().toString())) //用户已经在别处登录了
                ResponseUtil.out(response, R.error().resultEnum(ResultEnum.USER_HAS_LOGGED));*/
            else if (onlineUserService.checkCurUserIsOnline(jwtUser.getUserId().toString())) //用户已经在别处登录了
            {
                ResponseUtil.out(response, R.error().resultEnum(ResultEnum.USER_HAS_LOGGED));
            } else { //用户通过验证
                Query query = new Query();
                query.addCriteria(new Criteria().orOperator(Criteria.where("username").is(jwtUser.getUsername()),
                        Criteria.where("code").is(jwtUser.getUsername())));
                Update update = new Update();
                update.set("lastLoginTime", new Date());
                //设置一下uid
                update.set("uid", jwtUser.getUserId().toString());
                // System.out.println("当前登录用户的uid为：" + jwtUser.getUserId().toString());
                jwtUser.setLastLogin(new Date());
                jwtUser.setUid(jwtUser.getUserId().toString());
                UpdateResult updateResult = mongoTemplate.upsert(query, update, User.class);
                // System.out.println("更新用户表是否成功？" + updateResult);
                //生成token
                String token = JwtUtils.createJwt(jwtUser.getUserId().toString(), jwtUser.getUsername());
                ResponseUtil.out(response, R.ok().resultEnum(ResultEnum.LOGIN_SUCCESS).data("token", token).data("userInfo", jwtUser));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //登录验证失败后调用，这里直接Json返回，实际上可以重定向到错误界面等
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        ResponseUtil.out(response, R.error().resultEnum(ResultEnum.USER_LOGIN_FAILED));
    }
}
