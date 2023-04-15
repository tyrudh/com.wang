package com.wang.elm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.elm.entity.Dish;
import com.wang.elm.mapper.DIshMapper;
import com.wang.elm.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DIshMapper, Dish> implements DishService {
}
