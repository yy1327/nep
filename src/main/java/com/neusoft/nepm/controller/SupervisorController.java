package com.neusoft.nepm.controller;

import com.neusoft.nepm.common.Result;
import com.neusoft.nepm.po.Supervisor;
import com.neusoft.nepm.service.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supervisor")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

    @PostMapping("/getSupervisorById")
    public Result<Supervisor> getSupervisorById(@RequestBody Supervisor supervisor) {
        Supervisor result = supervisorService.getSupervisorById(supervisor.getSupervisorTel());
        if (result != null) {
            return Result.success(result);
        }
        return Result.error("手机号未注册");
    }

    @PostMapping("/saveSupervisor")
    public Result<Void> saveSupervisor(@RequestBody Supervisor supervisor) {
        if (supervisor.getSupervisorTel() == null || supervisor.getSupervisorTel().trim().isEmpty()) {
            return Result.error("手机号不能为空");
        }
        if (supervisor.getSupervisorPwd() == null || supervisor.getSupervisorPwd().trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        if (supervisor.getSupervisorName() == null || supervisor.getSupervisorName().trim().isEmpty()) {
            return Result.error("姓名不能为空");
        }
        try {
            supervisorService.saveSupervisor(supervisor);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("注册失败: " + e.getMessage());
        }
    }

    @PostMapping("/getSupervisorByIdByPass")
    public Result<Supervisor> getSupervisorByIdByPass(@RequestBody Supervisor supervisor) {
        if (supervisor.getSupervisorTel() == null || supervisor.getSupervisorTel().trim().isEmpty()) {
            return Result.error("手机号/监督员ID不能为空");
        }
        if (supervisor.getSupervisorPwd() == null || supervisor.getSupervisorPwd().trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        Supervisor result = supervisorService.getSupervisorByIdAndPwd(supervisor.getSupervisorTel(), supervisor.getSupervisorPwd());
        if (result != null) {
            return Result.success(result);
        }
        return Result.error("手机号/监督员ID或密码错误");
    }
}
