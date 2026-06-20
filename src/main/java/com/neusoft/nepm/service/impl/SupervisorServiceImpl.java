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
        supervisor.setSupervisorId(generateSupervisorId());
        if (supervisor.getProvinceId() == null) supervisor.setProvinceId(0);
        if (supervisor.getCityId() == null) supervisor.setCityId(0);
        supervisor.setCreateTime(new java.util.Date());
        save(supervisor);
    }

    private String generateSupervisorId() {
        String latestId = supervisorMapper.getLatestSupervisorId();
        if (latestId != null && latestId.startsWith("SP")) {
            try {
                return String.format("SP%04d", Integer.parseInt(latestId.substring(2)) + 1);
            } catch (NumberFormatException e) {}
        }
        return "SP0001";
    }

    @Override
    public Supervisor getSupervisorByIdAndPwd(String tel, String pwd) {
        return supervisorMapper.getSupervisorByTelAndPwd(tel, pwd);
    }
}
