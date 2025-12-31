package com.sky.service.rider.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.sky.dto.CourierTradeQueryDto;
import com.sky.dto.UserLoginDTO;
import com.sky.exception.CourierNotExistException;
import com.sky.exception.CourierTradeOrderNotExistException;
import com.sky.exception.ParameterIsNullException;
import com.sky.mapper.common.ShotUriMapper;
import com.sky.mapper.rider.RiderMapper;
import com.sky.mapper.rider.RiderTradeOrderMapper;
import com.sky.mapper.user.OrderMapper;
import com.sky.pojo.Courier;
import com.sky.pojo.CourierTradeOrder;
import com.sky.pojo.Orders;
import com.sky.pojo.ShortUrl;
import com.sky.result.Result;
import com.sky.service.rider.RiderService;
import com.sky.shoturi.ShortUriGenerator;
import com.sky.vo.CourierTradeOrderDetailItemVO;
import com.sky.vo.CourierTradeOrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RiderServiceImpl implements RiderService {
    @Autowired
    private RiderMapper courierMapper;
    @Autowired
    private RiderTradeOrderMapper riderTradeOrderMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ShortUriGenerator shortUriGenerator;

    @Autowired
    private ShotUriMapper shotUriMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    public static final Set<String> LOGIN_WHITE_LIST = Set.of(
            "17633500448",  // 审核账号1
            "13899991117"   // 审核账号2
    );

    /**
     * 骑手登录
     *
     * @param dto
     * @return
     */
    @Override
    public Result login(UserLoginDTO dto) {
        //验参
        if (dto == null) {
            throw new ParameterIsNullException("缺少请求参数");
        }
        Courier courier = courierMapper.selectByTelephone(dto.getTelephone());
        if (ObjectUtil.isEmpty(courier)) {
            throw new CourierNotExistException("快递员不存在，请检查手机号");
        }

        return Result.success(courier);
    }

    /**
     * 骑手接单
     *
     * @param courierId
     * @param tradeNo
     * @return
     */
    @Override
    @Transactional
    public Result receivingOrder(Long courierId, Long tradeNo) {
        //健壮性判断
        if (courierId == null || tradeNo == null) {
            throw new ParameterIsNullException("参数不合法");
        }
        // 修改运单的状态
        CourierTradeOrder courierTradeOrder = riderTradeOrderMapper.selectByTradeNo(tradeNo);
        if (ObjectUtil.isEmpty(courierTradeOrder)) {
            throw new CourierTradeOrderNotExistException("找不到运单");
        }
        riderTradeOrderMapper.updateStatusByTradeNo(tradeNo, CourierTradeOrder.DELIVORING);
        // 修改订单的状态
        orderMapper.updateStatusByNumber(courierTradeOrder.getOrderNumber(), Orders.TOBEPICKEDUP);
        // 发送短信给用户
        sendMessageToCustomer(courierTradeOrder);
        return Result.success();
    }

    private void sendMessageToCustomer(CourierTradeOrder tradeOrder) {
        //生成长连接
        String fullUrl = shortUriGenerator.getFullUrl(tradeOrder.getTradeNo(), tradeOrder.getCourierId(), tradeOrder.getCustomerExpectedDeliveryTime());
        System.out.println("------完整连接地址：" + fullUrl);
        //短编码
        String code = "";
        while (true) {
            code = shortUriGenerator.creatKey();
            //判断是否重复
            long index = 0l;
            int hashCode = code.hashCode();
            if (hashCode < 0) {
                index = 2147483648l + Math.abs(hashCode);
            } else {
                index = hashCode;
            }
            Boolean res = redisTemplate.opsForValue().getBit("short_code", index);
            if (!res) {
                //设置bit位
                redisTemplate.opsForValue().setBit("short_code", index, true);
                break;
            }
        }
        //保存入库
        String shortUrl = shortUriGenerator.getShortUrl(fullUrl);
        ShortUrl shotUri = ShortUrl.builder()
                .shortCode(code)
                .shortUrl(shortUrl)
                .fullUrl(fullUrl)
                .expireTime(tradeOrder.getCustomerExpectedDeliveryTime())
                .tradeNo(tradeOrder.getTradeNo())
                .build();

        shotUriMapper.save(shotUri);
        ///短链通过短信给到用户 , ps:附带连接的短信审核要求较高，个人无法申请
        //alismsTemplate.sendMessage("15501521617",shortUrl);
        System.out.println("--------发送短信给客户,地址:" + shortUrl);
    }

    /**
     * 根据id查找骑手
     *
     * @param id
     * @return
     */
    @Override
    public Result<Courier> findById(Long id) {
        Courier courier = courierMapper.selectById(id);
        return Result.success(courier);
    }

    /**
     * 骑手的月度统计
     *
     * @param dto
     * @return
     */
    @Override
    public Result<CourierTradeOrderDetailVO> findCourierMonthTradeOrderInfo(CourierTradeQueryDto dto) {
        Long courierId = dto.getCourierId();
        // 获取当前月的第一天和最后一天
        LocalDate today = LocalDateTimeUtil.parseDate(dto.getDate() + "-01", "yyyy-MM-dd");
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.plusMonths(1).withDayOfMonth(1).minusDays(1);
        // 获取日期列表
        List<String> dateList = getDateList(firstDayOfMonth, lastDayOfMonth);
        // 当月完成的订单量
        Integer completedTradeOrderCountByDate = riderTradeOrderMapper.getTradeOrderCountByDateAndCount(courierId, firstDayOfMonth, lastDayOfMonth, CourierTradeOrder.TRADE_COMPLETE);
        // 当月取消的订单量
        Integer canceledTradeOrderCountByDate = riderTradeOrderMapper.getTradeOrderCountByDateAndCount(courierId, firstDayOfMonth, lastDayOfMonth, CourierTradeOrder.CANCLE_TRADE);
        // 获取平均配送时间
        Double avgOfCompletedTime = riderTradeOrderMapper.getAvgOfCompletedTime(courierId, firstDayOfMonth, lastDayOfMonth);
        // 获取配送准时率
        Integer tradeOrderCountOnTimeRate = riderTradeOrderMapper.getTradeOrderOnTimeRate(courierId, firstDayOfMonth, lastDayOfMonth);
        Double tradeOrderOnTimeRate = tradeOrderCountOnTimeRate * 1.0 / completedTradeOrderCountByDate;
        // 获取当月每天的订单完成量
        Map<String, Object> completeMap = riderTradeOrderMapper.selectEveryDayOrdersCountByDateAndStatus(courierId, firstDayOfMonth, lastDayOfMonth, dateList, CourierTradeOrder.TRADE_COMPLETE);
        // 获取当月每天取消的订单量
        Map<String, Object> cancelMap = riderTradeOrderMapper.selectEveryDayOrdersCountByDateAndStatus(courierId, firstDayOfMonth, lastDayOfMonth, dateList, CourierTradeOrder.CANCLE_TRADE);
        // 封装运单明细对象
        List<CourierTradeOrderDetailItemVO> courierTradeOrderDetailItemVOS = new ArrayList<>();
        dateList.forEach(date -> {
            CourierTradeOrderDetailItemVO courierTradeOrderDetailItemVO = CourierTradeOrderDetailItemVO.builder()
                    .tradeOrderDate(date)
                    .tradeOrdersCompleted(Integer.valueOf(completeMap.get(date).toString()))
                    .tradeOrdersCancelled(Integer.valueOf(cancelMap.get(date).toString()))
                    .build();
            courierTradeOrderDetailItemVOS.add(courierTradeOrderDetailItemVO);
        });
        // 封装返回数据
        CourierTradeOrderDetailVO courierTradeOrderDetailVO = CourierTradeOrderDetailVO.builder()
                .tradeOrderAverageDuration(avgOfCompletedTime)
                .tradeOrdersCancelled(canceledTradeOrderCountByDate)
                .tradOrderOnTimeRate(tradeOrderOnTimeRate)
                .tradeOrderDetails(courierTradeOrderDetailItemVOS)
                .tradeOrdersCompleted(completedTradeOrderCountByDate)
                .build();

        return Result.success(courierTradeOrderDetailVO);
    }

    /**
     * 根据起始日期和结束日期封装每一天日期的集合
     *
     * @param begin
     * @param end
     * @return
     */
    private static List<String> getDateList(LocalDate begin, LocalDate end) {
        // 封装日期集合
        LocalDate today = begin;
        List<String> dateList = new ArrayList<>();
        while (today.isBefore(end.plusDays(1))) {
            dateList.add(today.toString());
            today = today.plusDays(1);
        }
        return dateList;
    }
}
