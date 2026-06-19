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
        supervisorService.saveSupervisor(supervisor);
        return Result.success();
    }

    @PostMapping("/getSupervisorByIdByPass")
    public Result<Supervisor> getSupervisorByIdByPass(@RequestBody Supervisor supervisor) {
        Supervisor result = supervisorService.getSupervisorByIdAndPwd(supervisor.getSupervisorTel(), supervisor.getSupervisorPwd());
        if (result != null) {
            return Result.success(result);
        }
        return Result.error("手机号或密码错误");
    }
}
