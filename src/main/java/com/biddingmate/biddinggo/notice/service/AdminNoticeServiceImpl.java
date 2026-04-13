package com.biddingmate.biddinggo.notice.service;

import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.member.mapper.MemberMapper;
import com.biddingmate.biddinggo.notice.dto.CreateNoticeRequest;
import com.biddingmate.biddinggo.notice.dto.NoticeResponse;
import com.biddingmate.biddinggo.notice.dto.UpdateNoticeRequest;
import com.biddingmate.biddinggo.notice.mapper.NoticeMapper;
import com.biddingmate.biddinggo.notice.vo.Notice;
import com.biddingmate.biddinggo.notification.model.NotificationType;
import com.biddingmate.biddinggo.notification.service.NotificationPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminNoticeServiceImpl implements AdminNoticeService {

    private static final String ACTIVE = "ACTIVE";
    private static final String DELETED = "DELETED";

    private final NoticeMapper noticeMapper;
    private final NotificationPublisher notificationPublisher;
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public NoticeResponse create(CreateNoticeRequest request) {
        Notice notice = Notice
                .builder()
                .title(request.getTitle())
                .content(request.getContent())
                .status(ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        int inserted = noticeMapper.insert(notice);
        if (inserted <= 0){
            throw new CustomException(ErrorType.NOTICE_CREATE_FAIL);
        }

        publishNoticeNotificationToAllActiveMembers(notice);

        return toResponse(notice);
    }

    @Override
    @Transactional
    public NoticeResponse update(Long noticeId, UpdateNoticeRequest request) {
        Notice found = noticeMapper.findById(noticeId);
        if (found == null || DELETED.equals(found.getStatus())) {
            throw new CustomException(ErrorType.NOTICE_NOT_FOUND);
        }

        Notice toUpdate = Notice.builder()
                .id(noticeId)
                .title(request.getTitle())
                .content(request.getContent())
                .status(found.getStatus())
                .createdAt(found.getCreatedAt())
                .build();

        int updated = noticeMapper.update(toUpdate);
        if (updated <= 0) {
            throw new CustomException(ErrorType.NOTICE_UPDATE_FAIL);
        }

        Notice updatedNotice = noticeMapper.findById(noticeId);

        return toResponse(updatedNotice);
    }

    @Override
    @Transactional
    public void delete(Long noticeId) {
        Notice found = noticeMapper.findById(noticeId);
        if (found == null || DELETED.equals(found.getStatus())) {
            throw new CustomException(ErrorType.NOTICE_NOT_FOUND);
        }

        Notice toDelete = Notice.builder()
                .id(noticeId)
                .title(found.getTitle())
                .content(found.getContent())
                .status(DELETED)
                .createdAt(found.getCreatedAt())
                .build();

        int updated = noticeMapper.update(toDelete);
        if (updated <= 0) {
            throw new CustomException(ErrorType.NOTICE_DELETE_FAIL);
        }


    }

    private NoticeResponse toResponse(Notice notice) {
        return NoticeResponse
                .builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .status(notice.getStatus())
                .createdAt(notice.getCreatedAt())
                .build();
    }

    private void publishNoticeNotificationToAllActiveMembers(Notice notice) {

        List<Long> memberIds = memberMapper.findAllActiveMemberIds();
        if(memberIds == null || memberIds.isEmpty()) {
            return;
        }

        String title = notice.getTitle() == null ? "" : notice.getTitle().trim();
        String body = notice.getContent() == null ? "" : notice.getContent().trim();
        String content = title.isEmpty() ? body : "[공지] " + title + "\n" + body;

        for (Long memberId : memberIds) {
            notificationPublisher.publishNotification(
                    memberId,
                    NotificationType.ADMIN_NOTICE,
                    content,
                    null
            );
        }
    }
}
