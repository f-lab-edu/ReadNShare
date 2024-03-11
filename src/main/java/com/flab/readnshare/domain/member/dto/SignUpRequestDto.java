package com.flab.readnshare.domain.member.dto;

import com.flab.readnshare.domain.member.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpRequestDto {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickName;

    @Builder
    public SignUpRequestDto(String email, String password, String nickName){
        this.email = email;
        this.password = password;
        this.nickName = nickName;
    }

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .password(password)
                .nickName(nickName)
                .build();
    }

}
