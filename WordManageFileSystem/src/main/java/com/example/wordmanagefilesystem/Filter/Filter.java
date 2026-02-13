package com.example.wordmanagefilesystem.Filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

//@Slf4j
////@WebFilter(urlPatterns = "/*") //所有响应和请求都拦截
//public class Filter implements jakarta.servlet.Filter {
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        jakarta.servlet.Filter.super.init(filterConfig);
//        log.info("init 开始拦截！！");
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        log.info("do 处理拦截");
////        filterChain.doFilter(servletRequest , servletResponse);
//    }
//
//    @Override
//    public void destroy() {
//        jakarta.servlet.Filter.super.destroy();
//        log.info("destroy 销毁拦截！！");
//    }
//}
