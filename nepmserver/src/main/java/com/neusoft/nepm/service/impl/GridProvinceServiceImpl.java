package com.neusoft.nepm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.nepm.mapper.GridProvinceMapper;
import com.neusoft.nepm.po.GridProvince;
import com.neusoft.nepm.service.GridProvinceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GridProvinceServiceImpl extends ServiceImpl<GridProvinceMapper, GridProvince> implements GridProvinceService {

    @Override
    public List<GridProvince> listAll() {
        return list();
    }
}
