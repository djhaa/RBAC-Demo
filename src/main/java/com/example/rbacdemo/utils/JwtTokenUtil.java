package com.example.rbacdemo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT生成工具类
 *
 * JWT = header.payload.signature
 * header ==> algorithm && token type
 * payload ==> username && create time && expire time
 * signature ==> HMACSHA512( base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
 *
 * @author djhaa
 */
public class JwtTokenUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String KEY_USERNAME = "sub";
    private static final String KEY_CREATE_TIME = "create";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.tokenHead}")
    private String head;

    /**
    * 生成JWT的token
    * */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 根据用户信息生成token
     * */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(KEY_USERNAME, userDetails.getUsername());
        claims.put(KEY_CREATE_TIME, new Date());
        return generateToken(claims);
    }

    /**
    * 从token中获取payload
    * */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            LOGGER.info("JWT格式验证失败：{}", token);
        }
        return claims;
    }

    /**
    * 生成token的过期时间
    * */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
    * 从token中获取登录用户名
    * */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 验证token是否还有效
     *
     * @param token 客户端传入的token
     * @param userDetails 数据库查询出来的用户信息
     * */
    public boolean verifyToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && isTokenExpired(token);
    }

    /**
    * 判断token是否已失效
    * */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return !expiredDate.before(new Date());
    }

    /**
    * 从token中获取过期时间
    * */
    private Date getExpiredDateFromToken(String token) {
        Date res = null;
        try {
            Claims claims = getClaimsFromToken(token);
            res = claims.getExpiration();
        } catch (Exception ignored) {

        }
        return res;
    }

    /**
    * 判断token是否可被刷新
    * */
    public boolean canRefresh(String token) {
        return isTokenExpired(token);
    }

    /**
    * 刷新token
    * */
    public String refreshToken(String oldToken) {
        if(StringUtils.isEmpty(oldToken)) {
            return null;
        }
        String token = oldToken.substring(head.length());
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        Claims claims = getClaimsFromToken(token);
        if(claims == null || !isTokenExpired(token)) {
            return null;
        }
        claims.put(KEY_CREATE_TIME, new Date());
        return generateToken(claims);
    }
}
