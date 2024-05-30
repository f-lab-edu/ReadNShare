package com.flab.readnshare.domain.review.event;

import com.flab.readnshare.domain.feed.service.FeedService;
import com.flab.readnshare.domain.follow.service.FollowService;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewEventListener {
    private final FollowService followService;
    private final FeedService feedService;

    @TransactionalEventListener
    private void updateFollowersFeed(ReviewEvent event) {
        Member writer = event.getMember();
        Review review = event.getReview();

        List<Long> followerIds = followService.getFollowerIds(writer);

        if (!followerIds.isEmpty()) {
            feedService.addToFeed(followerIds, review);
        }
    }
}
