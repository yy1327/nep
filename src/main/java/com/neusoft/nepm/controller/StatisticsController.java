package com.neusoft.nepm.controller;

import com.neusoft.nepm.common.Result;
import com.neusoft.nepm.dto.PageResponseDto;
import com.neusoft.nepm.dto.StatisPageRequestDto;
import com.neusoft.nepm.po.Statistics;
import com.neusoft.nepm.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @PostMapping("/saveStatistics")
    public Result<Void> saveStatistics(@RequestBody Statistics statistics) {
        statisticsService.saveStatistics(statistics);
        return Result.success();
    }

    @PostMapping("/listStatisticsPage")
    public Result<PageResponseDto<Statistics>> listStatisticsPage(@RequestBody StatisPageRequestDto dto) {
        return Result.success(statisticsService.listPage(dto));
    }

    @PostMapping("/getStatisticsById")
    public Result<Statistics> getStatisticsById(@RequestBody Map<String, Integer> params) {
        Integer id = params.get("statisticsId");
        return Result.success(statisticsService.getStatisticsById(id));
    }

    @GetMapping("/listProvinceItemTotalStatis")
    public Result<List<Map<String, Object>>> listProvinceItemTotalStatis() {
        return Result.success(statisticsService.listProvinceItemTotalStatis());
    }

    @GetMapping("/listAqiDistributeTotalStatis")
    public Result<List<Map<String, Object>>> listAqiDistributeTotalStatis() {
        return Result.success(statisticsService.listAqiDistributeTotalStatis());
    }

    @GetMapping("/listAqiTrendTotalStatis")
    public Result<List<Map<String, Object>>> listAqiTrendTotalStatis() {
        return Result.success(statisticsService.listAqiTrendTotalStatis());
    }

    @GetMapping("/getAqiCount")
    public Result<Integer> getAqiCount() {
        return Result.success(statisticsService.getAqiCount());
    }

    @GetMapping("/getAqiGoodCount")
    public Result<Integer> getAqiGoodCount() {
        return Result.success(statisticsService.getAqiGoodCount());
    }

    @GetMapping("/getProvinceCoverage")
    public Result<Integer> getProvinceCoverage() {
        return Result.success(statisticsService.getProvinceCoverage());
    }

    @GetMapping("/getCityCoverage")
    public Result<Integer> getCityCoverage() {
        return Result.success(statisticsService.getCityCoverage());
    }
}
