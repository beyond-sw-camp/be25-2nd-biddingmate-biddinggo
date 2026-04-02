package com.biddingmate.biddinggo.notice.service;

import com.biddingmate.biddinggo.notice.dto.NoticeRequest;
import com.biddingmate.biddinggo.notice.dto.NoticeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminNoticeServiceImpl implements AdminNoticeService {


    @Override
    public NoticeResponse create(NoticeRequest request) {
        return null;
    }

    @Override
    public NoticeResponse update(Long noticeId, NoticeRequest request) {
        return null;
    }

    @Override
    public void delete(Long noticeId) {

    }
}
