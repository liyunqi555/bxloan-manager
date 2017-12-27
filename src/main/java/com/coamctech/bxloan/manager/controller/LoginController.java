package com.coamctech.bxloan.manager.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.config.WebSecurityConfig;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.service.UserMngService;
import com.coamctech.bxloan.manager.service.UserService;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserMngService userMngService;

    @RequestMapping(value="/login")
    public String login(){
    	return "login";
    }
    
    @RequestMapping(value="/index")
    public String index(HttpSession session){
    	User curUser = (User)session.getAttribute("user");
    	if("admin".equals(curUser.getUserName())){
    		return "index1";
		}else{
			if(userMngService.isManager(curUser.getId())){
				return "index1";
			}else{
				return "index2";
			}
		}
    	
    }
    
    @RequestMapping("/loginPost")
    @ResponseBody
    public  JsonResult loginPost(String userName, String password, HttpSession session) {
        JsonResult result = userService.ifLoginSuccess(userName, password);
        if(ResultCode.ERROR_CODE==result.getCode()){
        	return result;
        }else{
        	session.setAttribute(WebSecurityConfig.SESSION_KEY, (User)result.getBody());
        	return result;
        }
        
    }
    
    @GetMapping("/loginOut")
    public String logout(HttpSession session) {
        // 移除session
        session.removeAttribute(WebSecurityConfig.SESSION_KEY);
        return "redirect:/login";
    }
}
