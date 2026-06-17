package com.neusoft.nepm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nepm.po.Statistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface StatisticsMapper extends BaseMapper<Statistics> {

    @Select("SELECT gp.province_name, " +
            "COUNT(*) as total_count, " +
            "SUM(CASE WHEN s.aqi_level IN ('优','良') THEN 1 ELSE 0 END) as good_count, " +
            "SUM(CASE WHEN s.aqi_level NOT IN ('优','良') THEN 1 ELSE 0 END) as polluted_count " +
            "FROM statistics s " +
            "LEFT JOIN grid_province gp ON s.province_id = gp.province_id " +
            "GROUP BY s.province_id, gp.province_name")
    List<Map<String, Object>> getProvinceItemTotalStatis();

    @Select("SELECT aqi_level as aqiLevel, COUNT(*) as count " +
            "FROM statistics GROUP BY aqi_level")
    List<Map<String, Object>> getAqiDistributeTotalStatis();

    @Select("SELECT DATE_FORMAT(statistics_date, '%Y-%m') as month, " +
            "COUNT(*) as totalCount, AVG(aqi_value) as avgAqi " +
            "FROM statistics GROUP BY DATE_FORMAT(statistics_date, '%Y-%m') ORDER BY month")
    List<Map<String, Object>> getAqiTrendTotalStatis();

    @Select("SELECT COUNT(*) FROM statistics")
    Integer getAqiCount();

    @Select("SELECT COUNT(*) FROM statistics WHERE aqi_level = '良'")
    Integer getAqiGoodCount();

    @Select("SELECT COUNT(DISTINCT province_id) FROM statistics")
    Integer getProvinceCoverage();

    @Select("SELECT COUNT(DISTINCT city_id) FROM statistics")
    Integer getCityCoverage();
}
