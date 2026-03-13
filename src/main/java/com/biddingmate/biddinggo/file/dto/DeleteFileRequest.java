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
@Schema(description = "R2 임시 파일 삭제 요청 DTO")
public class DeleteFileRequest {
    @Schema(description = "삭제할 R2 파일 key", example = "items/2026/03/13/uuid.jpg")
    private String fileKey;
}
