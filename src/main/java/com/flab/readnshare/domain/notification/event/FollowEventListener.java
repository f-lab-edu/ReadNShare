package com.flab.readnshare.domain.notification.event;

import com.flab.readnshare.domain.follow.event.FollowEvent;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.notification.service.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class FollowEventListener {
    private final FCMService fcmService;

    @TransactionalEventListener
    public void handle(FollowEvent event) {
        Member fromMember = event.getFromMember();
        Member toMember = event.getToMember();

        fcmService.sendNotification(toMember.getId(), fromMember.getNickName());
    }
}
