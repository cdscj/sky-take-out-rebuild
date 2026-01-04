package com.sky.service.user.impl;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.service.user.ScheduleService;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private AliOssUtil aliOssUtil;

    ///上传课程表文件
    public Result upload(MultipartFile file){

        ///检查参数是否缺失
        if(file.isEmpty()){
            return Result.error("参数为空");
        }

        ///获取文件名，并判断格式是否正确
        String OriginalFilename = file.getOriginalFilename();
        String fileExtension = OriginalFilename.substring(OriginalFilename.lastIndexOf(".") + 1).toLowerCase();
        if(!fileExtension.equals("xls")){
            return Result.error("文件格式错误");
        }

        try {
            ///构造新的文件名称
            String objectName = UUID.randomUUID().toString() + fileExtension;

            ///文件请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);

            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}",e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
