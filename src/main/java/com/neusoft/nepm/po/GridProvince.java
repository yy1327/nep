package com.neusoft.nepm.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("grid_province")
public class GridProvince {
    @TableId(type = IdType.AUTO)
    private Integer provinceId;
    private String provinceName;
}
