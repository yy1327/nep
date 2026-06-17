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

import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl extends ServiceImpl<StatisticsMapper, Statistics> implements StatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Override
    public void saveStatistics(Statistics statistics) {
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

        wrapper.orderByDesc(Statistics::getCreateTime);
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
