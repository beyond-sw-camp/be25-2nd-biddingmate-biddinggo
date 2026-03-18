package com.biddingmate.biddinggo.inspection.mapper;

import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import com.biddingmate.biddinggo.inspection.model.Inspection;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InspectionMapper extends IMybatisCRUD<Inspection> {
}
