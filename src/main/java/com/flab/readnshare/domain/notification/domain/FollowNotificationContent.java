package com.flab.readnshare.domain.notification.domain;

import com.flab.readnshare.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FollowNotificationContent implements NotificationContent {
    private Member fromMember;
    private Member toMember;

    @Builder
    public FollowNotificationContent(Member fromMember, Member toMember) {
        this.fromMember = fromMember;
        this.toMember = toMember;
    }

    @Override
    public String getTitle() {
        return "팔로우 시작";
    }

    @Override
    public String getBody() {
        return fromMember.getNickName() + "님이 팔로우하기 시작했습니다.";
    }

    @Override
    public Long getReceiverId() {
        return toMember.getId();
    }

}
