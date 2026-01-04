package com.sky.controller.user;


import com.sky.result.Result;
import com.sky.service.user.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user/schedule")
@Api(tags = "课程表功能")
@Slf4j
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    ///上传课程表
    @Operation(summary = "上传课程表")
    @PostMapping(value ="/upload", consumes = "multipart/form-data" )
    public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("上传课程表");
        return scheduleService.upload(file);
    }

}
