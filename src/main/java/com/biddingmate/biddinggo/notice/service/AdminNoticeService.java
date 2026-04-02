package com.biddingmate.biddinggo.notice.service;

import com.biddingmate.biddinggo.notice.dto.NoticeRequest;
import com.biddingmate.biddinggo.notice.dto.NoticeResponse;
import jakarta.validation.Valid;

public interface AdminNoticeService {
    NoticeResponse create(@Valid NoticeRequest request);

    NoticeResponse update(Long noticeId, @Valid NoticeRequest request);

    void delete(Long noticeId);
}
