package com.flab.readnshare.domain.member.service;

import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.member.dto.SignUpRequestDto;
import com.flab.readnshare.domain.member.repository.MemberRepository;
import com.flab.readnshare.global.common.exception.DuplicateEmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입을 하면 member가 등록된다")
    void signup_success_add_member(){
        // given
        SignUpRequestDto request = SignUpRequestDto.builder()
                .email("test@naver.com")
                .password("test24680!")
                .nickName("test")
                .build();

        Member expectedMember = Member.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickName(request.getNickName())
                .build();

        when(memberRepository.save(any(Member.class))).thenReturn(expectedMember);
        when(memberRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        // when
        Member savedMember = memberService.signUp(request);

        // then
        assertNotNull(savedMember);
        assertEquals("test@naver.com", savedMember.getEmail());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("중복 이메일로 회원가입 시 DuplicateEmailException가 발생한다")
    void signup_fail_duplicate_email(){
        // given
        SignUpRequestDto request = SignUpRequestDto.builder()
                .email("test@naver.com")
                .password("test24680!")
                .nickName("test")
                .build();

        // 중복 이메일을 가진 회원이 이미 존재한다고 가정
        when(memberRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(Member.builder()
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .nickName(request.getNickName())
                        .build()));

        // then
        assertThrows(DuplicateEmailException.class, () -> memberService.signUp(request),"이메일이 중복되어 회원가입에 실패해야 합니다.");
    }
}