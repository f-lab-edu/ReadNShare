package com.flab.readnshare.domain.auth.controller;

import com.flab.readnshare.AuthTestFixture;
import com.flab.readnshare.domain.auth.dto.SignInRequestDto;
import com.flab.readnshare.domain.auth.exception.InvalidPasswordException;
import com.flab.readnshare.domain.auth.repository.RefreshTokenRepository;
import com.flab.readnshare.domain.auth.service.AuthService;
import com.flab.readnshare.domain.member.dto.MemberResponseDto;
import com.flab.readnshare.domain.member.exception.MemberNotFoundException;
import com.flab.readnshare.domain.member.service.MemberService;
import com.flab.readnshare.global.common.advice.ApiExceptionAdvice;
import com.flab.readnshare.global.common.auth.jwt.JwtCode;
import com.flab.readnshare.global.common.auth.jwt.JwtUtil;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthApiControllerTest {
    @Mock
    private AuthService authService;
    @Mock
    private MemberService memberService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @InjectMocks
    private AuthApiController authApiController;
    @InjectMocks
    ApiExceptionAdvice apiExceptionAdvice;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders
                .standaloneSetup(authApiController)
                .setControllerAdvice(apiExceptionAdvice)
                .build();
    }

    @Test
    @DisplayName("로그인에 성공한다.")
    void signin_success() throws Exception {
        // given
        SignInRequestDto request = AuthTestFixture.getSignInRequestDto();

        given(authService.signIn(any(SignInRequestDto.class)))
                .willReturn(MemberResponseDto.builder()
                        .id(1L)
                        .email(request.getEmail())
                        .nickName("test")
                        .build().toEntity());

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(request.getEmail()));
    }

    @Test
    @DisplayName("로그인에 실패한다.(존재하지 않는 회원)")
    void signin_fail_email() throws Exception {
        // given
        SignInRequestDto request = AuthTestFixture.getSignInRequestDto();

        when(authService.signIn(any(SignInRequestDto.class)))
                .thenThrow(new MemberNotFoundException());

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인에 실패한다.(비밀번호 불일치)")
    void signin_fail_password() throws Exception {
        // given
        SignInRequestDto request = AuthTestFixture.getSignInRequestDto();

        when(authService.signIn(any(SignInRequestDto.class)))
                .thenThrow(new InvalidPasswordException());

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Access token 재발급에 성공한다.")
    void refresh_success() throws Exception {
        // given
        MockCookie refreshTokenCookie = new MockCookie("refreshToken", "refreshTokenValue");

        when(jwtUtil.extractMemberId(any(String.class))).thenReturn("1");
        when(jwtUtil.validateToken(any(String.class))).thenReturn(JwtCode.ACCESS);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(refreshTokenCookie)
        );

        // then
        resultActions.andExpect(status().isOk());

    }

    @Test
    @DisplayName("Access token 재발급에 실패한다. (refresh token이 없는 경우)")
    void refresh_fail_null_token() throws Exception {
        // given
        MockCookie refreshTokenCookie = new MockCookie("refreshToken", null);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(refreshTokenCookie)
        );

        // then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Access token 재발급에 실패한다. (refresh token이 이상한 값인 경우)")
    void refresh_fail_denied_token() throws Exception {
        // given
        MockCookie refreshTokenCookie = new MockCookie("refreshToken", "refreshTokenValue");

        when(jwtUtil.extractMemberId(any(String.class))).thenReturn("1");
        when(jwtUtil.validateToken(any(String.class))).thenReturn(JwtCode.DENIED);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(refreshTokenCookie)
        );

        // then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Access token 재발급에 실패한다. (refresh token이 만료된 경우)")
    void refresh_fail_expired_token() throws Exception {
        // given
        MockCookie refreshTokenCookie = new MockCookie("refreshToken", "refreshTokenValue");

        when(jwtUtil.extractMemberId(any(String.class))).thenReturn("1");
        when(jwtUtil.validateToken(any(String.class))).thenReturn(JwtCode.EXPIRED);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(refreshTokenCookie)
        );

        // then
        resultActions.andExpect(status().isUnauthorized());
    }

}