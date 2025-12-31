package com.sky.controller.common;

import com.sky.result.Result;
import com.sky.service.common.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController("commonController")
@RequestMapping("/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    @Autowired
    private CommonService commonService;

    /**
     * 查询位置
     * @param tradeNo
     * @param courierId
     * @param expire
     * @return
     */
    @GetMapping("/dispatch/loadRouteInfo")
    @ApiOperation("查询位置")
    public Result loadRouteInfo(Long tradeNo, Long courierId, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime expire){

        return commonService.loadRouteInfo(tradeNo,courierId,expire);
    }

    /**
     * 解析短链
     * @param code
     * @param response
     */
    @GetMapping("/r/{code}")
    public void shortUrl(@PathVariable("code") String code, HttpServletResponse response){

        String url = commonService.findUrlByCode(code);

        try {
            //重定向
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
