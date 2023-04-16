package com.wang.elm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.elm.dto.DishDto;
import com.wang.elm.entity.Dish;
import com.wang.elm.entity.DishFlavor;
import com.wang.elm.mapper.DIshMapper;
import com.wang.elm.service.DishFlavorService;
import com.wang.elm.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DIshMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService flavorService;

    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId();//菜品id
        //菜品口味
        List<DishFlavor> flavorList = dishDto.getFlavors();

        flavorList = flavorList.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
                }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表dish_flavor
        flavorService.saveBatch(flavorList);


    }
}
