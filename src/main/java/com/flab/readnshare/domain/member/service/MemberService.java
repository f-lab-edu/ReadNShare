package com.flab.readnshare.domain.member.service;

import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.member.dto.SignUpRequestDto;
import com.flab.readnshare.domain.member.repository.MemberRepository;
import com.flab.readnshare.global.common.exception.DuplicateEmailException;
import com.flab.readnshare.global.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional
    public Member signUp(SignUpRequestDto dto){
        validateDuplicateMember(dto.getEmail());
        return memberRepository.save(dto.toEntity());
    }

    private void validateDuplicateMember(String email){
        if (memberRepository.findByEmail(email).isPresent()){
            throw new DuplicateEmailException(ErrorCode.EMAIL_DUPLICATION);
        }
    }

}
