package com.wang.elm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wang.elm.common.R;
import com.wang.elm.entity.User;
import com.wang.elm.service.UserService;
import com.wang.elm.utils.SMsUtils1;
import com.wang.elm.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送验证码
     * @param user
     * @param session
     * @return
     * @throws Exception
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) throws Exception {
        //获取手机号
        String phone = user.getPhone();

        if (StringUtils.hasText(phone)){
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code = {}",code);
            //调用阿里云提供的短信服务API完成发送短信
            //SMsUtils1.sendMessage();
            //需要将生成的验证码保存到session
            session.setAttribute(phone,code);

            return R.success("手机验证码短信发送成功");
        }
        return R.error("短信发送失败，请稍后再试");
    }

    /**
     * 登录验证码认证
     * @param
     * @param session
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session) throws Exception {

        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();


        //获取验证码
        String code = map.get("code").toString();
        //从session中获取保存的验证码
        Object attribute = session.getAttribute(phone);
        //进行验证码的比对
        if (attribute != null && attribute.equals(code)){
            //比对成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if(user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);

        }

        return R.error("登陆失败");
    }
}
