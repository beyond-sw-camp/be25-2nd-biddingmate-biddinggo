package com.biddingmate.biddinggo.notification.mapper;

import com.biddingmate.biddinggo.notification.model.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NotificationMapper {

    int countUnreadByReceiverId(@Param("receiverId") Long receiverId);

    int insert(Notification notification);
}
