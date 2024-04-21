package com.flab.readnshare.domain.review.controller;

import com.flab.readnshare.ReviewTestFixture;
import com.flab.readnshare.domain.book.dto.BookDto;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.review.dto.SaveReviewRequestDto;
import com.flab.readnshare.domain.review.service.ReviewService;
import com.flab.readnshare.global.common.advice.ApiExceptionAdvice;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReviewApiControllerTest {
    @Mock
    private ReviewService reviewService;
    @InjectMocks
    private ReviewApiController reviewApiController;
    @InjectMocks
    private ApiExceptionAdvice apiExceptionAdvice;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(reviewApiController)
                .setControllerAdvice(apiExceptionAdvice)
                .build();
    }

    @Test
    @DisplayName("독서 기록 등록에 성공한다.")
    void save_success() throws Exception {
        // given
        SaveReviewRequestDto request = ReviewTestFixture.getSaveReviewRequestDto();

        given(reviewService.save(any(SaveReviewRequestDto.class), any(Member.class))).willReturn(1L);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    @DisplayName("독서 기록 등록에 실패한다. (내용 없음)")
    void save_fail_no_content() throws Exception {
        SaveReviewRequestDto request = mock(SaveReviewRequestDto.class);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("입력값을 확인하세요."))
                .andExpect(jsonPath("$.errors[0].field").value("content"))
                .andExpect(jsonPath("$.errors[0].message").value("내용을 입력해주세요."));
    }

    @Test
    @DisplayName("독서 기록 등록에 실패한다. (책 isbn 정보 없음)")
    void save_fail_no_isbn() throws Exception {
        SaveReviewRequestDto request = SaveReviewRequestDto.builder()
                .content("내용")
                .book(BookDto.builder().id(1L).isbn("").title("테스트").build())
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("입력값을 확인하세요."))
                .andExpect(jsonPath("$.errors[0].field").value("book.isbn"))
                .andExpect(jsonPath("$.errors[0].message").value("책 isbn이 없습니다."));
    }

    @Test
    @DisplayName("독서 기록 등록에 실패한다. (책 제목 정보 없음)")
    void save_fail_no_title() throws Exception {
        SaveReviewRequestDto request = SaveReviewRequestDto.builder()
                .content("내용")
                .book(BookDto.builder().id(1L).isbn("1234").title("").build())
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("입력값을 확인하세요."))
                .andExpect(jsonPath("$.errors[0].field").value("book.title"))
                .andExpect(jsonPath("$.errors[0].message").value("책 제목이 없습니다."));
    }

}