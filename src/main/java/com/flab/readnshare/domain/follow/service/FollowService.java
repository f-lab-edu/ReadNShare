package com.flab.readnshare.domain.follow.service;

import com.flab.readnshare.domain.follow.domain.Follow;
import com.flab.readnshare.domain.follow.repository.FollowRepository;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.global.common.exception.FollowException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;

    public Follow save(Member fromMember, Member toMember) {
        validateDuplicateFollow(fromMember, toMember);

        Follow follow = Follow.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build();

        return followRepository.save(follow);
    }

    private void validateDuplicateFollow(Member fromMember, Member toMember) {
        followRepository.findByFromMemberAndToMember(fromMember, toMember).ifPresent(f -> {
            throw new FollowException.DuplicateFollowException();
        });
    }

    public void delete(Member fromMember, Member toMember) {
        followRepository.findByFromMemberAndToMember(fromMember, toMember).ifPresent(f -> {
            followRepository.delete(f);
        });
    }
}
