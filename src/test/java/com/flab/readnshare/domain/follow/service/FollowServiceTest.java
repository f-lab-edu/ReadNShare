package com.flab.readnshare.domain.follow.service;

import com.flab.readnshare.FollowTestFixture;
import com.flab.readnshare.domain.follow.domain.Follow;
import com.flab.readnshare.domain.follow.exception.DuplicateFollowException;
import com.flab.readnshare.domain.follow.repository.FollowRepository;
import com.flab.readnshare.domain.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {
    @Mock
    private FollowRepository followRepository;
    @InjectMocks
    private FollowService followService;

    @Test
    @DisplayName("이미 팔로우하고 있는 사용자를 또 팔로우하면 DuplicateFollowException이 발생한다")
    void follow_fail_duplicate_follow() {
        // given
        Member fromMember = FollowTestFixture.getMemberEntity();
        Member toMember = FollowTestFixture.getMemberEntity();

        Optional<Follow> follow = Optional.of(mock(Follow.class));
        when(followRepository.findByFromMemberAndToMember(fromMember, toMember)).thenReturn(follow);

        // when & then
        assertThrows(DuplicateFollowException.class, () ->
                followService.save(fromMember, toMember));
    }

}