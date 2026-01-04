package com.sky.mapper.admin;

import com.sky.pojo.Merchant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MerchantMapper {

    /**
     * 根据id查询商家
     * @param id
     * @return
     */
    @Select("select * from merchant where id=#{id}")
    Merchant selectById(long id);
}
