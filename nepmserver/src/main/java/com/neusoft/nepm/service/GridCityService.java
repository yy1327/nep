package com.neusoft.nepm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.nepm.po.GridCity;

import java.util.List;

public interface GridCityService extends IService<GridCity> {
    List<GridCity> listByProvinceId(Integer provinceId);
}
