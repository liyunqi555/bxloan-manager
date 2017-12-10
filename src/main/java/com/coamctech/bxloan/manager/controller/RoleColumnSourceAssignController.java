package com.coamctech.bxloan.manager.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.service.RoleMngService;
import com.coamctech.bxloan.manager.utils.CommonHelper;

@RequestMapping("/roleColumnSourceAssign")
@Controller
public class RoleColumnSourceAssignController {
	
	@Autowired
	private RoleMngService roleMngService;
	
	@RequestMapping
	public String index(){
		return "roleAllocation";
	}
	
	
	@RequestMapping(value="/getCheckedColumn")
    @ResponseBody
    public JsonResult getCheckedColumn(HttpServletRequest request){
    	Long roleId = CommonHelper.toLong(request.getParameter("roleId"));
    	return roleMngService.getCheckedColumn(roleId);
    	
    }
    
    @RequestMapping(value="/getCheckedSource")
    @ResponseBody
    public JsonResult getCheckedSource(HttpServletRequest request){
    	Long roleId = CommonHelper.toLong(request.getParameter("roleId"));
    	return roleMngService.getCheckedSource(roleId);
    	
    }

}
