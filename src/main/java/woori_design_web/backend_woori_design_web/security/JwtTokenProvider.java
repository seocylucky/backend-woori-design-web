package woori_design_web.backend_woori_design_web.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private Key key;
    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Access Token 생성
    public String createAccessToken(String loginId) {
        return createToken(loginId, accessTokenExpiration);
    }

    // Refresh Token 생성
    public String createRefreshToken(String userId) {
        return createToken(userId, refreshTokenExpiration);
    }

    // 토큰 생성
    private String createToken(String loginId, long expirationTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(loginId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 userId(loginId) 추출
    public String getUserIdFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    // 토큰 만료 시간(ms) 추출
    public long getExpiration(String token) {
        return parseClaims(token).getExpiration().getTime();
    }

    // JWT 클레임 파싱
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 로그아웃 처리 (토큰 무효화)
    public void invalidateToken(String token) {
        String userId = getUserIdFromToken(token);
        redisTemplate.opsForValue().set(token, "blacklisted", accessTokenExpiration, TimeUnit.MILLISECONDS);
        log.info("토큰 무효화: {}", token);
    }
}
