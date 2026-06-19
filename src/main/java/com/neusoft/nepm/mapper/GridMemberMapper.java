package com.neusoft.nepm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nepm.po.GridMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GridMemberMapper extends BaseMapper<GridMember> {

    @Select("SELECT * FROM grid_member WHERE gm_province_id = #{provinceId} AND gm_city_id = #{cityId} AND gm_status = '01'")
    List<GridMember> getGridMembersByProvinceAndCity(Integer provinceId, Integer cityId);
}
