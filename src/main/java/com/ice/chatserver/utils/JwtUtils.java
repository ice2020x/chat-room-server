package com.ice.chatserver.utils;

import com.ice.chatserver.pojo.config.JwtInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

public class JwtUtils {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    //设置一天时间
    private static final Long EXPIRE = 3 * 24 * 3600 * 1000L;
    //用于signature（签名）部分解密
    private static final String SECRET = "javaee-ice2021x";
    
    //生成Token
    public static String createJwt(String userId, String username) {
        Assert.notNull(userId, "用户ID不能为空");
        Assert.notNull(username, "用户名不能为空");
        return Jwts.builder().setSubject(userId)
                .claim("userId", userId)
                .claim("username", username)
                .setIssuedAt(new Date())
                .setId(UUID.randomUUID().toString())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
    }
    
    //解析Token
    public static Claims parseJwt(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }
    
    //从Token中提取信息
    public static JwtInfo getInfoByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(jwtToken)) {
            return null;
        }
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwtToken).getBody();
        return new JwtInfo(claims.get("userId").toString(), claims.get("username").toString());
    }
    
    //从Token中提取当前uid
    public static String getCurrentUserId(HttpServletRequest request) {
        try {
            JwtInfo infoByJwtToken = JwtUtils.getInfoByJwtToken(request);
            if (infoByJwtToken == null) {
                System.out.println("jwt parse fail");
                return null;
            }
            return infoByJwtToken.getUserId();
        } catch (Exception e) {
            System.out.println("获取当前用户id失败");
            return null;
        }
    }
}