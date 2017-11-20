package com.coamctech.bxloan.manager.config;

import com.coamctech.bxloan.manager.utils.TokenUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Component
public class BackRequestFilter implements Filter {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(BackRequestFilter.class);
    @Value("/user/userMng")
    private String redirectURL;
    @Value("/user/userMng")
    private String loginUrlPattern;
    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if(loginUrlPattern.indexOf(request.getServletPath())!=-1){
            filterChain.doFilter(request,response);
        }
        Object userId = request.getSession().getAttribute("userId");
        if(userId!=null){
            filterChain.doFilter(servletRequest, servletResponse);
        }else{
//            String path = request.getContextPath();
//            String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
//            logger.info(basePath + redirectURL);
            response.sendRedirect(redirectURL);
            return;
        }
    }

    @Override
    public void destroy() {

    }
} 