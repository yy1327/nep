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
        aqiFeedbackService.saveFeedback(feedback);
        return Result.success();
    }

    @PostMapping("/listAqiFeedbackByTelId")
    public Result<List<Map<String, Object>>> listAqiFeedbackByTelId(@RequestBody Map<String, String> params) {
        String telId = params.get("telId");
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
        Integer afId = (Integer) params.get("afId");
        Integer gmId = (Integer) params.get("gmId");
        String desc = (String) params.get("desc");
        aqiFeedbackService.updateAssign(afId, gmId, desc);
        return Result.success();
    }

    @PostMapping("/listAqiFeedbackByGmId")
    public Result<List<Map<String, Object>>> listAqiFeedbackByGmId(@RequestBody Map<String, Integer> params) {
        Integer gmId = params.get("gmId");
        return Result.success(aqiFeedbackService.listByGmId(gmId));
    }

    @PostMapping("/updateAqiFeedbackState")
    public Result<Void> updateAqiFeedbackState(@RequestBody Map<String, Object> params) {
        Integer afId = (Integer) params.get("afId");
        String state = (String) params.get("state");
        Integer aqi = (Integer) params.get("aqi");
        String desc = (String) params.get("desc");
        aqiFeedbackService.updateState(afId, state, aqi, desc);
        return Result.success();
    }
}
