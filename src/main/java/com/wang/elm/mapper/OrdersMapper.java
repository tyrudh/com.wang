package com.wang.elm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wang.elm.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
