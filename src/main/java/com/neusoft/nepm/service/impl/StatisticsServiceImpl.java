package com.neusoft.nepm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.nepm.dto.PageResponseDto;
import com.neusoft.nepm.dto.StatisPageRequestDto;
import com.neusoft.nepm.mapper.StatisticsMapper;
import com.neusoft.nepm.po.Statistics;
import com.neusoft.nepm.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl extends ServiceImpl<StatisticsMapper, Statistics> implements StatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Override
    public void saveStatistics(Statistics statistics) {
        // 1. 获取前端传来的 PM2.5 和 PM10 数据
        // 注意：这里要处理可能为 null 的情况
        double pm25 = statistics.getPm25Value() != null ? statistics.getPm25Value().doubleValue() : 0.0;
        double pm10 = statistics.getPm10Value() != null ? statistics.getPm10Value().doubleValue() : 0.0;

        // 2. 计算 AQI 值
        // 这里使用你前端原本的逻辑：(PM2.5 + PM10) / 2
        // 实际项目中 AQI 计算通常更复杂，这里为了保持一致性沿用你的逻辑
        int aqiValue = (int) Math.round((pm25 + pm10) / 2.0);

        // 3. 计算 AQI 等级
        String aqiLevel = "优"; // 默认值
        if (aqiValue > 300) {
            aqiLevel = "严重污染";
        } else if (aqiValue > 200) {
            aqiLevel = "重度污染";
        } else if (aqiValue > 150) {
            aqiLevel = "中度污染";
        } else if (aqiValue > 100) {
            aqiLevel = "轻度污染";
        } else if (aqiValue > 50) {
            aqiLevel = "良";
        }

        // 4. 将计算结果设置回对象
        statistics.setAqiValue(aqiValue);
        statistics.setAqiLevel(aqiLevel);

        // 5. 设置统计日期（如果表结构需要自动记录当前时间）
        statistics.setStatisticsDate(new Date());

        // 6. 执行保存
        save(statistics);
    }

    @Override
    public PageResponseDto<Statistics> listPage(StatisPageRequestDto dto) {
        Page<Statistics> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<Statistics> wrapper = new LambdaQueryWrapper<>();

        if (dto.getProvinceId() != null) {
            wrapper.eq(Statistics::getProvinceId, dto.getProvinceId());
        }
        if (dto.getCityId() != null) {
            wrapper.eq(Statistics::getCityId, dto.getCityId());
        }
        if (dto.getAqiLevel() != null && !dto.getAqiLevel().isEmpty()) {
            wrapper.eq(Statistics::getAqiLevel, dto.getAqiLevel());
        }

        wrapper.orderByAsc(Statistics::getCreateTime);
        page = page(page, wrapper);

        PageResponseDto<Statistics> response = new PageResponseDto<>();
        response.setRecords(page.getRecords());
        response.setTotal(page.getTotal());
        response.setPageNum(dto.getPageNum());
        response.setPageSize(dto.getPageSize());
        return response;
    }

    @Override
    public Statistics getStatisticsById(Integer id) {
        return getById(id);
    }

    @Override
    public List<Map<String, Object>> listProvinceItemTotalStatis() {
        return statisticsMapper.getProvinceItemTotalStatis();
    }

    @Override
    public List<Map<String, Object>> listAqiDistributeTotalStatis() {
        return statisticsMapper.getAqiDistributeTotalStatis();
    }

    @Override
    public List<Map<String, Object>> listAqiTrendTotalStatis() {
        return statisticsMapper.getAqiTrendTotalStatis();
    }

    @Override
    public Integer getAqiCount() {
        return statisticsMapper.getAqiCount();
    }

    @Override
    public Integer getAqiGoodCount() {
        return statisticsMapper.getAqiGoodCount();
    }

    @Override
    public Integer getProvinceCoverage() {
        return statisticsMapper.getProvinceCoverage();
    }

    @Override
    public Integer getCityCoverage() {
        return statisticsMapper.getCityCoverage();
    }
}
