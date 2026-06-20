package com.neusoft.nepm.controller;

import com.neusoft.nepm.common.Result;
import com.neusoft.nepm.dto.AfPageRequestDto;
import com.neusoft.nepm.dto.PageResponseDto;
import com.neusoft.nepm.po.AqiFeedback;
import com.neusoft.nepm.service.AqiFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/aqiFeedback")
public class AqiFeedbackController {

    @Autowired
    private AqiFeedbackService aqiFeedbackService;

    @PostMapping("/saveAqiFeedback")
    public Result<Void> saveAqiFeedback(@RequestBody AqiFeedback feedback) {
        if (feedback.getSupervisorId() == null || feedback.getSupervisorId().trim().isEmpty()) {
            return Result.error("supervisorId不能为空");
        }
        if (feedback.getAfType() == null || feedback.getAfType().trim().isEmpty()) {
            return Result.error("反馈类型不能为空");
        }
        if (feedback.getAfLevel() == null || feedback.getAfLevel().trim().isEmpty()) {
            return Result.error("AQI等级不能为空");
        }
        if (feedback.getAfAddress() == null || feedback.getAfAddress().trim().isEmpty()) {
            return Result.error("问题地址不能为空");
        }
        aqiFeedbackService.saveFeedback(feedback);
        return Result.success();
    }

    @PostMapping("/listAqiFeedbackByTelId")
    public Result<List<Map<String, Object>>> listAqiFeedbackByTelId(@RequestBody Map<String, String> params) {
        String telId = params.get("telId");
        if (telId == null || telId.trim().isEmpty()) {
            return Result.error("telId不能为空");
        }
        return Result.success(aqiFeedbackService.listBySupervisorId(telId));
    }

    @PostMapping("/listAqiFeedbackPage")
    public Result<PageResponseDto<Map<String, Object>>> listAqiFeedbackPage(@RequestBody AfPageRequestDto dto) {
        return Result.success(aqiFeedbackService.listPage(dto));
    }

    @PostMapping("/getAqiFeedbackById")
    public Result<Map<String, Object>> getAqiFeedbackById(@RequestBody Map<String, Integer> params) {
        Integer afId = params.get("afId");
        return Result.success(aqiFeedbackService.getFeedbackById(afId));
    }
    @PostMapping("/updateAqiFeedbackAssign")
    public Result<Void> updateAqiFeedbackAssign(@RequestBody Map<String, Object> params) {
        Object afIdObj = params.get("afId");
        Object gmIdObj = params.get("gmId");

        if (afIdObj == null || gmIdObj == null) {
            return Result.error("afId和gmId不能为空");
        }

        Integer afId;
        Integer gmId;

        try {
            if (afIdObj instanceof Integer) {
                afId = (Integer) afIdObj;
            } else if (afIdObj instanceof Number) {
                afId = ((Number) afIdObj).intValue();
            } else {
                afId = Integer.parseInt(afIdObj.toString());
            }

            if (gmIdObj instanceof Integer) {
                gmId = (Integer) gmIdObj;
            } else if (gmIdObj instanceof Number) {
                gmId = ((Number) gmIdObj).intValue();
            } else {
                gmId = Integer.parseInt(gmIdObj.toString());
            }
        } catch (NumberFormatException e) {
            return Result.error("afId和gmId格式不正确");
        }

        String desc = params.get("desc") != null ? params.get("desc").toString() : null;

        try {
            aqiFeedbackService.updateAssign(afId, gmId, desc);
            return Result.success();
        } catch (Exception e) {
            return Result.error("指派失败：" + e.getMessage());
        }
    }

    @PostMapping("/listAqiFeedbackByGmId")
    public Result<List<Map<String, Object>>> listAqiFeedbackByGmId(@RequestBody Map<String, Integer> params) {
        Integer gmId = params.get("gmId");
        if (gmId == null) {
            return Result.error("gmId不能为空");
        }
        return Result.success(aqiFeedbackService.listByGmId(gmId));
    }

    @PostMapping("/updateAqiFeedbackState")
    public Result<Void> updateAqiFeedbackState(@RequestBody Map<String, Object> params) {
        Object afIdObj = params.get("afId");
        if (afIdObj == null) {
            return Result.error("afId不能为空");
        }
        Integer afId;
        try {
            if (afIdObj instanceof Integer) {
                afId = (Integer) afIdObj;
            } else {
                afId = Integer.parseInt(afIdObj.toString());
            }
        } catch (NumberFormatException e) {
            return Result.error("afId格式不正确");
        }
        String state = params.get("state") != null ? params.get("state").toString() : null;
        Integer aqi = params.get("aqi") != null ? Integer.parseInt(params.get("aqi").toString()) : null;
        String desc = params.get("desc") != null ? params.get("desc").toString() : null;
        aqiFeedbackService.updateState(afId, state, aqi, desc);
        return Result.success();
    }
}
