package com.biddingmate.biddinggo.item.mapper;

import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import com.biddingmate.biddinggo.item.model.AuctionItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuctionItemMapper extends IMybatisCRUD<AuctionItem> {
}
