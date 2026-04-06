package com.biddingmate.biddinggo.notification.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NotificationMapper {


    int countUnreadByReceiverId(@Param("receiverId") Long receiverId);
}
