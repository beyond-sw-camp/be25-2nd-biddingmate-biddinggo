package com.biddingmate.biddinggo.common.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
/**
 * 공용 페이징 응답 DTO.
 *
 * <p>페이지 번호는 0부터 시작하는 값을 기준으로 사용한다.</p>
 * <p>JPA에서는 {@code Page<T>}를 그대로 넘겨 생성할 수 있고,
 * MyBatis처럼 {@code List<T> + totalElements} 형태로 조회한 경우에도 생성할 수 있다.</p>
 */
public class PageResponse<T> {

    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final int numberOfElements;
    private final boolean hasNext;
    private final boolean hasPrevious;
    private final boolean first;
    private final boolean last;
    private final boolean empty;

    /**
     * Spring Data JPA의 {@code Page<T>}를 그대로 응답 DTO로 변환할 때 사용한다.
     */
    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.numberOfElements = page.getNumberOfElements();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.empty = page.isEmpty();
    }

    /**
     * MyBatis 등에서 목록과 전체 개수를 따로 조회한 경우 사용한다.
     *
     * @param content 현재 페이지 데이터 목록
     * @param page 0-based 페이지 번호
     * @param size 페이지 크기
     * @param totalElements 전체 데이터 개수
     */
    public PageResponse(List<T> content, int page, int size, long totalElements) {
        if (page < 0) {
            throw new IllegalArgumentException("page must be greater than or equal to 0");
        }

        if (size < 1) {
            throw new IllegalArgumentException("size must be greater than or equal to 1");
        }

        if (totalElements < 0) {
            throw new IllegalArgumentException("totalElements must be greater than or equal to 0");
        }

        this.content = content == null ? List.of() : List.copyOf(content);
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalElements == 0 ? 0 : (int) Math.ceil((double) totalElements / size);
        this.numberOfElements = this.content.size();
        this.hasNext = page + 1 < this.totalPages;
        this.hasPrevious = page > 0;
        this.first = page == 0;
        this.last = this.totalPages == 0 || page >= this.totalPages - 1;
        this.empty = this.content.isEmpty();
    }
}
