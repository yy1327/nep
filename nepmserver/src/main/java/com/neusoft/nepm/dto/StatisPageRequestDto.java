package com.neusoft.nepm.dto;

import lombok.Data;

@Data
public class StatisPageRequestDto {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Integer provinceId;
    private Integer cityId;
    private String aqiLevel;
}
