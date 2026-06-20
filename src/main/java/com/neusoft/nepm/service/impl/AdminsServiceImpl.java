package com.neusoft.nepm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.nepm.mapper.AdminsMapper;
import com.neusoft.nepm.po.Admins;
import com.neusoft.nepm.service.AdminsService;
import org.springframework.stereotype.Service;

@Service
public class AdminsServiceImpl extends ServiceImpl<AdminsMapper, Admins> implements AdminsService {

    @Override
    public Admins getAdminByCodeAndPwd(String adminCode, String adminPwd) {
        LambdaQueryWrapper<Admins> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admins::getAdminCode, adminCode)
               .eq(Admins::getAdminPwd, adminPwd);
        return getOne(wrapper, false);
    }

    @Override
    public void updateAdminProfile(Admins admin) {
        Admins existing = getById(admin.getAdminId());
        if (existing != null) {
            existing.setAdminName(admin.getAdminName());
            existing.setAdminTel(admin.getAdminTel());
            updateById(existing);
        }
    }

    @Override
    public void updateAdminPassword(Integer adminId, String oldPwd, String newPwd) {
        Admins existing = getById(adminId);
        if (existing == null) {
            throw new RuntimeException("管理员不存在");
        }
        if (!existing.getAdminPwd().equals(oldPwd)) {
            throw new RuntimeException("原密码错误");
        }
        existing.setAdminPwd(newPwd);
        updateById(existing);
    }
}
