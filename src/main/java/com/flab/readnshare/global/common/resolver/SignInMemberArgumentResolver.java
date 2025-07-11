package com.flab.readnshare.global.common.resolver;

import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.member.exception.MemberNotFoundException;
import com.flab.readnshare.domain.member.repository.MemberRepository;
import com.flab.readnshare.global.common.auth.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class SignInMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SignInMember.class)
                && parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Member resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String accessToken = jwtUtil.extractAccessToken(httpServletRequest);

        Long memberId = Long.valueOf(jwtUtil.extractMemberId(accessToken));

        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }
}
