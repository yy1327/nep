package com.neusoft.nepm.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("admins")
public class Admins {
    @TableId(type = IdType.AUTO)
    private Integer adminId;
    private String adminCode;
    private String adminPwd;
    private String adminName;
    private String adminTel;
    private Date createTime;
}
