package com.neusoft.nepm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.nepm.mapper.GridMemberMapper;
import com.neusoft.nepm.po.GridMember;
import com.neusoft.nepm.service.GridMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GridMemberServiceImpl extends ServiceImpl<GridMemberMapper, GridMember> implements GridMemberService {

    @Autowired
    private GridMemberMapper gridMemberMapper;

    @Override
    public List<GridMember> listByProvinceId(Integer provinceId, Integer cityId) {
        return gridMemberMapper.getGridMembersByProvinceAndCity(provinceId, cityId);
    }

    @Override
    public GridMember getGridMemberByCodeAndPwd(String gmCode, String gmPwd) {
        LambdaQueryWrapper<GridMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GridMember::getGmCode, gmCode)
               .eq(GridMember::getGmPwd, gmPwd);
        return getOne(wrapper, false);
    }

    @Override
    public void updateProfile(GridMember member) {
        GridMember existing = getById(member.getGmId());
        if (existing != null) {
            existing.setGmName(member.getGmName());
            existing.setGmTel(member.getGmTel());
            updateById(existing);
        }
    }

    @Override
    public void updatePassword(Integer gmId, String oldPwd, String newPwd) {
        GridMember existing = getById(gmId);
        if (existing == null) throw new RuntimeException("网格员不存在");
        if (!existing.getGmPwd().equals(oldPwd)) throw new RuntimeException("原密码错误");
        existing.setGmPwd(newPwd);
        updateById(existing);
    }
}
