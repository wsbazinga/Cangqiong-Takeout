package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量新增口味信息
     * @param flavorList
     */
    void saveBatch(List<DishFlavor> flavorList);

    void deleteBatchByDishIds(@Param("dishIds") List<Long> ids);
}
