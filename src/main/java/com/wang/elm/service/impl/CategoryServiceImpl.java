package com.wang.elm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.elm.common.CustomException;
import com.wang.elm.entity.Category;
import com.wang.elm.entity.Dish;
import com.wang.elm.entity.Setmeal;
import com.wang.elm.mapper.CategoryMapper;
import com.wang.elm.service.CategoryService;
import com.wang.elm.service.DishService;
import com.wang.elm.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private DishService dishService;
    /**
     * 根据Id删除，删除之前进行判断
     * @param ids
     */
    @Override
    public void remove(Long ids) {
        //查询当前分类是否关联了菜品，如果已关联，抛出一个业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        int num = dishService.count(dishLambdaQueryWrapper);

        if (num > 0){
            //已经关联菜品抛出业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        //查询当前分类是否关联了套餐，如果已关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealServiceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealServiceLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        int num1 = setmealService.count(setmealServiceLambdaQueryWrapper);

        if (num1 > 0){
            //已经关联套餐抛出业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        super.removeById(ids);
        //正常删除分类
    }
}
