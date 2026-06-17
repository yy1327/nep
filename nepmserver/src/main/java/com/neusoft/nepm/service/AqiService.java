package com.neusoft.nepm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.nepm.po.Aqi;

import java.util.List;

public interface AqiService extends IService<Aqi> {
    List<Aqi> listAqiAll();
}
