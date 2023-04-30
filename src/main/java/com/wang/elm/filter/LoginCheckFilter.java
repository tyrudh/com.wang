package com.wang.elm.filter;

import com.alibaba.fastjson.JSON;
import com.wang.elm.common.BaseContext;
import com.wang.elm.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * 检测用户是否已经完成登录
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
//        1、获取本此请求的url
        String requestUIL = request.getRequestURI();
        log.info("拦截到请求：{}",requestUIL);
//        定义不需要处理的请求
        String[] urls = new String[]{
          "/employee/login",
          "/employee/logout",
          "/backend/**",
          "/front/**",
          "/common/**",
          "/user/sendMsg",
          "/user/login"
        };
//        2、判断本次的请求是否需要处理
        boolean checked = check(urls, requestUIL);
//        3、如果不需要处理，则直接进行放行
        if (checked){
            log.info("本此次请求{}不需要处理",requestUIL);
            filterChain.doFilter(request,response);
            return;
        }

//        4-1、判断登录状态，如果已经登录则直接放行
        if (request.getSession().getAttribute("employee") != null){
            log.info("用户已登录，用户Id为：{}",request.getSession().getAttribute("employee"));

            Long empId = (Long)request.getSession().getAttribute("employee");
            BaseContext.setThreadLocal(empId);

            filterChain.doFilter(request,response);
            return;
        }
        //4-2、判断登录状态，如果已经登录则直接放行
        if (request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户Id为：{}",request.getSession().getAttribute("user"));

            Long userId = (Long)request.getSession().getAttribute("user");
            BaseContext.setThreadLocal(userId);

            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登录");
//        5、如果未登录则返回登录结果,通过输出流的方式来响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

/**
 * 路径匹配，检查本次请求是否需要放行
 * @return boolean
 * @create 2023/4/9
 * 
 * @param urls
 * @param requestUIL
 **/
    public boolean check(String[] urls,String requestUIL){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestUIL);
            if (match){
                return true;
            }
        }
        return false;
    }
}
