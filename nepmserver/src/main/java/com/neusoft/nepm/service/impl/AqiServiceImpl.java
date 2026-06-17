package com.neusoft.nepm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.nepm.mapper.AqiMapper;
import com.neusoft.nepm.po.Aqi;
import com.neusoft.nepm.service.AqiService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AqiServiceImpl extends ServiceImpl<AqiMapper, Aqi> implements AqiService {

    @Override
    public List<Aqi> listAqiAll() {
        return list();
    }
}
