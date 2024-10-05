package cn.tedu.tmall.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 14:25
 *
 * JWT工具类
 */
public class JwtUtils {

    /**
     * 生成JWT
     *
     * @param claims     存入到JWT中的数据
     * @param expiration JWT过期时间
     * @param secretKey  密钥
     * @return JWT数据
     */
    public static synchronized String generate(Map<String, Object> claims, Date expiration, String secretKey) {
        return Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * 解析JWT
     *
     * @param jwt       JWT数据
     * @param secretKey 生成JWT时使用的密钥
     * @return 解析结果
     */
    public static synchronized Claims parse(String jwt, String secretKey) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

}