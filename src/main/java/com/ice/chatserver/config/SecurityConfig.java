package com.ice.chatserver.config;

import com.ice.chatserver.auth.UnAuthEntryPoint;
import com.ice.chatserver.filter.JwtLoginAuthFilter;
import com.ice.chatserver.filter.JwtPreAuthFilter;
import com.ice.chatserver.filter.VerificationFilter;
import com.ice.chatserver.handler.ChatLogoutSuccessHandler;
import com.ice.chatserver.service.OnlineUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author ice2020x
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    
    @Resource
    private MongoTemplate mongoTemplate;
    
    @Resource
    private OnlineUserService onlineUserService;
    
    //密码加密类
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    //userDetailsService，用于从数据库中取用户信息
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
    
    //配置访问认证白名单路径
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/*", "/user/getCode", "/user/register", "/sms/**", "/system/getFaceImages", "/webjars/**", "/v2/**", "/superuser/login"
        );
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 开启跨域资源共享（可以另外设置需要通过的具体的）
        http.cors()
                // 关闭csrf（跨站请求伪造）
                .and().csrf().disable()
                //关闭session（无状态） jwt无状态登录
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                //设置认证请求
                //放行静态资源
                .antMatchers("/expression/**", "/face/**", "/img/**", "/uploads/**").permitAll()
                .anyRequest().authenticated()
                .and().logout().logoutSuccessHandler(new ChatLogoutSuccessHandler())
                .and()
                // 添加到过滤链中，放在验证用户密码之前
                .addFilterBefore(new VerificationFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class)
                // 先是UsernamePasswordAuthenticationFilter用于login校验
                .addFilter(new JwtLoginAuthFilter(authenticationManager(), mongoTemplate, onlineUserService))
                // 再通过OncePerRequestFilter，对其它请求过滤
                .addFilter(new JwtPreAuthFilter(authenticationManager(), onlineUserService))
                //没有权限访问
                .httpBasic().authenticationEntryPoint(new UnAuthEntryPoint());
    }
}
