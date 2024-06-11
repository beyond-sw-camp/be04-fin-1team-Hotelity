package org.iot.hotelitybackend.login.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.iot.hotelitybackend.common.constant.Constant.*;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final long tokenExpirationTime;
    private final long refreshTokenExpirationTime;

    public JwtUtil(
            @Value("${token.secret}") String secretKey,
            @Value("${token.expiration_time}") long tokenExpirationTime,
            @Value("${spring.data.redis.expiration_time}") long refreshTokenExpirationTime
    ) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        String algorithm = Jwts.SIG.HS256.key().build().getAlgorithm();

        this.secretKey = new SecretKeySpec(keyBytes, algorithm);
        this.tokenExpirationTime = tokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    public String getLoginCode(String accessToken) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload()
                .get(JWT_ATTR_LOGIN_CODE, String.class);
    }

    public String getRole(String accessToken) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload()
                .get(JWT_ATTR_ROLE, String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    public String createAccessToken(String loginCode, String role) {
        return Jwts
                .builder()
                .claim(JWT_ATTR_LOGIN_CODE, loginCode)
                .claim(JWT_ATTR_ROLE, role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(String accessToken) {
        return Jwts
                .builder()
                .claim(KEY_ACCESS_TOKEN, accessToken)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
                .signWith(secretKey)
                .compact();
    }
}
