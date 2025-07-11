package com.flab.readnshare.global.common.auth.jwt;

import com.flab.readnshare.domain.auth.domain.RefreshToken;
import com.flab.readnshare.domain.auth.exception.NullTokenException;
import com.flab.readnshare.domain.auth.repository.RefreshTokenRepository;
import com.flab.readnshare.global.common.util.CookieUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {
    @Value("${jwt.access.expiration}")
    private Long accessTokenValidTime;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenValidTime;

    private final Key key;

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public JwtUtil(@Value("${jwt.security.secretKey}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String createAccessToken(Long memberId) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .compact();
    }

    public String createRefreshToken(Long memberId) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));
        Date now = new Date();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .compact();

        // Redis에 저장
        RefreshToken redisRefreshToken = RefreshToken.builder()
                .refreshTokenValue(refreshToken)
                .memberId(memberId)
                .expiration(refreshTokenValidTime)
                .build();

        refreshTokenRepository.save(redisRefreshToken);

        return refreshToken;
    }

    public JwtCode validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
            return JwtCode.ACCESS;
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException | SignatureException ex) {
            return JwtCode.DENIED;
        } catch (ExpiredJwtException ex) {
            return JwtCode.EXPIRED;
        }
    }

    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        CookieUtils.addCookie(response, "refreshToken", refreshToken, refreshTokenValidTime.intValue() / 1000);
    }

    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader("Authorization", "Bearer " + accessToken);
    }

    public String extractRefreshToken(HttpServletRequest request) {
        Optional<Cookie> cookie = CookieUtils.getCookie(request, "refreshToken");
        return cookie.map(Cookie::getValue).orElseThrow(NullTokenException::new);
    }

    public String extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .map(token -> token.substring("Bearer ".length()))
                .orElseThrow(NullTokenException::new);
    }

    public String extractMemberId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


}
