package com.flab.readnshare.domain.member.controller;

import com.flab.readnshare.domain.member.dto.MemberResponseDto;
import com.flab.readnshare.domain.member.dto.SignUpRequestDto;
import com.flab.readnshare.domain.member.service.MemberService;
import com.flab.readnshare.global.common.advice.ApiExceptionAdvice;
import com.flab.readnshare.global.common.exception.DuplicateEmailException;
import com.flab.readnshare.global.common.exception.ErrorCode;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MemberApiControllerTest {
    @Mock
    private MemberService memberService;
    @InjectMocks
    private MemberApiController memberApiController;
    @InjectMocks
    ApiExceptionAdvice apiExceptionAdvice;
    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders
                .standaloneSetup(memberApiController)
                .setControllerAdvice(apiExceptionAdvice)
                .build();
    }

    @Test
    @DisplayName("회원가입에 성공한다.")
    void signup_success() throws Exception {
        // given
        SignUpRequestDto request = SignUpRequestDto.builder()
                .email("test@naver.com")
                .password("test24680!")
                .nickName("test")
                .build();

        given(memberService.signUp(any(SignUpRequestDto.class)))
                .willReturn(MemberResponseDto.builder()
                        .id(1L)
                        .email("test@naver.com")
                        .nickName("test")
                        .build().toEntity());

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/member/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(request))
        );

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andExpect(jsonPath("$.nickName").value(request.getNickName()));
    }

    @Test
    @DisplayName("회원가입에 실패한다.(중복 이메일)")
    void signup_fail() throws Exception {
        // given
        SignUpRequestDto request = SignUpRequestDto.builder()
                .email("test@naver.com")
                .password("test24680!")
                .nickName("test")
                .build();

        when(memberService.signUp(any(SignUpRequestDto.class)))
                .thenThrow(new DuplicateEmailException(ErrorCode.EMAIL_DUPLICATION));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/member/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입에 실패한다.(이메일 포맷)")
    void signup_fail_emali_format() throws Exception {
        // given
        SignUpRequestDto request = SignUpRequestDto.builder()
                .email("123")
                .password("test24680!")
                .nickName("test")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/member/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("입력값을 확인하세요."))
                .andExpect(jsonPath("$.errors[0].field").value("email"))
                .andExpect(jsonPath("$.errors[0].message").value("이메일 형식이 맞지 않습니다."))
            ;
    }

    @Test
    @DisplayName("회원가입에 실패한다.(비밀번호 포맷)")
    void signup_fail_password_format() throws Exception {
        // given
        SignUpRequestDto request = SignUpRequestDto.builder()
                .email("test@naver.com")
                .password("123")
                .nickName("test")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/member/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("입력값을 확인하세요."))
                .andExpect(jsonPath("$.errors[0].field").value("password"))
                .andExpect(jsonPath("$.errors[0].message").value("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요."))
        ;
    }

}