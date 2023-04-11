package com.wang.elm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wang.elm.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
