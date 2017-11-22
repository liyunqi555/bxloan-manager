package com.coamctech.bxloan.manager.config;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.UserDao;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.utils.TokenUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeMap;

/**
 *
 */
@Component
public class AppRequestFilter implements Filter {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(AppRequestFilter.class);
    private AntPathMatcher pathMatcher = new AntPathMatcher();
    @Value("/api/app/*/anon/*")
    private String anonUrlPattern;
    @Autowired
    private UserDao userDao;
    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if(true){
            User user = userDao.findOne(1L);
            TokenUtils.storeSessionUser(user);
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            String servletPath = request.getServletPath();
            logger.info("servletPath={}",servletPath);
            if(pathMatcher.match(anonUrlPattern,servletPath)){
                filterChain.doFilter(servletRequest, servletResponse);
            }else{
                String token = request.getParameter("token");
                logger.info("token:{}", token);
                if(token==null) {
                    response.setHeader("Content-Type", "application/json; charset=UTF-8");
                    response.getWriter().write(com.alibaba.fastjson.JSON.toJSONString(new JsonResult(ResultCode.PARAM_ERROR_CODE,ResultCode.TOKEN_NULL_MSG)));
                    response.getWriter().flush();
                    return;
                }
                if(checkSign(request,response)){
                    filterChain.doFilter(servletRequest, servletResponse);
                }else{
                    response.setHeader("Content-Type", "application/json; charset=UTF-8");
                    response.getWriter().write(com.alibaba.fastjson.JSON.toJSONString(new JsonResult(ResultCode.PARAM_ERROR_CODE, ResultCode.TOKEN_NULL_MSG)));
                    response.getWriter().flush();
                    return;
                }
            }
        }

        TokenUtils.clearSession();
    }
    private boolean checkSign(HttpServletRequest request, HttpServletResponse response){
        //获得传的数据
        Enumeration enumeration =  request.getParameterNames();
        TreeMap treemap = new TreeMap<String, String>(TokenUtils.URLSortedComparator.INSTANCE);
        for(Enumeration e=enumeration;e.hasMoreElements();) {
            String thisName=e.nextElement().toString();
            String thisValue=request.getParameter(thisName);
            treemap.put(thisName, thisValue);
        }
        String token = request.getParameter("token");
        Long uid = TokenUtils.uid(token);
        User user = userDao.findOne(uid);
        boolean success = TokenUtils.urlIsLegal(treemap, user.getSign());
        if(success){
            TokenUtils.storeSessionUser(user);
        }
        return  success;
    }
    @Override
    public void destroy() {

    }
} 