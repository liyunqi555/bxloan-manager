package com.coamctech.bxloan.manager.service.Impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.common.DynamicQuery.DynamicQuery;
import com.coamctech.bxloan.manager.dao.RoleDao;
import com.coamctech.bxloan.manager.dao.RoleDocColumnRelDao;
import com.coamctech.bxloan.manager.dao.RoleDocSourceRelDao;
import com.coamctech.bxloan.manager.dao.RoleUserRelDao;
import com.coamctech.bxloan.manager.dao.UserDocColumnRelDao;
import com.coamctech.bxloan.manager.dao.UserDocSourceRelDao;
import com.coamctech.bxloan.manager.domain.Role;
import com.coamctech.bxloan.manager.domain.RoleDocColumnRel;
import com.coamctech.bxloan.manager.domain.RoleDocSourceRel;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.domain.UserDocColumnRel;
import com.coamctech.bxloan.manager.domain.UserDocSourceRel;
import com.coamctech.bxloan.manager.service.RoleMngService;
import com.coamctech.bxloan.manager.service.VO.RoleVO;
import com.coamctech.bxloan.manager.utils.CommonHelper;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Service
public class RoleMngServiceImpl implements RoleMngService{
	
	@Autowired
	private DynamicQuery dynamicQuery;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private RoleUserRelDao roleUserRelDao;
	
	@Autowired
	private RoleDocColumnRelDao roleDocColumnRelDao;
	
	@Autowired
	private RoleDocSourceRelDao roleDocSourceRelDao;
	
	@Autowired
	private UserDocColumnRelDao userDocColumnRelDao;
	
	@Autowired
	private UserDocSourceRelDao userDocSourceRelDao;
	
	@Override
	public Page<RoleVO> findBySearch(Integer pageNumber, Integer pageSize,
			String roleName) throws ParseException{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT r.id,r.role_name,r.english_name,r.type ");
		sql.append(" FROM t_role r WHERE 1=1");
		int i = 0;
		if(StringUtils.isNotBlank(roleName)){
			sql.append(" AND r.role_name like ?").append(++i);
			params.add(StringUtils.join("%", roleName, "%"));
		}
		sql.append(" ORDER BY r.id asc");
		Page<Object[]> page = dynamicQuery.nativeQuery(Object[].class,
				new PageRequest(pageNumber, pageSize), sql.toString(),params.toArray());
		List<RoleVO> returnList = Lists.newArrayList(Lists.transform(
				page.getContent(), new Function<Object[], RoleVO>() {
					@Override
					public RoleVO apply(Object[] objs) {
						RoleVO vo =  new RoleVO(objs);
						return vo;
					}
				}));
		Page<RoleVO> resultPage = new PageImpl<RoleVO>(returnList, new PageRequest(pageNumber, pageSize), page.getTotalElements());
		return resultPage;
	}

	@Override
	public JsonResult deleteRoleById(Long roleId) {
		Role role = roleDao.findOne(roleId);
		if(role.getType()==1){//管理员
			return new JsonResult(ResultCode.ERROR_CODE,"该角色为管理员，不可删除",null);
		}
		roleDao.delete(role);
		roleUserRelDao.deleteByRoleId(roleId);
		roleDocColumnRelDao.deleteRelByRoleId(roleId);
		roleDocSourceRelDao.deleteRelByRoleId(roleId);
		return new JsonResult(ResultCode.SUCCESS_CODE,"删除成功");
	}

	@Override
	public JsonResult addOrEdit(User curUser, RoleVO vo,String type) throws Exception{
		if(StringUtils.equals("add", type)){
			Role role = new Role();
			BeanUtils.copyProperties(vo, role,"id");
			role.setCreateTime(CommonHelper.getNow());
			role.setCreator(curUser.getId());
			role.setStatus(1);//1:启用
			roleDao.save(role);
			return new JsonResult(ResultCode.SUCCESS_CODE,"角色新增成功");
		}else{
			Role role = roleDao.findOne(vo.getId());
			role.setRoleName(vo.getRoleName());
			role.setEnglishName(vo.getEnglishName());
			role.setUpdateTime(CommonHelper.getNow());
			roleDao.save(role);
			return new JsonResult(ResultCode.SUCCESS_CODE,"角色修改成功");
		}
		
	}

	@Override
	public JsonResult allocateToRole(User curUser, Long roleId, List<String> columnIds, List<String> sourceIds)
			throws Exception {
		roleDocColumnRelDao.deleteRelByRoleId(roleId);
		roleDocSourceRelDao.deleteRelByRoleId(roleId);
		if(CollectionUtils.isNotEmpty(columnIds)){
			for(String columnId:columnIds){
				RoleDocColumnRel rel = new  RoleDocColumnRel();
				rel.setDocColumnId(CommonHelper.toLong(columnId));
				rel.setRoleId(roleId);
				rel.setCreateTime(CommonHelper.getNow());
				rel.setCreator(curUser.getId());
				roleDocColumnRelDao.save(rel);
			}
		}
		
		if(CollectionUtils.isNotEmpty(sourceIds)){
			for(String sourceId:sourceIds){
				RoleDocSourceRel rel = new  RoleDocSourceRel();
				rel.setDocSourceId(CommonHelper.toLong(sourceId));
				rel.setRoleId(roleId);
				rel.setCreateTime(CommonHelper.getNow());
				rel.setCreator(curUser.getId());
				roleDocSourceRelDao.save(rel);
			}
		}
		
		List<Long> userIds =  roleUserRelDao.findUserIdsByRoleId(roleId);
		for(Long userId:userIds){
			if(CollectionUtils.isNotEmpty(columnIds)){
				for(String columnId:columnIds){
					UserDocColumnRel rel = new  UserDocColumnRel();
					rel.setDocColumnId(CommonHelper.toLong(columnId));
					rel.setUserId(userId);
					rel.setCreateTime(CommonHelper.getNow());
					rel.setCreator(curUser.getId());
					userDocColumnRelDao.save(rel);
				}
			}
			
			if(CollectionUtils.isNotEmpty(sourceIds)){
				for(String sourceId:sourceIds){
					UserDocSourceRel rel = new  UserDocSourceRel();
					rel.setDocSourceId(CommonHelper.toLong(sourceId));
					rel.setUserId(userId);
					rel.setCreateTime(CommonHelper.getNow());
					rel.setCreator(curUser.getId());
					userDocSourceRelDao.save(rel);
				}
			}
		}
		return new JsonResult(ResultCode.SUCCESS_CODE,"分配成功");
	}

}
