package com.neusoft.nepm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nepm.po.Supervisor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SupervisorMapper extends BaseMapper<Supervisor> {

    @Select("SELECT * FROM supervisor WHERE supervisor_tel = #{tel} LIMIT 1")
    Supervisor getSupervisorByTel(String tel);

    @Select("SELECT * FROM supervisor WHERE (supervisor_tel = #{tel} OR supervisor_id = #{tel}) AND supervisor_pwd = #{pwd} LIMIT 1")
    Supervisor getSupervisorByTelAndPwd(String tel, String pwd);

    @Select("SELECT supervisor_id FROM supervisor ORDER BY supervisor_id DESC LIMIT 1")
    String getLatestSupervisorId();
}
