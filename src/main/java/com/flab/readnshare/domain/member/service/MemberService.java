package com.flab.readnshare.domain.member.service;

import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.member.dto.SignUpRequestDto;
import com.flab.readnshare.domain.member.exception.DuplicateEmailException;
import com.flab.readnshare.domain.member.exception.MemberNotFoundException;
import com.flab.readnshare.domain.member.repository.MemberRepository;
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
    public Member signUp(SignUpRequestDto dto) {
        validateDuplicateMember(dto.getEmail());
        return memberRepository.save(dto.toEntity());
    }

    private void validateDuplicateMember(String email) {
        memberRepository.findByEmail(email).ifPresent(m -> {
            throw new DuplicateEmailException();
        });
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

}
