package com.flab.readnshare.domain.follow.facade;

import com.flab.readnshare.FollowTestFixture;
import com.flab.readnshare.domain.follow.domain.Follow;
import com.flab.readnshare.domain.follow.event.FollowEvent;
import com.flab.readnshare.domain.follow.exception.SelfFollowException;
import com.flab.readnshare.domain.follow.service.FollowService;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FollowFacadeTest {
    @Mock
    private MemberService memberService;
    @Mock
    private FollowService followService;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @InjectMocks
    private FollowFacade followFacade;

    @Test
    @DisplayName("팔로우에 성공한다")
    void follow_success() {
        // given
        String memberEmail = FollowTestFixture.getMemberEmail();

        Member fromMember = FollowTestFixture.getMemberEntity();
        Member toMember = FollowTestFixture.getMemberEntity();

        Follow expectedFollow = FollowTestFixture.getFollowEntity(fromMember, toMember);

        when(memberService.findByEmail(memberEmail)).thenReturn(toMember);
        when(followService.save(any(Member.class), any(Member.class))).thenReturn(expectedFollow);

        // when
        Follow savedFollow = followFacade.follow(memberEmail, fromMember);

        // then
        assertNotNull(savedFollow);
        assertEquals(savedFollow.getFromMember(), fromMember);
        assertEquals(savedFollow.getToMember(), toMember);

        verify(eventPublisher).publishEvent(any(FollowEvent.class));
    }

    @Test
    @DisplayName("자기자신을 팔로우하면 SelfFollowException이 발생한다")
    void follow_fail_self_follow() {
        // given
        String memberEmail = FollowTestFixture.getMemberEmail();
        Member fromMember = FollowTestFixture.getMemberEntity();

        when(memberService.findByEmail(memberEmail)).thenReturn(fromMember);

        // when & then
        assertThrows(SelfFollowException.class, () ->
                followFacade.follow(memberEmail, fromMember));
    }

    @Test
    @DisplayName("언팔로우에 성공한다")
    void unfollow_success() {
        // given
        String memberEmail = FollowTestFixture.getMemberEmail();

        Member fromMember = FollowTestFixture.getMemberEntity();
        Member toMember = FollowTestFixture.getMemberEntity();

        when(memberService.findByEmail(memberEmail)).thenReturn(toMember);

        // when
        followFacade.unfollow(memberEmail, fromMember);

        // then
        verify(followService, times(1)).delete(fromMember, toMember);
    }
}