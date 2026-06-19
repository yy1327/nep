package com.neusoft.nepm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.nepm.po.Supervisor;

public interface SupervisorService extends IService<Supervisor> {
    Supervisor getSupervisorById(String supervisorId);
    void saveSupervisor(Supervisor supervisor);
    Supervisor getSupervisorByIdAndPwd(String tel, String pwd);
}
