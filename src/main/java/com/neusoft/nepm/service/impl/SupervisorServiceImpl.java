package com.neusoft.nepm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.nepm.mapper.SupervisorMapper;
import com.neusoft.nepm.po.Supervisor;
import com.neusoft.nepm.service.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupervisorServiceImpl extends ServiceImpl<SupervisorMapper, Supervisor> implements SupervisorService {

    @Autowired
    private SupervisorMapper supervisorMapper;

    @Override
    public Supervisor getSupervisorById(String supervisorId) {
        return supervisorMapper.getSupervisorByTel(supervisorId);
    }

    @Override
    public void saveSupervisor(Supervisor supervisor) {
        supervisor.setSupervisorId(supervisor.getSupervisorTel());
        if (supervisor.getProvinceId() == null) {
            supervisor.setProvinceId(0);
        }
        if (supervisor.getCityId() == null) {
            supervisor.setCityId(0);
        }
        supervisor.setCreateTime(new java.util.Date());
        save(supervisor);
    }

    @Override
    public Supervisor getSupervisorByIdAndPwd(String tel, String pwd) {
        return supervisorMapper.getSupervisorByTelAndPwd(tel, pwd);
    }
}
