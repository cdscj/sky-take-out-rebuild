package com.sky.mapper.rider;

import com.sky.pojo.Courier;
import com.sky.pojo.CourierTodayTotalOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RiderMapper {
    /**
     * 查询所有在职骑手
     * @return
     */
    @Select("select * from tb_courier where on_job = 1")
    List<Courier> findAllOnJob();

    /**
     * 查询指定骑手的今日订单数
     * @param courierIds
     * @param today
     * @return
     */
    List<CourierTodayTotalOrder> findTodayCourierOrderNumByCourierId(@Param("courierIds") List<Long> courierIds, @Param("today") String today);

    List<Long> findCourierOrderingLess12ByIds(@Param("newMinOrderNumCourierIds") List<Long> newMinOrderNumCourierIds, @Param("today") String today);

    /**
     * 查询距离商家十公里内的所有骑手
     * @return
     */
    List<Courier> selectNearbyCouriers(@Param("shopLng") double shopLng,
                                       @Param("shopLat") double shopLat,
                                       @Param("distance") double maxDistance);

    /**
     * 根据手机号查询骑手
     * @param telephone
     * @return
     */
    @Select("select * from tb_courier where telephone = #{telephone}")
    Courier selectByTelephone(String telephone);

    /**
     * 根据id查询骑手
     * @param courierId
     * @return
     */
    @Select("select * from tb_courier where id = #{courierId}")
    Courier selectById(Long courierId);
}
