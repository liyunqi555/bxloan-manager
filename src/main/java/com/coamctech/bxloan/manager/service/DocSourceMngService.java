package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.domain.DocSource;
import com.coamctech.bxloan.manager.domain.Role;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.service.VO.DocSourceVO;
import com.coamctech.bxloan.manager.service.VO.RoleVO;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;

public interface DocSourceMngService {
	
	//删除角色
	JsonResult deleteById(Long docSourceId);
	
	//角色dataTables查询
	Page<DocSourceVO> findBySearch(Integer pageNumber, Integer pageSize, String roleName) throws ParseException;
	

	//新增角色
	JsonResult saveDocSource(DocSourceVO docSourceVO, User curUser) throws Exception;
	
	//根据roleId 获取role
    DocSource getById(Long valueOf);
	
}
