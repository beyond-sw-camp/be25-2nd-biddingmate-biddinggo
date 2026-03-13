package com.biddingmate.biddinggo.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "R2 presigned upload URL 발급 요청 DTO")
public class CreatePresignedUploadUrlRequest {
    @Schema(description = "원본 파일명", example = "main.jpg")
    private String originalFilename;

    @Schema(description = "콘텐츠 타입", example = "image/jpeg")
    private String contentType;
}
