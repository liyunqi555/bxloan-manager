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
import com.coamctech.bxloan.manager.dao.RoleUserRelDao;
import com.coamctech.bxloan.manager.dao.UserDao;
import com.coamctech.bxloan.manager.dao.UserDocColumnRelDao;
import com.coamctech.bxloan.manager.dao.UserDocSourceRelDao;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.DocSource;
import com.coamctech.bxloan.manager.domain.Role;
import com.coamctech.bxloan.manager.domain.RoleUserRel;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.domain.UserDocColumnRel;
import com.coamctech.bxloan.manager.domain.UserDocSourceRel;
import com.coamctech.bxloan.manager.service.UserMngService;
import com.coamctech.bxloan.manager.service.VO.UserVO;
import com.coamctech.bxloan.manager.utils.CommonHelper;
import com.coamctech.bxloan.manager.utils.encrypt.MD5Util;
import com.google.common.base.Function;
import com.google.common.collect.Lists;


@Service
public class UserMngServiceImpl implements UserMngService{
	
	@Autowired
	private DynamicQuery dynamicQuery;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private RoleUserRelDao roleUserRelDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserDocColumnRelDao userDocColumnRelDao;
	
	@Autowired
	private UserDocSourceRelDao userDocSourceRelDao;

	@Override
	public Page<UserVO> findBySearch(Integer pageNumber, Integer pageSize,
			String userName) throws ParseException{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT u.id,u.user_name,u.nick_name,u.birthday,u.email,u.office_phone,u.telephone ");
		sql.append(" FROM t_user u WHERE 1=1");
		int i = 0;
		if(StringUtils.isNotBlank(userName)){
			sql.append(" AND u.user_name like ?").append(++i);
			params.add(StringUtils.join("%", userName, "%"));
		}
		sql.append(" ORDER BY u.id asc");
		Page<Object[]> page = dynamicQuery.nativeQuery(Object[].class,
				new PageRequest(pageNumber, pageSize), sql.toString(),params.toArray());
		List<UserVO> returnList = Lists.newArrayList(Lists.transform(
				page.getContent(), new Function<Object[], UserVO>() {
					@Override
					public UserVO apply(Object[] objs) {
						UserVO vo =  new UserVO(objs);
						return vo;
					}
				}));
		Page<UserVO> resultPage = new PageImpl<UserVO>(returnList, new PageRequest(pageNumber, pageSize), page.getTotalElements());
		return resultPage;
	}

	@Override
	public JsonResult deleteUserById(Long userId) throws Exception{
		List<Role> ll = roleUserRelDao.getRoleByUserId(userId);
		if(CollectionUtils.isNotEmpty(ll)){
			for(Role r:ll){
				if(r.getType()==1){//管理员
					return new JsonResult(ResultCode.ERROR_CODE,"该用户为管理员，不可删除",null);
				}
			}
		}
		userDao.delete(userId);
		roleUserRelDao.deleteByUserId(userId);
		userDocSourceRelDao.deleteRelByUserId(userId);
		userDocColumnRelDao.deleteRelByUserId(userId);
		return new JsonResult(ResultCode.SUCCESS_CODE,"删除成功");
	}

