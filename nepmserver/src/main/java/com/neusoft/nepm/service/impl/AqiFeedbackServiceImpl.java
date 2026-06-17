package com.neusoft.nepm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.nepm.dto.AfPageRequestDto;
import com.neusoft.nepm.dto.PageResponseDto;
import com.neusoft.nepm.mapper.AqiFeedbackMapper;
import com.neusoft.nepm.po.AqiFeedback;
import com.neusoft.nepm.service.AqiFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AqiFeedbackServiceImpl extends ServiceImpl<AqiFeedbackMapper, AqiFeedback> implements AqiFeedbackService {

    @Autowired
    private AqiFeedbackMapper aqiFeedbackMapper;

    @Override
    public void saveFeedback(AqiFeedback feedback) {
        feedback.setAfState("01");
        feedback.setCreateTime(new Date());
        save(feedback);
    }

    @Override
    public List<Map<String, Object>> listBySupervisorId(String supervisorId) {
        return aqiFeedbackMapper.getFeedbackBySupervisorId(supervisorId);
    }

    @Override
    public PageResponseDto<Map<String, Object>> listPage(AfPageRequestDto dto) {
        Page<AqiFeedback> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<AqiFeedback> wrapper = new LambdaQueryWrapper<>();

        if (dto.getAfCode() != null && !dto.getAfCode().isEmpty()) {
            wrapper.like(AqiFeedback::getAfCode, dto.getAfCode());
        }
        if (dto.getAfState() != null && !dto.getAfState().isEmpty()) {
            wrapper.eq(AqiFeedback::getAfState, dto.getAfState());
        }
        if (dto.getSupervisorName() != null && !dto.getSupervisorName().isEmpty()) {
            wrapper.like(AqiFeedback::getSupervisorId, dto.getSupervisorName());
        }
        if (dto.getAfAddress() != null && !dto.getAfAddress().isEmpty()) {
            wrapper.like(AqiFeedback::getAfAddress, dto.getAfAddress());
        }

        wrapper.orderByDesc(AqiFeedback::getCreateTime);
        page = page(page, wrapper);

        PageResponseDto<Map<String, Object>> response = new PageResponseDto<>();
        response.setTotal(page.getTotal());
        response.setPageNum(dto.getPageNum());
        response.setPageSize(dto.getPageSize());
        response.setRecords(null);

        return response;
    }

    @Override
    public Map<String, Object> getFeedbackById(Integer afId) {
        return aqiFeedbackMapper.getFeedbackDetailById(afId);
    }

    @Override
    public void updateAssign(Integer afId, Integer gmId, String desc) {
        AqiFeedback feedback = getById(afId);
        if (feedback != null) {
            feedback.setAfAssignGmId(gmId);
            feedback.setAfAssignTime(new Date());
            feedback.setAfAssignDesc(desc);
            feedback.setAfState("02");
            updateById(feedback);
        }
    }

    @Override
    public List<Map<String, Object>> listByGmId(Integer gmId) {
        LambdaQueryWrapper<AqiFeedback> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AqiFeedback::getAfAssignGmId, gmId);
        wrapper.orderByDesc(AqiFeedback::getCreateTime);
        List<AqiFeedback> list = list(wrapper);
        return null;
    }

    @Override
    public void updateState(Integer afId, String state, Integer aqi, String desc) {
        AqiFeedback feedback = getById(afId);
        if (feedback != null) {
            feedback.setAfState(state);
            if (aqi != null) {
                feedback.setAfConfirmAqi(aqi);
            }
            if (desc != null) {
                feedback.setAfConfirmDesc(desc);
            }
            feedback.setAfConfirmTime(new Date());
            updateById(feedback);
        }
    }
}
