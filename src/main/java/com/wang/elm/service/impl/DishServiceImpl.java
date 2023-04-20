package com.wang.elm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.elm.dto.DishDto;
import com.wang.elm.entity.Dish;
import com.wang.elm.entity.DishFlavor;
import com.wang.elm.mapper.DIshMapper;
import com.wang.elm.service.DishFlavorService;
import com.wang.elm.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
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

    @Override
    public DishDto getByIdWithFlavor(Long id) {

    //查询菜品基本信息，从dish表中查找
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavorList = flavorService.list(queryWrapper);
        dishDto.setFlavors(flavorList);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //清理当前菜品对应口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

        flavorService.remove(queryWrapper);

        //添加当前提交过来的口味数据————dish_Flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        flavorService.saveBatch(flavors);


        //添加当前提交过来的口味数据
    }


}
