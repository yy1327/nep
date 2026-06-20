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
        if (member.getGmCode() == null || member.getGmCode().trim().isEmpty()) {
            return Result.error("工号不能为空");
        }
        if (member.getGmPwd() == null || member.getGmPwd().trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        GridMember result = gridMemberService.getGridMemberByCodeAndPwd(member.getGmCode(), member.getGmPwd());
        if (result != null) {
            return Result.success(result);
        }
        return Result.error("工号或密码错误");
    }

    @PostMapping("/updateProfile")
    public Result<Void> updateProfile(@RequestBody GridMember member) {
        try {
            gridMemberService.updateProfile(member);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/updatePassword")
    public Result<Void> updatePassword(@RequestBody Map<String, Object> params) {
        try {
            Integer gmId = (Integer) params.get("gmId");
            String oldPwd = (String) params.get("oldPwd");
            String newPwd = (String) params.get("newPwd");
            gridMemberService.updatePassword(gmId, oldPwd, newPwd);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
