package com.neusoft.nepm.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("grid_city")
public class GridCity {
    @TableId(type = IdType.AUTO)
    private Integer cityId;
    private String cityName;
    private Integer provinceId;
}
