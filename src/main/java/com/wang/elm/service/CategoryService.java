package com.wang.elm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wang.elm.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long ids);
}
