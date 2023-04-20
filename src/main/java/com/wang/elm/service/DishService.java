package com.wang.elm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wang.elm.dto.DishDto;
import com.wang.elm.entity.Dish;

public interface DishService extends IService<Dish> {

    //新增菜品同时插入菜品对应的口味数据，需要操作两张表：dish，dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息
    public void updateWithFlavor(DishDto dishDto);
}
