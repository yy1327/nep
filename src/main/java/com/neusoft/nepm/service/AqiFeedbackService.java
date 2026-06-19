package com.neusoft.nepm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.nepm.dto.AfPageRequestDto;
import com.neusoft.nepm.dto.PageResponseDto;
import com.neusoft.nepm.po.AqiFeedback;

import java.util.List;
import java.util.Map;

public interface AqiFeedbackService extends IService<AqiFeedback> {
    void saveFeedback(AqiFeedback feedback);
    List<Map<String, Object>> listBySupervisorId(String supervisorId);
    PageResponseDto<Map<String, Object>> listPage(AfPageRequestDto dto);
    Map<String, Object> getFeedbackById(Integer afId);
    void updateAssign(Integer afId, Integer gmId, String desc);
    List<Map<String, Object>> listByGmId(Integer gmId);
    void updateState(Integer afId, String state, Integer aqi, String desc);
}
