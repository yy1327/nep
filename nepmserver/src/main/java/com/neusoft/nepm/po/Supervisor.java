package com.neusoft.nepm.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("supervisor")
public class Supervisor {
    @TableId(type = IdType.AUTO)
    private String supervisorId;
    private String supervisorTel;
    private String supervisorPwd;
    private String supervisorName;
    private String supervisorSex;
    private Integer supervisorAge;
    private String supervisorAddr;
    private Integer provinceId;
    private Integer cityId;
    private Date createTime;
}
