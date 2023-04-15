package com.wang.elm.common;
/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户Id
 * @return
 * @create 2023/4/13
 * @param
 **/
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    //    设置值
    public static void setThreadLocal(Long id){
        threadLocal.set(id);
    }
//    获取值
    public static Long getThreadLocal(){
        return threadLocal.get();
    }
}
