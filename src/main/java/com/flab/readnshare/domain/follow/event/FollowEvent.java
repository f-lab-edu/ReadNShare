package com.flab.readnshare.domain.follow.event;

import com.flab.readnshare.domain.follow.domain.Follow;
import com.flab.readnshare.domain.member.domain.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FollowEvent extends ApplicationEvent {
    private final Member fromMember;
    private final Member toMember;

    public FollowEvent(Object source, Member fromMember, Member toMember) {
        super(source);
        this.fromMember = fromMember;
        this.toMember = toMember;
    }
}
