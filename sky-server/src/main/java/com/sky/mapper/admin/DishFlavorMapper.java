package com.sky.mapper.admin;


import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import org.apache.ibatis.annotations.Param;

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入口味数据
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品id删除口味数据
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    /**
     * 根据菜品id批量删除相关联菜品口味数据
     * @param dishIds
     */
    void deleteByDishIds(List<Long> dishIds);

    /**
     * 根据菜品id查询口味
     * @param dishId
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);

    /**
     * 批量根据菜品 ID 查询口味（消除 N+1）
     */
    @Select("<script>select * from dish_flavor where dish_id in " +
            "<foreach item='id' collection='dishIds' open='(' separator=',' close=')'>#{id}</foreach></script>")
    List<DishFlavor> getByDishIds(@Param("dishIds") List<Long> dishIds);
}
