package com.coamctech.bxloan.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.coamctech.bxloan.manager.service.UserMngService;
import com.coamctech.bxloan.manager.service.VO.UserStoreVO;

/**
 * Created by Administrator on 2017/10/20.
 */
@Controller
@RequestMapping("/userStoreMng")
public class UserStoreController {
    private static final Logger logger = LoggerFactory.getLogger(UserStoreController.class);
    
    private static final String mainPage = "userStore";
    
    @Autowired
    private UserMngService userMngService;
    
    /**
     * 用户管理主页面
     */
    @RequestMapping
    public String index(HttpSession session,Model model){
    	User curUser = (User)session.getAttribute("user");
    	if(curUser.getUserName().equals("admin")){
    		model.addAttribute("curUserName", "");
    		model.addAttribute("type", "2");
    	}else{
    		//判断当前用户角色是否为管理员
        	if(!userMngService.isManager(curUser.getId())){
        		model.addAttribute("curUserName", curUser.getUserName());
        		model.addAttribute("type", "1");
        	}else{
        		model.addAttribute("curUserName", "");
        		model.addAttribute("type", "2");
        	}
    	}
    	
    	return mainPage;
    }
    
    
    /**
     * 用户管理主页面初始化
     */
    @RequestMapping("/findByCondition")
	@ResponseBody
	public DataTablesPage findByCondition(@RequestParam("sEcho") Integer sEcho,
			@RequestParam("iDisplayStart") Integer firstIndex,
			@RequestParam("iDisplayLength") Integer pageSize,
			@RequestParam("userName") String userName,HttpSession session) {
    	try{
    		Page<UserStoreVO> page = null;
    		page = userMngService.findUserStoreList(firstIndex/pageSize, pageSize,userName);
    		return new DataTablesPage(sEcho, page);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		return null;
    	
    }
    
    @RequestMapping("/checkDownload")
	@ResponseBody
	public JsonResult checkDownload(@RequestParam("userName") String  userName){
		try{
			Page<UserStoreVO> page = null;
			page = userMngService.findUserStoreList(new Integer(0 / 1000), new Integer(1000),
					userName);
			//查询正常合同列表
			if(page == null || page.getContent() == null || page.getContent().size() == 0){
				return new JsonResult(ResultCode.ERROR_CODE,"无数据，无法导出报表",null);
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			return new JsonResult(ResultCode.ERROR_CODE,"服务器异常",null);
		}
		return new JsonResult(ResultCode.SUCCESS_CODE,"导出成功",null);
		
	}
	
	@RequestMapping("/downloadExcel")
	@ResponseBody
	public JsonResult downloadExcel(HttpServletRequest request, HttpServletResponse response, String userName){
		try {
			/**下载excel到本地*/
			userMngService.exportUserStore(userName, request, response);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new JsonResult(ResultCode.ERROR_CODE,"服务器异常",null);
		}
		return new JsonResult(ResultCode.SUCCESS_CODE,"导出成功",null);
	}
    
  
    
}