	@Override
	public JsonResult addOrEdit(User curUser,UserVO vo, List<String> roleIds, List<String> columnIds, List<String> sourceIds,
			String type) throws Exception{
		if(StringUtils.equals("add", type)){
			//新增
			User user = new User();
			BeanUtils.copyProperties(vo, user,"id","password");
			user.setPassword(MD5Util.md5Hex(vo.getPassword()));
			user.setCreateTime(CommonHelper.getNow());
			user.setCreator(curUser.getId());
			user.setStatus(1);//默认为1：启用
			userDao.save(user);
			
			if(CollectionUtils.isNotEmpty(roleIds)){
				for(String roleId:roleIds){
					RoleUserRel rel = new  RoleUserRel();
					rel.setRoleId(CommonHelper.toLong(roleId));
					rel.setUserId(user.getId());
					roleUserRelDao.save(rel);
				}
			}
			
			if(CollectionUtils.isNotEmpty(columnIds)){
				for(String columnId:columnIds){
					UserDocColumnRel rel = new  UserDocColumnRel();
					rel.setDocColumnId(CommonHelper.toLong(columnId));
					rel.setUserId(user.getId());
					rel.setCreateTime(CommonHelper.getNow());
					rel.setCreator(curUser.getId());
					userDocColumnRelDao.save(rel);
				}
			}
			
			if(CollectionUtils.isNotEmpty(sourceIds)){
				for(String sourceId:sourceIds){
					UserDocSourceRel rel = new  UserDocSourceRel();
					rel.setDocSourceId(CommonHelper.toLong(sourceId));
					rel.setUserId(user.getId());
					rel.setCreateTime(CommonHelper.getNow());
					rel.setCreator(curUser.getId());
					userDocSourceRelDao.save(rel);
				}
			}
			return new JsonResult(ResultCode.SUCCESS_CODE,"用户新增成功");
		}else{
			//修改
			User user = userDao.findOne(vo.getId());
			user.setUserName(vo.getUserName());
			user.setBirthday(CommonHelper.str2Date(vo.getBirthday(), CommonHelper.DF_DATE));
			user.setEmail(vo.getEmail());
			user.setNickName(vo.getNickName());
			user.setOfficePhone(vo.getOfficePhone());
			user.setTelephone(vo.getTelephone());
			user.setUpdateTime(CommonHelper.getNow());
			user.setPassword(MD5Util.md5Hex(vo.getPassword()));
			userDao.save(user);
			roleUserRelDao.deleteByUserId(user.getId());
			userDocSourceRelDao.deleteRelByUserId(user.getId());
			userDocColumnRelDao.deleteRelByUserId(user.getId());
			if(CollectionUtils.isNotEmpty(roleIds)){
				for(String roleId:roleIds){
					RoleUserRel rel = new  RoleUserRel();
					rel.setRoleId(CommonHelper.toLong(roleId));
					rel.setUserId(user.getId());
					roleUserRelDao.save(rel);
				}
			}
			
			if(CollectionUtils.isNotEmpty(columnIds)){
				for(String columnId:columnIds){
					UserDocColumnRel rel = new  UserDocColumnRel();
					rel.setDocColumnId(CommonHelper.toLong(columnId));
					rel.setUserId(user.getId());
					rel.setCreateTime(CommonHelper.getNow());
					rel.setCreator(curUser.getId());
					userDocColumnRelDao.save(rel);
				}
			}
			
			if(CollectionUtils.isNotEmpty(sourceIds)){
				for(String sourceId:sourceIds){
					UserDocSourceRel rel = new  UserDocSourceRel();
					rel.setDocSourceId(CommonHelper.toLong(sourceId));
					rel.setUserId(user.getId());
					rel.setCreateTime(CommonHelper.getNow());
					rel.setCreator(curUser.getId());
					userDocSourceRelDao.save(rel);
				}
			}
		}
		return new JsonResult(ResultCode.SUCCESS_CODE,"用户修改成功");
	}

	@Override
	public List<Long> getAllRoleByUserId(Long userId) {
		return roleUserRelDao.findRoleIdsByUserId(userId);
		
	}

	@Override
	public List<Long> getAllColumnByUserId(Long userId) {
		return userDocColumnRelDao.findColumnIdsByUserId(userId);
	}

	@Override
	public List<Long> getAllSourceByUserId(Long userId) {
		return userDocSourceRelDao.findSourceIdsByUserId(userId);
	}

	@Override
	public JsonResult allocateToUser(User curUser,Long userId, List<String> columnIds, List<String> sourceIds) throws Exception {
		userDocSourceRelDao.deleteRelByUserId(userId);
		userDocColumnRelDao.deleteRelByUserId(userId);
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
		return new JsonResult(ResultCode.SUCCESS_CODE,"分配成功");
		
	}
}