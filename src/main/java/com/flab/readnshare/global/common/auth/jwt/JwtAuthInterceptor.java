package com.flab.readnshare.global.common.auth.jwt;

import com.flab.readnshare.global.common.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = jwtUtil.extractAccessToken(request);

        if (accessToken != null) {
            switch (jwtUtil.validateToken(accessToken)) {
                case DENIED -> throw new AuthException.DeniedTokenException();
                case EXPIRED -> throw new AuthException.ExpiredTokenException();
                case ACCESS -> {
                    return true;
                }
            }
        } else {
            throw new AuthException.NullTokenException();
        }

        return false;
    }
}
