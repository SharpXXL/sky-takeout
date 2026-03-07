package com.sky.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     *
     * @param secretKey jwt秘钥
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // 指定签名的时候使用的签名算法，也就是header那部分
        return Jwts.builder()
                .claims(claims) // 设置负载
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes())) // 签名
                .expiration(new Date(System.currentTimeMillis() + ttlMillis))
                .compact();
    }

    /**
     * Token解密
     *
     * @param secretKey jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param token     加密后的token
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        // 得到DefaultJwtParser
        Claims claims = Jwts.parser()             // 1. 准备好一个“安检机” (获取构造器)
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes())) // 2. 输入“正确密码” (设置验证密钥)
                .build()                         // 3. 组装完毕，启动机器 (构建解析器)
                .parseSignedClaims(token)          // 4. 把令牌丢进去检查 (解析签名)
                .getPayload();                   // 5. 检查通过，取出里面的东西 (获取数据)
        return claims;
    }

}
