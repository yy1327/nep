package com.neusoft.nepm.controller;

import com.neusoft.nepm.common.Result;
import com.neusoft.nepm.po.GridCity;
import com.neusoft.nepm.service.GridCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gridCity")
public class GridCityController {

    @Autowired
    private GridCityService gridCityService;

    @PostMapping("/listGridCityByProvinceId")
    public Result<List<GridCity>> listGridCityByProvinceId(@RequestBody Map<String, Integer> params) {
        Integer provinceId = params.get("provinceId");
        return Result.success(gridCityService.listByProvinceId(provinceId));
    }
}
