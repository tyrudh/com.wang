package com.wang.elm.dto;

import com.wang.elm.entity.Setmeal;
import com.wang.elm.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
