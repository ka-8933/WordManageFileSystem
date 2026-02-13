package LoginAndRegister;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTTest {

    //创建令牌
    @Test
    public void generateJWTTest() {
        Map<String, Object> addMap = new HashMap<>();
        addMap.put("id", 1);
        addMap.put("username", "牢大");

        String secretString = "xiaokaaichitanglaodaaichiman888888";
        SecretKey secretKey = Keys.hmacShaKeyFor(secretString.getBytes());
        String jwt = Jwts.builder() //建造令牌
                .claims(addMap) //添加个人信息
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000)) //有效期
                .signWith(secretKey, Jwts.SIG.HS256) //密码和转换方式
                .compact(); //生成

        System.out.println("生成的JWT：" + jwt);
    }

    //解析令牌
    @Test
    public void parseJWTTest(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiLniaLlpKciLCJleHAiOjE3NzAxOTQxMzB9.3GxL7ZHMNAq_Q7ZBMhzC3uYbne5aMrJjnrAnPs72qk4";

        String secretString = "xiaokaaichitanglaodaaichiman888888";
        SecretKey secretKey = Keys.hmacShaKeyFor(secretString.getBytes());
        Claims payload = Jwts.parser().verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Integer id = (Integer)payload.get("id");
        System.out.println(id);
    }
}
