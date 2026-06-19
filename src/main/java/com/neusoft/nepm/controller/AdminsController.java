package com.neusoft.nepm.controller;

import com.neusoft.nepm.common.Result;
import com.neusoft.nepm.po.Admins;
import com.neusoft.nepm.service.AdminsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
public class AdminsController {

    @Autowired
    private AdminsService adminsService;

    @PostMapping("/getAdminsByCode")
    public Result<Admins> getAdminsByCode(@RequestBody Admins admin) {
        Admins result = adminsService.getAdminByCodeAndPwd(admin.getAdminCode(), admin.getAdminPwd());
        if (result != null) {
            return Result.success(result);
        }
        return Result.error("账号或密码错误");
    }
}
