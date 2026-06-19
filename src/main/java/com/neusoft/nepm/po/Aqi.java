package com.neusoft.nepm.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("aqi")
public class Aqi {
    @TableId(type = IdType.AUTO)
    private Integer aqiId;
    private String aqiLevel;
    private Integer aqiValueMin;
    private Integer aqiValueMax;
    private String aqiColor;
    private String aqiDesc;
}
