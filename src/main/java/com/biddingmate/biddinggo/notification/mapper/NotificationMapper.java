package com.biddingmate.biddinggo.notification.mapper;

import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import com.biddingmate.biddinggo.notification.model.Notification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper extends IMybatisCRUD<Notification> {
}
