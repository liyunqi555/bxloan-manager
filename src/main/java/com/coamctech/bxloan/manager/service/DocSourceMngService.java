package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.domain.Role;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.service.VO.DocSourceVO;
import com.coamctech.bxloan.manager.service.VO.RoleVO;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;

public interface DocSourceMngService {
	
	//删除角色
	JsonResult deleteRoleById(Long roleId);
	
	//角色dataTables查询
	Page<DocSourceVO> findBySearch(Integer pageNumber, Integer pageSize, String roleName) throws ParseException;
	
	//角色栏目来源分配
	JsonResult allocateToRole(User curUser, Long roleId, List<String> columnIds, List<String> sourceIds) throws Exception;
	
	//新增角色
	JsonResult addDocSource(DocSourceVO docSourceVO, User curUser) throws Exception;
	
	//根据roleId 获取role
	Role getById(Long valueOf);
	
	//编辑角色
	JsonResult editRole(String roleName, String roleType, String englishName, String id, User curUser);

	//查询角色已选栏目列表
	JsonResult getCheckedColumn(Long roleId);
		
	//查询角色已选来源列表
	JsonResult getCheckedSource(Long roleId);
}
