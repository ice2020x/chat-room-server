package com.ice.chatserver.utils;

import com.ice.chatserver.pojo.config.JwtInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

/**
* @author ice2020x
* @Date: 2021/12/18
* @Description: 生成jwt的一个认证
**/
public class JwtUtils {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    //设置一天时间
    private static final Long EXPIRE = 3*24 * 3600 * 1000L;
    //用于signature（签名）部分解密
    private static final String SECRET = "javaee-ice2021x";

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Param: uid 和用户名
    * @return: token
    * @Description: 生成token
    **/
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

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Param: token
    * @return: 解析token，获取用户信息
    * @Description: 解析token
    **/
    public static Claims parseJwt(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    public static JwtInfo getInfoByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(jwtToken)) {
            return null;
        }
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwtToken).getBody();
        return new JwtInfo(claims.get("userId").toString(),claims.get("username").toString());
    }

}