package com.coamctech.bxloan.manager.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coamctech.bxloan.manager.common.DataTablesPage;
import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.DocColumnDao;
import com.coamctech.bxloan.manager.dao.DocSourceDao;
import com.coamctech.bxloan.manager.dao.RoleUserRelDao;
import com.coamctech.bxloan.manager.dao.UserDao;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.DocSource;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.service.UserMngService;
import com.coamctech.bxloan.manager.service.VO.UserVO;
import com.coamctech.bxloan.manager.utils.CommonHelper;

/**
 * Created by Administrator on 2017/10/20.
 */
@Controller
@RequestMapping("/userMng")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private static final String userMainPage = "userMng/userMng";
    private static final String userAddPage = "userMng/userAdd";
    private static final String userEditPage = "userMng/userEdit";
    
    @Autowired
    private UserMngService userMngService;
    @Autowired
    private DocColumnDao docColumnDao;
    @Autowired
    private DocSourceDao docSourceDao;
    @Autowired
    private RoleUserRelDao roleUserRelDao;
    @Autowired
    private UserDao userDao;
    
    /**
     * 用户管理主页面
     */
    @RequestMapping
    public String index(){
    	return userMainPage;
    }
    
    @RequestMapping("/addUser")
    public String addUser(){
    	return userAddPage;
    }
    
    /**
     * type: add-新增 edit-编辑 view-查看
     */
    @RequestMapping("/edit/{type}/{userId}")
    public String edit(Model model,@PathVariable("type") String type,@PathVariable("userId") Long userId){
    	model.addAttribute("type", type);
    	model.addAttribute("userId", userId);
    	User user = userDao.findOne(userId);
    	List<Long> ll = roleUserRelDao.getRoleIdsByUserId(userId);
    	UserVO vo = new UserVO();
    	BeanUtils.copyProperties(user, vo,"birthday");
    	if(CollectionUtils.isNotEmpty(ll)){
    		vo.setRoleIds(CommonHelper.toStr(ll.get(0)));
    	}
    	vo.setBirthday(CommonHelper.date2Str(user.getBirthday(), CommonHelper.DF_DATE));
    	vo.setStartTime(CommonHelper.date2Str(user.getStartTime(), CommonHelper.DF_DATE));
    	vo.setEndTime(CommonHelper.date2Str(user.getEndTime(), CommonHelper.DF_DATE));
    	model.addAttribute("user",vo);
    	return userEditPage;
    }
    
    /**
     * 用户管理主页面初始化
     */
    @RequestMapping("/findByCondition")
	@ResponseBody
	public DataTablesPage findBySearch(@RequestParam("sEcho") Integer sEcho,
			@RequestParam("iDisplayStart") Integer firstIndex,
			@RequestParam("iDisplayLength") Integer pageSize,
			@RequestParam("userName") String userName,HttpSession session) {
    	User curUser = (User)session.getAttribute("user");
	    //当前登录用户
 		Page<UserVO> page = null;
	 		try {
	 			page = userMngService.findBySearch(firstIndex/pageSize, pageSize,userName,curUser);
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
	public JsonResult deleteById(@RequestParam("userId")String userId,HttpSession session) {
		try {
			User curUser = (User)session.getAttribute("user");
			JsonResult r = userMngService.deleteUserById(Long.valueOf(userId),curUser);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResult(ResultCode.ERROR_CODE,"删除失败");
		}
	}
    
    /**
     * 用户密码修改
     */
    @RequestMapping("/updatePassword")
	@ResponseBody
	public JsonResult updatePassword(@RequestParam("userId")String userId,@RequestParam("newPassword")String newPassword) {
		try {
			JsonResult r = userMngService.updatePassword(Long.valueOf(userId),newPassword);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResult(ResultCode.ERROR_CODE,"密码修改失败");
		}
	}
    
    /**
     * 用户新增/编辑
     */
	@RequestMapping("/editUser")
    @ResponseBody
    public JsonResult editUser(UserVO vo,HttpSession session){
    	try {
    		String sourceIds = vo.getSourceIds();
    		String columnIds = vo.getColumnIds();
    		String operateType = vo.getOperateType();
    		User curUser = (User)session.getAttribute("user");
    		List<String> sourceList = new ArrayList<>();
    		for(int i=0;i<sourceIds.split(",").length;i++){
    			sourceList.add(sourceIds.split(",")[i]);
    		}
    		List<String> columnList = new ArrayList<>();
    		for(int i=0;i<columnIds.split(",").length;i++){
    			columnList.add(columnIds.split(",")[i]);
    		}
			JsonResult r = userMngService.addOrEdit(curUser,vo,columnList,sourceList,operateType);
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
    public JsonResult allocateToUser(HttpSession session,Long userId,String columnIds,String sourceIds){
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
			JsonResult r = userMngService.allocateToUser(curUser, userId, columnList, sourceList);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResult(ResultCode.ERROR_CODE,"分配失败");
		}
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
}
