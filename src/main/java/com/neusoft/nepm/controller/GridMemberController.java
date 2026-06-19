package com.neusoft.nepm.controller;

import com.neusoft.nepm.common.Result;
import com.neusoft.nepm.po.GridMember;
import com.neusoft.nepm.service.GridMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gridMember")
public class GridMemberController {

    @Autowired
    private GridMemberService gridMemberService;

    @PostMapping("/listGridMemberByProvinceId")
    public Result<List<GridMember>> listGridMemberByProvinceId(@RequestBody Map<String, Integer> params) {
        Integer provinceId = params.get("provinceId");
        Integer cityId = params.get("cityId");
        return Result.success(gridMemberService.listByProvinceId(provinceId, cityId));
    }

    @PostMapping("/getGridMemberByCodeByPass")
    public Result<GridMember> getGridMemberByCodeByPass(@RequestBody GridMember member) {
        GridMember result = gridMemberService.getGridMemberByCodeAndPwd(member.getGmCode(), member.getGmPwd());
        if (result != null) {
            return Result.success(result);
        }
        return Result.error("工号或密码错误");
    }
}
