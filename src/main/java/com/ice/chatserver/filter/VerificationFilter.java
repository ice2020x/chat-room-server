package com.ice.chatserver.filter;
import com.ice.chatserver.common.R;
import com.ice.chatserver.common.ResultEnum;
import com.ice.chatserver.handler.HttpServletRequestReplacedWrapper;
import com.ice.chatserver.utils.CookieUtil;
import com.ice.chatserver.utils.HttpServletRequestUtil;
import com.ice.chatserver.utils.RedisKeyUtil;
import com.ice.chatserver.utils.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//验证码过滤器
public class VerificationFilter extends GenericFilter {
    private RedisTemplate<String, String> redisTemplate;
    
    public VerificationFilter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        if ("POST".equals(req.getMethod()) && ("/user/login".equals(req.getServletPath()))) {
            //从cookie中获取uuid
            String kaptchaOwner = CookieUtil.getValue(req, "kaptchaOwner");
            System.out.println(kaptchaOwner);
            //获取填写的验证码，要用特殊的方法，不能通过 req.getParameter()
            System.out.println("验证码:" + req.getParameter("cvCode"));
            ServletRequest requestWrapper = new HttpServletRequestReplacedWrapper(req);
            String cvCode = HttpServletRequestUtil.getBodyTxt(requestWrapper, "cvCode");
            String kaptcha = null;
            if (StringUtils.isNotBlank(kaptchaOwner)) {
                // 验证码的 key 为 uuid， 值为 验证码
                String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
                Long expire = redisTemplate.getExpire(redisKey);
                System.out.println("当前redis中剩余的过期时间为：" + expire);
                kaptcha = redisTemplate.opsForValue().get(redisKey);
            }
            System.out.println("redis的验证码为：" + kaptcha);
            System.out.println("填写的验证码为：" + cvCode);
            if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(cvCode) || !kaptcha.equalsIgnoreCase(cvCode)) {
                ResponseUtil.out(resp, R.error().resultEnum(ResultEnum.KAPTCHA_TIME_OUT_OR_ERROR));
            }
            else {
                filterChain.doFilter(requestWrapper, resp);
            }
        }
        else {
            filterChain.doFilter(req, resp);
        }
        
    }
}
