package com.biddingmate.biddinggo.inspection.dto;

import com.biddingmate.biddinggo.common.request.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "검수물품 목록 조회 요청 DTO")
public class InspectionListRequest extends BasePageRequest {
    @Schema(description = "회원 ID", example = "1")
    @NotNull(message = "회원 ID는 필수입니다.")
    @Positive(message = "회원 ID는 1 이상이어야 합니다.")
    private Long memberId;

    @Schema(description = "검수 상태", example = "PENDING", nullable = true)
    private String status;
}
