package com.neusoft.nepm.controller;

import com.neusoft.nepm.common.Result;
import com.neusoft.nepm.po.Aqi;
import com.neusoft.nepm.service.AqiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aqi")
public class AqiController {

    @Autowired
    private AqiService aqiService;

    @GetMapping("/listAqiAll")
    public Result<List<Aqi>> listAqiAll() {
        return Result.success(aqiService.listAqiAll());
    }
}
