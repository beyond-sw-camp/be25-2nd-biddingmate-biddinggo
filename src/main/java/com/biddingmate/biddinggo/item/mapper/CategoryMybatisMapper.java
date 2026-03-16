package com.biddingmate.biddinggo.item.mapper;

import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import com.biddingmate.biddinggo.item.model.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMybatisMapper extends IMybatisCRUD<Category> {
    Category findById(Long id);
}
