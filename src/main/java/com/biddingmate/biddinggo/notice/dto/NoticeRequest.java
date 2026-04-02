package com.biddingmate.biddinggo.notice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeRequest {

    @NotBlank(message = "제목은 필수 입니다.")
    @Size(max = 20, message = "공지 제목은 20자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "공지 내용은 필수 입니다.")
    private String content;
}
