package com.flab.readnshare.domain.like.controller;

import com.flab.readnshare.domain.like.service.ReviewLikeService;
import com.flab.readnshare.global.common.advice.ApiExceptionAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReviewLikeApiControllerTest {
    @Mock
    private ReviewLikeService reviewLikeService;
    @InjectMocks
    private ReviewLikeApiController reviewLikeApiController;
    @InjectMocks
    private ApiExceptionAdvice apiExceptionAdvice;
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(reviewLikeApiController)
                .setControllerAdvice(apiExceptionAdvice)
                .build();
    }

    @DisplayName("리뷰 좋아요에 성공한다.")
    @Test
    void like_success() throws Exception {
        // when & then
        mockMvc.perform(post("/api/review-like/review/1/member/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(reviewLikeService, times(1)).like(any(Long.class), any(Long.class));
    }

    @DisplayName("리뷰 좋아요 취소에 성공한다.")
    @Test
    void unlike_success() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/review-like/review/1/member/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(reviewLikeService, times(1)).unlike(any(Long.class), any(Long.class));
    }

    @DisplayName("중복 좋아요 시 서버 에러가 발생한다.")
    @Test
    void duplicate_like_server_error() throws Exception {
        // given
        doThrow(new DataIntegrityViolationException("duplicate key"))
                .when(reviewLikeService).like(anyLong(), anyLong());

        // when & then
        mockMvc.perform(post("/api/review-like/review/1/member/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("SERVER_ERROR"));
    }
}