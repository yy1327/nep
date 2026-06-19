package com.neusoft.nepm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.nepm.dto.PageResponseDto;
import com.neusoft.nepm.dto.StatisPageRequestDto;
import com.neusoft.nepm.po.Statistics;

import java.util.List;
import java.util.Map;

public interface StatisticsService extends IService<Statistics> {
    void saveStatistics(Statistics statistics);
    PageResponseDto<Statistics> listPage(StatisPageRequestDto dto);
    Statistics getStatisticsById(Integer id);
    List<Map<String, Object>> listProvinceItemTotalStatis();
    List<Map<String, Object>> listAqiDistributeTotalStatis();
    List<Map<String, Object>> listAqiTrendTotalStatis();
    Integer getAqiCount();
    Integer getAqiGoodCount();
    Integer getProvinceCoverage();
    Integer getCityCoverage();
}
