package com.biddingmate.biddinggo.common.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * 공용 페이징 요청 DTO.
 *
 * <p>컨트롤러의 query parameter를 바인딩해 사용하며,
 * 공통 페이징 요소인 page/size와 JPA/MyBatis 변환 헬퍼만 제공한다.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "공용 페이징 요청 DTO")
public class PagingRequest {

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 100;

    @Builder.Default
    @Schema(description = "0부터 시작하는 페이지 번호", example = "0", defaultValue = "0")
    @NotNull(message = "페이지 번호는 필수입니다.")
    @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
    private Integer page = DEFAULT_PAGE;

    @Builder.Default
    @Schema(description = "페이지 크기", example = "20", defaultValue = "20")
    @NotNull(message = "페이지 크기는 필수입니다.")
    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
    @Max(value = MAX_SIZE, message = "페이지 크기는 100 이하여야 합니다.")
    private Integer size = DEFAULT_SIZE;

    @Builder.Default
    @Schema(description = "정렬 옵션", example = "latest", defaultValue = "latest")
    private String sort = "latest";

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }

    public int getOffset() {
        return page * size;
    }

    public int getLimit() {
        return size;
    }
}
