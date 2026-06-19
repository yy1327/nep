package com.neusoft.nepm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.nepm.mapper.GridCityMapper;
import com.neusoft.nepm.po.GridCity;
import com.neusoft.nepm.service.GridCityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GridCityServiceImpl extends ServiceImpl<GridCityMapper, GridCity> implements GridCityService {

    @Override
    public List<GridCity> listByProvinceId(Integer provinceId) {
        LambdaQueryWrapper<GridCity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GridCity::getProvinceId, provinceId);
        return list(wrapper);
    }
}
