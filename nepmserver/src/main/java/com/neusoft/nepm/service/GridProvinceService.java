package com.neusoft.nepm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.nepm.po.GridProvince;

import java.util.List;

public interface GridProvinceService extends IService<GridProvince> {
    List<GridProvince> listAll();
}
