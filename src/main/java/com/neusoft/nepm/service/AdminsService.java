package com.neusoft.nepm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.nepm.po.Admins;

public interface AdminsService extends IService<Admins> {
    Admins getAdminByCodeAndPwd(String adminCode, String adminPwd);
    void updateAdminProfile(Admins admin);
    void updateAdminPassword(Integer adminId, String oldPwd, String newPwd);
}
