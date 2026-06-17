package com.neusoft.nepm.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("aqi_feedback")
public class AqiFeedback {
    @TableId(type = IdType.AUTO)
    private Integer afId;
    private String afCode;
    private String supervisorId;
    private String afType;
    private String afLevel;
    private String afDesc;
    private String afAddress;
    private BigDecimal afLongitude;
    private BigDecimal afLatitude;
    private Integer afProvinceId;
    private Integer afCityId;
    private String afPhoto;
    private String afState;
    private Integer afAssignGmId;
    private Date afAssignTime;
    private String afAssignDesc;
    private Integer afConfirmGmId;
    private Date afConfirmTime;
    private Integer afConfirmAqi;
    private String afConfirmDesc;
    private Date createTime;
}
