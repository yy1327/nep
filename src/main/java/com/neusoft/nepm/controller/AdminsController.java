package com.neusoft.nepm.controller;

import com.neusoft.nepm.common.Result;
import com.neusoft.nepm.po.Admins;
import com.neusoft.nepm.service.AdminsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("/updateProfile")
    public Result<Void> updateProfile(@RequestBody Admins admin) {
        try {
            adminsService.updateAdminProfile(admin);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/updatePassword")
    public Result<Void> updatePassword(@RequestBody Map<String, Object> params) {
        try {
            Integer adminId = (Integer) params.get("adminId");
            String oldPwd = (String) params.get("oldPwd");
            String newPwd = (String) params.get("newPwd");
            adminsService.updateAdminPassword(adminId, oldPwd, newPwd);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
