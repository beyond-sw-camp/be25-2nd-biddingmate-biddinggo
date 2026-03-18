package com.biddingmate.biddinggo.inspection.mapper;

import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import com.biddingmate.biddinggo.inspection.model.Inspection;
import com.biddingmate.biddinggo.inspection.model.InspectionStatus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InspectionMapper extends IMybatisCRUD<Inspection> {
    int updateShippingInfo(
            @Param("inspectionId") Long inspectionId,
            @Param("carrier") String carrier,
            @Param("trackingNumber") String trackingNumber,
            @Param("currentStatus") InspectionStatus currentStatus
    );
}
