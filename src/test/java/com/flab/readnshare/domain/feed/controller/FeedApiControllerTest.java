package com.flab.readnshare.domain.feed.controller;

import com.flab.readnshare.FeedTestFixture;
import com.flab.readnshare.domain.feed.dto.FeedResponseDto;
import com.flab.readnshare.domain.feed.facade.FeedFacade;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.global.common.resolver.SignInMemberArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FeedApiControllerTest {
    @Mock
    private FeedFacade feedFacade;
    @Mock
    private SignInMemberArgumentResolver signInMemberArgumentResolver;
    @InjectMocks
    private FeedApiController feedApiController;
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(feedApiController)
                .setCustomArgumentResolvers(signInMemberArgumentResolver)
                .build();
    }

    @DisplayName("피드 조회에 성공한다.")
    @Test
    void get_feed_success() throws Exception {
        // given
        Member member = Member.builder().id(1L).build();
        List<FeedResponseDto> feeds = FeedTestFixture.getFeedResponses();

        when(signInMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(signInMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).thenReturn(member);
        when(feedFacade.getFeed(anyLong(), isNull(), anyInt())).thenReturn(feeds);

        // when & then
        mockMvc.perform(
                        get("/api/feeds")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].reviewId").value(1L))
                .andExpect(jsonPath("$[0].nickName").value("user1"))
                .andExpect(jsonPath("$[0].content").value("content1"))
                .andExpect(jsonPath("$[0].bookTitle").value("book1"));
    }
}