package com.example.security.utils;

import com.example.security.entity.SOSUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenUtil implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    private static final long serialVersionUID = -3301605591108950415L;

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_AUDIENCE = "audience";
    static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String getUsernameFromToken(String token) {
        String username = "";
        try {
            final Claims claims = getClaimsFromToken(token);
            if(claims != null){
                username = claims.getSubject();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            if(claims != null){
                created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            if(claims != null) {
                expiration = claims.getExpiration();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return expiration;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            claims = null;
        }
        return claims;
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, System.currentTimeMillis());
        String token = generateToken(claims);

        stringRedisTemplate.opsForValue().set(userDetails.getUsername(), token);
        stringRedisTemplate.expire(userDetails.getUsername(), expiration, TimeUnit.MINUTES);

        return token;
    }


    public String exprieToken(UserDetails userDetails) {
        stringRedisTemplate.expire(userDetails.getUsername(), 0, TimeUnit.MILLISECONDS);
        return null;
    }

    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        // 登陆成功生成JWT
//        String token = Jwts.builder()
//                // 放入用户名和用户ID
//                .setId(selfUserEntity.getUserId()+"")
//                // 主题
//                .setSubject(selfUserEntity.getUsername())
//                // 签发时间
//                .setIssuedAt(new Date())
//                // 签发者
//                .setIssuer("sans")
//                // 自定义属性 放入用户拥有权限
//                .claim("authorities", JSON.toJSONString(selfUserEntity.getAuthorities()))
//                // 失效时间
//                .setExpiration(new Date(System.currentTimeMillis() + JWTConfig.expiration))
//                // 签名算法和密钥
//                .signWith(SignatureAlgorithm.HS512, JWTConfig.secret)
//                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token));
    }

    public String refreshToken(String token) {
        String refreshedToken = "";
        try {
            final Claims claims = getClaimsFromToken(token);
            if(claims != null){
                claims.put(CLAIM_KEY_CREATED, new Date());
            }
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return refreshedToken;
    }

    // token中取到的username
    public Boolean validateToken(String token, UserDetails userDetails) {
        SOSUserDetails user = (SOSUserDetails) userDetails;
        final String username = getUsernameFromToken(token);
        System.out.println(user.getPassword());
        String redisToken = stringRedisTemplate.opsForValue().get(username);
        if (StringUtils.isNotEmpty(redisToken) && redisToken.equals(token) && username.equals(user.getUsername())) {
            stringRedisTemplate.expire(userDetails.getUsername(), expiration, TimeUnit.MINUTES);
            return true;
        }

        return false;
    }
}