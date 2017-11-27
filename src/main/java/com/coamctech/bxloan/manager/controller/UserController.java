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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coamctech.bxloan.manager.common.DataTablesPage;
import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.service.UserMngService;
import com.coamctech.bxloan.manager.service.VO.UserVO;

/**
 * Created by Administrator on 2017/10/20.
 */
@Controller
@RequestMapping("/userMng")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private static final String userMainPage = "userMng";
    private static final String userAddPage = "userAdd";
    private static final String userEditPage = "userEdit";
    
    @Autowired
    private UserMngService userMngService;
    
    /**
     * 用户管理主页面
     */
    @RequestMapping
    public String index(Model model,HttpSession session){
    	return userMainPage;
    }
    /**
     * type: add-新增 edit-编辑 view-查看
     */
    @RequestMapping("/edit/{type}/{userId}")
    public String edit(Model model,@PathVariable("type") String type,@PathVariable("userId") Long userId){
    	model.addAttribute("type", type);
    	model.addAttribute("userId", userId);
    	//用户对应的角色信息
    	model.addAttribute("roleIds", userMngService.getAllRoleByUserId(userId));
    	//用户对应的栏目信息
    	model.addAttribute("roleIds", userMngService.getAllColumnByUserId(userId));
    	//用户对于的来源信息
    	model.addAttribute("roleIds", userMngService.getAllSourceByUserId(userId));
    	return userEditPage;
    }
    /**
    * type: add-新增 edit-编辑 view-查看
    */
   @RequestMapping("/add")
   public String add(){
	   	return userAddPage;
   }	
    
    /**
     * 用户管理主页面初始化
     */
    @RequestMapping("/findByCondition")
	@ResponseBody
	public DataTablesPage findBySearch(@RequestParam("sEcho") Integer sEcho,
			@RequestParam("iDisplayStart") Integer firstIndex,
			@RequestParam("iDisplayLength") Integer pageSize,
			@RequestParam("customerName") String customerName) {
	    //当前登录用户
 		Page<UserVO> page = null;
	 		try {
	 			page = userMngService.findBySearch(firstIndex/pageSize, pageSize,customerName);
	 		} catch (ParseException e) {
	 			e.printStackTrace();
	 		}
	 		return new DataTablesPage(sEcho, page);
    	
    }
    
  
    /**
     * 用户删除
     */
    @RequestMapping("/deleteById")
	@ResponseBody
	public JsonResult deleteById(HttpServletRequest request) {
		try {
			String userId = request.getParameter("userId");
			JsonResult r = userMngService.deleteUserById(Long.valueOf(userId));
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResult(ResultCode.ERROR_CODE,"删除失败");
		}
	}

    /**
     * 用户新增/编辑
     */
	@RequestMapping("/editUser")
    @ResponseBody
    public JsonResult addUser(HttpSession session,UserVO vo,List<String> roleIds,List<String> columnIds,List<String> sourceIds,String type){
    	try {
    		User curUser = (User)session.getAttribute("user");
			JsonResult r = userMngService.addOrEdit(curUser,vo,roleIds,columnIds,sourceIds,type);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResult(ResultCode.ERROR_CODE,"服务器异常");
		}
    }
	
	/**
	 * 用户栏目来源分配
	 */
	@RequestMapping("/allocateToUser")
    @ResponseBody
    public JsonResult allocateToUser(HttpSession session,Long userId,List<String> columnIds,List<String> sourceIds){
    	try {
    		User curUser = (User)session.getAttribute("user");
			JsonResult r = userMngService.allocateToUser(curUser, userId, columnIds, sourceIds);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResult(ResultCode.ERROR_CODE,"分配失败");
		}
	}
}
