package com.coamctech.bxloan.manager.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.service.VO.RoleVO;

public interface RoleMngService {
	
	//删除角色
	JsonResult deleteRoleById(Long roleId);
	
	//新增或修改角色
	JsonResult addOrEdit(User curUser, RoleVO vo,String type) throws Exception;
	
	//角色dataTables查询
	Page<RoleVO> findBySearch(Integer pageNumber, Integer pageSize, String roleName) throws ParseException;
	
	//角色栏目来源分配
	JsonResult allocateToRole(User curUser,Long roleId,List<String> columnIds, List<String> sourceIds) throws Exception;


}
