package com.coamctech.bxloan.manager.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coamctech.bxloan.manager.common.DataTablesPage;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.service.UserMngService;
import com.coamctech.bxloan.manager.service.VO.UserSignVO;

/**
 * Created by Administrator on 2017/10/20.
 */
@Controller
@RequestMapping("/userSign")
public class UserSignController {
    private static final Logger logger = LoggerFactory.getLogger(UserSignController.class);
    
    private static final String mainPage = "userSign";
    
    @Autowired
    private UserMngService userMngService;
    
    /**
     * 主页面
     */
    @RequestMapping
    public String index(HttpSession session,Model model){
    	
    	return mainPage;
    }
    
    
    /**
     * 主页面初始化
     */
    @RequestMapping("/findByCondition")
	@ResponseBody
	public DataTablesPage findByCondition(@RequestParam("sEcho") Integer sEcho,
			@RequestParam("iDisplayStart") Integer firstIndex,
			@RequestParam("iDisplayLength") Integer pageSize,
			@RequestParam("columnName") String columnName,HttpSession session) {
    	User curUser = (User)session.getAttribute("user");
    	try{
    		Page<UserSignVO> page = null;
    		page = userMngService.findUserSignList(firstIndex/pageSize, pageSize,curUser.getId(),columnName);
    		return new DataTablesPage(sEcho, page);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		return null;
    	
    }
    
  
    
}
