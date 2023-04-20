package com.wang.elm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wang.elm.dto.SetmealDto;
import com.wang.elm.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要爆保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);
}
