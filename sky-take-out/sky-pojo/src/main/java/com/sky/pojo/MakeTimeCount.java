package com.sky.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MakeTimeCount implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer number;
    private Integer dishMakeTime;
    private Integer setmealMakeTime;
}
