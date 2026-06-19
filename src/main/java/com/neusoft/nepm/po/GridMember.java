package com.neusoft.nepm.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("grid_member")
public class GridMember {
    @TableId(type = IdType.AUTO)
    private Integer gmId;
    private String gmCode;
    private String gmPwd;
    private String gmName;
    private String gmTel;
    private Integer gmProvinceId;
    private Integer gmCityId;
    private String gmStatus;
    private Date createTime;
}
