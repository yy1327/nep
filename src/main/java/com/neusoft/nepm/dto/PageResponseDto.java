package com.neusoft.nepm.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResponseDto<T> {
    private List<T> records;
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
}
