package com.flab.readnshare.domain.feed.controller;

import com.flab.readnshare.domain.feed.dto.FeedResponseDto;
import com.flab.readnshare.domain.feed.facade.FeedFacade;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.global.common.resolver.SignInMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FeedApiController {
    private final FeedFacade feedFacade;

    @GetMapping("/api/feeds")
    public ResponseEntity<List<FeedResponseDto>> getFeed(
            @SignInMember Member member,
            @RequestParam(required = false) Long lastReviewId,
            @RequestParam(defaultValue = "10") int limit) {
        return new ResponseEntity<>(feedFacade.getFeed(member.getId(), lastReviewId, limit), HttpStatus.OK);
    }
}
