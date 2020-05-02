package com.ly.storeserver.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * @Description:
 * @Author ly
 * @Date 2020/3/31 22:32
 * @Version V1.0.0
 **/
public class JwtTokenUtil {

    /**
     * jwt秘钥
     */
    private String jwtSecret;

    /**
     * 默认jwt过期时间
     */
    private Long expireTime;

    public JwtTokenUtil(String jwtSecret, Long expireTime) {
        this.jwtSecret = jwtSecret;
        this.expireTime = expireTime;
    }

    public String generateToken(String userId, Map<String, Object> claims) {
        final Date defualExpireTime = new Date(System.currentTimeMillis() + expireTime * 1000);
        return generateToken(userId, defualExpireTime, claims);
    }

    /**
     * 生成token令牌，根据用户id和过期时间
     */
    public String generateToken(String uerId, Date expireTime, Map<String, Object> claims) {
        final Date createDate = new Date();
        if (claims == null) {
            return Jwts.builder()
                    .setSubject(uerId)
                    .setIssuedAt(createDate)
                    .setExpiration(expireTime)
                    .signWith(SignatureAlgorithm.HS256, this.jwtSecret)
                    .compact();
        } else {
            return Jwts.builder()
                    .setSubject(uerId)
                    .setClaims(claims)
                    .setIssuedAt(createDate)
                    .setExpiration(expireTime)
                    .signWith(SignatureAlgorithm.HS256, this.jwtSecret)
                    .compact();
        }
    }

    /**
     * 获取jwt的payload部分
     */
    public Claims getClaimFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 解析token是否正确
     */
    public Boolean checkToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public static void main(String[] args) {

    }
}
