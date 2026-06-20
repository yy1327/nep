package com.neusoft.nepm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.nepm.po.GridMember;

import java.util.List;

public interface GridMemberService extends IService<GridMember> {
    List<GridMember> listByProvinceId(Integer provinceId, Integer cityId);
    GridMember getGridMemberByCodeAndPwd(String gmCode, String gmPwd);
    void updateProfile(GridMember member);
    void updatePassword(Integer gmId, String oldPwd, String newPwd);
}
