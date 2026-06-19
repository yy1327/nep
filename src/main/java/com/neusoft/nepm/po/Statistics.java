package com.neusoft.nepm.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("statistics")
public class Statistics {
    @TableId(type = IdType.AUTO)
    private Integer statisticsId;
    private Integer afId;
    private Integer gmId;
    private Integer aqiValue;
    private String aqiLevel;
    private BigDecimal pm25Value;
    private BigDecimal pm10Value;
    private BigDecimal so2Value;
    private BigDecimal no2Value;
    private BigDecimal coValue;
    private BigDecimal o3Value;
    private Integer provinceId;
    private Integer cityId;
    private Date statisticsDate;
    private Date createTime;
}
