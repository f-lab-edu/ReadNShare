package com.flab.readnshare.domain.auth.controller;

import com.flab.readnshare.domain.auth.dto.SignInRequestDto;
import com.flab.readnshare.domain.auth.service.AuthService;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.member.dto.MemberResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/signIn")
    public ResponseEntity<MemberResponseDto> signIn(@RequestBody SignInRequestDto dto, HttpServletResponse response){
        Member member = authService.signIn(dto);

        MemberResponseDto responseDto = MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .build();

        // access token 발급
        authService.sendAccessToken(response, member.getId());
        // refresh token 발급
        authService.sendRefreshToken(response, member.getId());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
