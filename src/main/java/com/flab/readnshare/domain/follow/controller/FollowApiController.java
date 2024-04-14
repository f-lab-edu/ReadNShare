package com.flab.readnshare.domain.follow.controller;

import com.flab.readnshare.domain.follow.facade.FollowFacade;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.global.common.resolver.SignInMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowApiController {
    private final FollowFacade followFacade;

    @PostMapping("/{memberEmail}")
    public ResponseEntity<Void> follow(
            @PathVariable String memberEmail
            , @SignInMember Member fromMember) {
        followFacade.follow(memberEmail, fromMember);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{memberEmail}")
    public ResponseEntity<Void> unfollow(
            @PathVariable String memberEmail
            , @SignInMember Member fromMember) {
        followFacade.unfollow(memberEmail, fromMember);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
