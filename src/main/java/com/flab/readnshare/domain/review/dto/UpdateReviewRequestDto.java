package com.flab.readnshare.domain.review.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateReviewRequestDto {
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;

    @Builder
    public UpdateReviewRequestDto(String content) {
        this.content = content;
    }

}
