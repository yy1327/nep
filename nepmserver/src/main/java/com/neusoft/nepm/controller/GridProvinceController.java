package com.neusoft.nepm.controller;

import com.neusoft.nepm.common.Result;
import com.neusoft.nepm.po.GridProvince;
import com.neusoft.nepm.service.GridProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gridProvince")
public class GridProvinceController {

    @Autowired
    private GridProvinceService gridProvinceService;

    @PostMapping("/listGridProvinceAll")
    public Result<List<GridProvince>> listGridProvinceAll() {
        return Result.success(gridProvinceService.listAll());
    }
}
