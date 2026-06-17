package com.neusoft.nepm.dto;

import lombok.Data;

@Data
public class ProvinceItemTotalStatisDto {
    private String provinceName;
    private Integer totalCount;
    private Integer goodCount;
    private Integer pollutedCount;
}
