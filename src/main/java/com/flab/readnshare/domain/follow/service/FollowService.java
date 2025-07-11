package com.flab.readnshare.domain.follow.service;

import com.flab.readnshare.domain.follow.domain.Follow;
import com.flab.readnshare.domain.follow.exception.DuplicateFollowException;
import com.flab.readnshare.domain.follow.repository.FollowRepository;
import com.flab.readnshare.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
            throw new DuplicateFollowException();
        });
    }

    public void delete(Member fromMember, Member toMember) {
        followRepository.findByFromMemberAndToMember(fromMember, toMember).ifPresent(f -> {
            followRepository.delete(f);
        });
    }

    public List<Long> getFollowerIds(Member member) {
        List<Follow> followers = followRepository.findByToMember(member);

        return followers.stream()
                .map(follow -> follow.getFromMember().getId())
                .toList();
    }
}
