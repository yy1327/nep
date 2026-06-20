package com.neusoft.nepm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nepm.po.AqiFeedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AqiFeedbackMapper extends BaseMapper<AqiFeedback> {

    @Select("SELECT af_code FROM aqi_feedback ORDER BY create_time DESC LIMIT 1")
    String getLatestAfCode();

    @Select("SELECT af.*, s.supervisor_name, s.supervisor_tel, gp.province_name, gc.city_name, gm.gm_name " +
            "FROM aqi_feedback af " +
            "LEFT JOIN supervisor s ON af.supervisor_id = s.supervisor_id OR af.supervisor_id = s.supervisor_tel " +
            "LEFT JOIN grid_province gp ON af.af_province_id = gp.province_id " +
            "LEFT JOIN grid_city gc ON af.af_city_id = gc.city_id " +
            "LEFT JOIN grid_member gm ON af.af_assign_gm_id = gm.gm_id " +
            "WHERE af.af_id = #{afId}")
    Map<String, Object> getFeedbackDetailById(Integer afId);

    @Select("SELECT af.*, s.supervisor_name, s.supervisor_tel, gp.province_name, gc.city_name " +
            "FROM aqi_feedback af " +
            "LEFT JOIN supervisor s ON af.supervisor_id = s.supervisor_id OR af.supervisor_id = s.supervisor_tel " +
            "LEFT JOIN grid_province gp ON af.af_province_id = gp.province_id " +
            "LEFT JOIN grid_city gc ON af.af_city_id = gc.city_id " +
            "WHERE af.supervisor_id = #{supervisorId} ORDER BY af.create_time DESC")
    List<Map<String, Object>> getFeedbackBySupervisorId(String supervisorId);

    @Select("SELECT af.*, s.supervisor_name, s.supervisor_tel " +
            "FROM aqi_feedback af " +
            "LEFT JOIN supervisor s ON af.supervisor_id = s.supervisor_id OR af.supervisor_id = s.supervisor_tel " +
            "WHERE af.af_code LIKE CONCAT('%',#{afCode},'%') " +
            "AND (#{afState} = '' OR af.af_state = #{afState}) " +
            "AND (#{supervisorName} = '' OR af.supervisor_id LIKE CONCAT('%',#{supervisorName},'%') OR s.supervisor_tel LIKE CONCAT('%',#{supervisorName},'%')) " +
            "AND af.af_address LIKE CONCAT('%',#{afAddress},'%') " +
            "ORDER BY af.create_time DESC LIMIT #{limit} OFFSET #{offset}")
    List<Map<String, Object>> listPageWithJoin(
            @Param("afCode") String afCode,
            @Param("afState") String afState,
            @Param("supervisorName") String supervisorName,
            @Param("afAddress") String afAddress,
            @Param("limit") int limit,
            @Param("offset") int offset);
}
