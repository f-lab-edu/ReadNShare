package com.flab.readnshare.domain.feed.controller;

import com.flab.readnshare.domain.feed.dto.FeedRequestDto;
import com.flab.readnshare.domain.feed.dto.FeedResponseDto;
import com.flab.readnshare.domain.feed.facade.FeedFacade;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.global.common.resolver.SignInMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FeedApiController {
    private final FeedFacade feedFacade;

    @GetMapping("/api/feed")
    public ResponseEntity<List<FeedResponseDto>> getFeed(@SignInMember Member member, @RequestBody FeedRequestDto dto) {
        return new ResponseEntity<>(feedFacade.getFeed(member.getId(), dto.getLastReviewId(), dto.getLimit()), HttpStatus.OK);
    }
}
