package com.example.wordmanagefilesystem.Tool;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JWTTool {

    //密钥定义
    private static final String secretString = "xiaokaaichitanglaodaaichiman888888";

    private static final SecretKey secretKey =  Keys.hmacShaKeyFor(secretString.getBytes());

    //有效时间
    private static final  Date limitTime = new Date(System.currentTimeMillis() + 360000 * 1000);

    //生成token
    public String generateToken(Map<String , Object> dataMap){
        String token = Jwts.builder().claims(dataMap).setExpiration(limitTime).signWith(secretKey).compact();
        log.info("生成token为{}" , token);
        return token;
    }

    //解析token
    public Claims parseToken(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public JWTTool() {
    }
}
