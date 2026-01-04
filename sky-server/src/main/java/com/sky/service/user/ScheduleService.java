package com.sky.service.user;

import com.sky.result.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ScheduleService {

    Result upload(MultipartFile file) throws IOException;
}
