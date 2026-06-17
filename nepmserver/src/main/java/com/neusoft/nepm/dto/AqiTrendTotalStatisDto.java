package com.neusoft.nepm.dto;

import lombok.Data;

@Data
public class AqiTrendTotalStatisDto {
    private String month;
    private Integer totalCount;
    private Double avgAqi;
}
