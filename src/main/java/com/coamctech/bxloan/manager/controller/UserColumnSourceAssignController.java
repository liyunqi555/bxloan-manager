package com.coamctech.bxloan.manager.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.dao.DocColumnDao;
import com.coamctech.bxloan.manager.dao.DocSourceDao;
import com.coamctech.bxloan.manager.dao.RoleDao;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.DocSource;
import com.coamctech.bxloan.manager.domain.Role;
import com.coamctech.bxloan.manager.service.UserMngService;
import com.coamctech.bxloan.manager.service.VO.UserTreeVO;
import com.coamctech.bxloan.manager.utils.CommonHelper;

@RequestMapping("/userColumnSourceAssign")
@Controller
public class UserColumnSourceAssignController {
	
	@Autowired
    private DocColumnDao docColumnDao;
    @Autowired
    private DocSourceDao docSourceDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserMngService userMngService;
	
	@RequestMapping
	public String index(){
		return "userAllocation";
	}
	
	@RequestMapping(value="/getAllRole", method = RequestMethod.POST)
    @ResponseBody
    public List<Role> getAllRole(){
    	
		List<Role> list = (List<Role>)roleDao.findAll();
		return list;
    	
    }
	
	@RequestMapping(value="/getAllUser", method = RequestMethod.POST)
    @ResponseBody
    public List<UserTreeVO> getAllUser(){
    	
		List<UserTreeVO> list = userMngService.getUserTree();
		return list;
    	
    }

	
	@RequestMapping(value="/getAllColumn", method = RequestMethod.POST)
    @ResponseBody
    public List<DocColumn> getAllColumn(){
    	
		List<DocColumn> list =  (List<DocColumn>)docColumnDao.findAll();
		return list;
    	
    }
    
    @RequestMapping(value="/getAllSource", method = RequestMethod.POST)
    @ResponseBody
    public List<DocSource> getAllSource(){
    	
		List<DocSource> list =  (List<DocSource>)docSourceDao.findAll();
		return list;
    	
    }
    
    @RequestMapping(value="/getCheckedColumn")
    @ResponseBody
    public JsonResult getCheckedColumn(HttpServletRequest request){
    	Long userId = CommonHelper.toLong(request.getParameter("userId"));
    	return userMngService.getCheckedColumn(userId);
    	
    }
    
    @RequestMapping(value="/getCheckedSource")
    @ResponseBody
    public JsonResult getCheckedSource(HttpServletRequest request){
    	Long userId = CommonHelper.toLong(request.getParameter("userId"));
    	return userMngService.getCheckedSource(userId);
    	
    }
    
    
}
