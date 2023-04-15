package com.wang.elm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wang.elm.common.R;
import com.wang.elm.entity.Employee;
import com.wang.elm.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        // 1、将页面提交的password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
//        2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
//        3、如何没有查询到则直接返回登陆失败
        if(emp == null){
            return R.error("登陆失败");
        }
//        4、密码比对，如果不一致泽返回登陆失败
        if(!emp.getPassword().equals(password)){
            return R.error("登陆失败");
        }

//        5、查看员工状态，如果已经是禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0){
            return R.error("账号已禁用");
        }
//        6、登录成功，将员工Id存入Session并返回登陆成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }
    /**
     * logout
     * @return com.wang.elm.common.R<java.lang.String>
     * @create 2023/4/8
     * 员工退出
     **/
    @PostMapping("logout")
    public R<String> logout(HttpServletRequest request){
        //清理session中的Id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
    /**
     * 新增员工
     * @return com.wang.elm.common.R<java.lang.String>
     * @create 2023/4/9
     * @param employee
     **/
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
      log.info("新增员工，员工信息：{}",employee.toString());
      employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//      employee.setCreateTime(LocalDateTime.now());
//      employee.setUpdateTime(LocalDateTime.now());
//      获得当前登录用户的Id
        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeService.save(employee);
      return R.success("新增员工成功");
    }
   /**
    * 员工信息分页查询
    * @return com.wang.elm.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
    * @create 2023/4/10
    *
    * @param page
    * @param pageSize
    * @param name
    **/

   @GetMapping("/page")
    public R<Page> page(int page ,int pageSize,String name){
        log.info("page = {}, pageSize = {}, name = {}",page,pageSize,name);

        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();

        //添加过滤条件
        queryWrapper.like(StringUtils.hasText(name),Employee::getName,name);

        //添加排序条件
       queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    /**
     * 状态修改
     * @return com.wang.elm.common.R<java.lang.String>
     * @create 2023/4/11
     *
     * @param request
     * @param employee
     **/
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){

       Long empId = (Long) request.getSession().getAttribute("employee");
       employee.setUpdateTime(LocalDateTime.now());
       employee.setUpdateUser(empId);
       employeeService.updateById(employee);
       return R.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息");
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到信息");
    }

}
