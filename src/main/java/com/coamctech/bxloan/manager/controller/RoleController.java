package com.coamctech.bxloan.manager.controller;

import java.text.ParseException;
import java.util.ArrayList;
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
import com.coamctech.bxloan.manager.dao.RoleDao;
import com.coamctech.bxloan.manager.domain.Role;
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
    
    @Autowired
    private RoleDao roleDao;
    
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
    @RequestMapping(value="add")
    @ResponseBody
    public JsonResult add(HttpServletRequest request){
    	try {
    		User curUser = (User)request.getSession().getAttribute("user");
    		String roleName = request.getParameter("roleName");
    		String roleType = request.getParameter("roleType");
    		String englishName = request.getParameter("englishName");
			JsonResult r = roleMngService.addRole(roleName,roleType,englishName,curUser);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResult(ResultCode.ERROR_CODE,"服务器异常");
		}
    	
    	
    }
    
    /**
     * 角色新增
     */
    @RequestMapping(value="edit")
    @ResponseBody
    public JsonResult edit(HttpServletRequest request){
    	try {
    		User curUser = (User)request.getSession().getAttribute("user");
    		String roleName = request.getParameter("roleName");
    		String roleType = request.getParameter("roleType");
    		String englishName = request.getParameter("englishName");
    		String id = request.getParameter("id");
			JsonResult r = roleMngService.editRole(roleName,roleType,englishName,id,curUser);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResult(ResultCode.ERROR_CODE,"服务器异常");
		}
    	
    	
    }
    
    @RequestMapping(value="/getAllRoles")
    @ResponseBody
    public List<Role> getAllRoles(){
    	return (List<Role>)roleDao.findAll();
    	
    }
    
    @RequestMapping("/initModalData")
    @ResponseBody
    public JsonResult initModalData(HttpServletRequest request){
    	try{
    		String id = request.getParameter("id");
        	Role role = roleMngService.getById(Long.valueOf(id));
        	return new JsonResult(ResultCode.SUCCESS_CODE,"初始化弹窗成功",role);
    	}catch (Exception e) {
    		e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResult(ResultCode.ERROR_CODE,"初始化弹窗失败");
    	}
    	
    }
    
    /**
	 * 角色栏目来源分配
	 */
	@RequestMapping("/allocateToRole")
    @ResponseBody
    public JsonResult allocateToRole(HttpSession session,Long roleId,String columnIds,String sourceIds){
    	try {
    		User curUser = (User)session.getAttribute("user");
    		List<String> sourceList = new ArrayList<>();
    		for(int i=0;i<sourceIds.split(",").length;i++){
    			sourceList.add(sourceIds.split(",")[i]);
    		}
    		List<String> columnList = new ArrayList<>();
    		for(int i=0;i<columnIds.split(",").length;i++){
    			columnList.add(columnIds.split(",")[i]);
    		}
			JsonResult r = roleMngService.allocateToRole(curUser, roleId, columnList, sourceList);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResult(ResultCode.ERROR_CODE,"分配失败");
		}
	}
}
