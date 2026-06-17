package com.neusoft.nepm.dto;

import lombok.Data;

@Data
public class AfPageRequestDto {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String afCode;
    private String afState;
    private String supervisorName;
    private String afAddress;
}
