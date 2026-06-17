package com.neusoft.nepm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nepm.po.AqiFeedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AqiFeedbackMapper extends BaseMapper<AqiFeedback> {

    @Select("SELECT af.*, s.supervisor_name, gp.province_name, gc.city_name, gm.gm_name " +
            "FROM aqi_feedback af " +
            "LEFT JOIN supervisor s ON af.supervisor_id = s.supervisor_id " +
            "LEFT JOIN grid_province gp ON af.af_province_id = gp.province_id " +
            "LEFT JOIN grid_city gc ON af.af_city_id = gc.city_id " +
            "LEFT JOIN grid_member gm ON af.af_assign_gm_id = gm.gm_id " +
            "WHERE af.af_id = #{afId}")
    Map<String, Object> getFeedbackDetailById(Integer afId);

    @Select("SELECT af.*, s.supervisor_name, gp.province_name, gc.city_name " +
            "FROM aqi_feedback af " +
            "LEFT JOIN supervisor s ON af.supervisor_id = s.supervisor_id " +
            "LEFT JOIN grid_province gp ON af.af_province_id = gp.province_id " +
            "LEFT JOIN grid_city gc ON af.af_city_id = gc.city_id " +
            "WHERE af.supervisor_id = #{supervisorId} ORDER BY af.create_time DESC")
    List<Map<String, Object>> getFeedbackBySupervisorId(String supervisorId);
}
