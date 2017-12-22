package com.coamctech.bxloan.manager.service;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.DocSource;
import com.coamctech.bxloan.manager.domain.Role;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.service.VO.UserStoreVO;
import com.coamctech.bxloan.manager.service.VO.UserTreeVO;
import com.coamctech.bxloan.manager.service.VO.UserVO;

public interface UserMngService {
	
	//主页面dataTable查询
	Page<UserVO> findBySearch(Integer pageNumber, Integer pageSize,String userName,User curUser) throws ParseException;
	
	//删除用户
	JsonResult deleteUserById(Long userId,User curUser) throws Exception;
	
	//新增查询用户
	JsonResult addOrEdit(User curUser,UserVO vo,
			List<String> columnIds, List<String> sourceIds, String type) throws Exception;
	
	//用户来源栏目分配
	JsonResult allocateToUser(User curUser,Long userId,List<String> columnIds, List<String> sourceIds) throws Exception;
	
	//查询用户的所有角色信息
	List<Long> getAllRoleByUserId(Long userId);
	
	//查询用户的所有的栏目信息
	List<Long> getAllColumnByUserId(Long userId);
	
	//查询用户的所有的来源信息
	List<Long> getAllSourceByUserId(Long userId);
	
	//查询角色用户树
	List<UserTreeVO> getUserTree();
	
	//查询用户已选栏目列表
	JsonResult getCheckedColumn(Long userId);
	
	//查询用户已选来源列表
	JsonResult getCheckedSource(Long userId);
	
	//修改密码
	JsonResult updatePassword(Long valueOf, String newPassword) throws RuntimeException;
	
	//查询用户收藏列表
	Page<UserStoreVO> findUserStoreList(Integer pageNumber, Integer pageSize, String userName);
	
	//判断当前用户是否为管理员
	boolean isManager(Long userId);
	
	//导出用户收藏列表
	void exportUserStore(String userName, HttpServletRequest request,
			HttpServletResponse response) throws Exception ;
}
