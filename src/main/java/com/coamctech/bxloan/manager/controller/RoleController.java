package com.coamctech.bxloan.manager.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.service.RoleMngService;
import com.coamctech.bxloan.manager.service.VO.RoleVO;

/**
 * Created by Administrator on 2017/10/20.
 */
@Controller
@RequestMapping("/roleMng")
public class RoleController {
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
    
    private static final String roleMainPage = "roleMng";
    private static final String roleAddPage = "roleAdd";
    private static final String roleEditPage = "roleEdit";
    
    @Autowired
    private RoleMngService roleMngService;
    
    /**
     * 角色管理主页面初始化
     */
    @RequestMapping("/findByCondition")
	@ResponseBody
	public DataTablesPage findBySearch(@RequestParam("sEcho") Integer sEcho,
			@RequestParam("iDisplayStart") Integer firstIndex,
			@RequestParam("iDisplayLength") Integer pageSize,
			@RequestParam("roleName") String roleName) {
	    //当前登录用户
 		Page<RoleVO> page = null;
	 		try {
	 			page = roleMngService.findBySearch(firstIndex/pageSize, pageSize,roleName);
	 		} catch (ParseException e) {
	 			e.printStackTrace();
	 		}

	 		return new DataTablesPage(sEcho, page);
    	
    }
    
    /**
     * 用户管理主页面
     */
    @RequestMapping
    public String index(Model model,HttpSession session){
    	return roleMainPage;
    }
    	
    /**
     * 角色删除
     */
    @RequestMapping("/deleteById")
	@ResponseBody
	public JsonResult deleteById(HttpServletRequest request) {
		try {
			String roleId = request.getParameter("roleId");
			JsonResult r = roleMngService.deleteRoleById(Long.valueOf(roleId));
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResult(ResultCode.ERROR_CODE,"删除失败");
		}
	}
    
    /**
     * 角色新增
     */
    @RequestMapping(value="editRole")
    @ResponseBody
    public JsonResult addUser(HttpSession session,RoleVO vo,String type){
    	try {
    		User curUser = (User)session.getAttribute("user");
			JsonResult r = roleMngService.addOrEdit(curUser,vo,type);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResult(ResultCode.ERROR_CODE,"服务器异常");
		}
    	
    	
    }
    
    /**
	 * 角色栏目来源分配
	 */
	@RequestMapping("/allocateToRole")
    @ResponseBody
    public JsonResult allocateToRole(HttpSession session,Long roleId,List<String> columnIds,List<String> sourceIds){
    	try {
    		User curUser = (User)session.getAttribute("user");
			JsonResult r = roleMngService.allocateToRole(curUser, roleId, columnIds, sourceIds);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResult(ResultCode.ERROR_CODE,"分配失败");
		}
	}
}
