package com.flab.readnshare.global.common.auth.jwt;

import com.flab.readnshare.domain.auth.exception.DeniedTokenException;
import com.flab.readnshare.domain.auth.exception.ExpiredTokenException;
import com.flab.readnshare.domain.auth.exception.NullTokenException;
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
                case DENIED -> throw new DeniedTokenException();
                case EXPIRED -> throw new ExpiredTokenException();
                case ACCESS -> {
                    return true;
                }
            }
        } else {
            throw new NullTokenException();
        }

        return false;
    }
}
