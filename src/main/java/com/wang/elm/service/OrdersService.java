package com.wang.elm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wang.elm.entity.Orders;

public interface OrdersService extends IService<Orders> {
    public void submit(Orders orders);
}
