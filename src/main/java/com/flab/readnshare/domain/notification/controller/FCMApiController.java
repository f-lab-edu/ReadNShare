package com.flab.readnshare.domain.notification.controller;

import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.notification.dto.SaveFCMTokenRequestDto;
import com.flab.readnshare.domain.notification.service.FCMService;
import com.flab.readnshare.global.common.resolver.SignInMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FCMApiController {
    private final FCMService fcmService;

    @PostMapping
    public ResponseEntity<Void> saveFCMToken(@SignInMember Member member, @RequestBody SaveFCMTokenRequestDto dto) {
        fcmService.saveFCMToken(member, dto.getToken());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
