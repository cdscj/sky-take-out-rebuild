package com.sky.mapper.common;

import com.sky.pojo.ShortUrl;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ShotUriMapper {

    /**
     * 插入短链长链表
     * @param shortUrl
     */
    @Insert("INSERT INTO tb_short_url_info " +
            "(id, short_url, full_url, short_code, expire_time, trade_no) " +
            "VALUES " +
            "(null, #{shortUrl}, #{shortUrl}, #{fullUrl}, #{shortCode}, #{expireTime}, #{tradeNo})")
    void save(ShortUrl shortUrl);

    @Select("select full_url from tb_short_url_info where short_code = #{code}")
    String selectFullUrlByCode(String code);
}
