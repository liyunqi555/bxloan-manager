package com.coamctech.bxloan.manager.service.Impl;

import com.coamctech.bxloan.manager.common.DynamicQuery.DynamicQuery;
import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.*;
import com.coamctech.bxloan.manager.domain.*;
import com.coamctech.bxloan.manager.service.DocSourceMngService;
import com.coamctech.bxloan.manager.service.RoleMngService;
import com.coamctech.bxloan.manager.service.VO.DocSourceVO;
import com.coamctech.bxloan.manager.service.VO.RoleVO;
import com.coamctech.bxloan.manager.utils.CommonHelper;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class DocSourceMngServiceImpl implements DocSourceMngService{
	
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
    @Autowired
    private DocSourceDao docSourceDao;
	
	@Override
	public Page<DocSourceVO> findBySearch(Integer pageNumber, Integer pageSize,
			String roleName) throws ParseException{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT r.id,r.name,r.type,r.if_special,r.language_type ");
		sql.append(" FROM t_doc_source r WHERE 1=1");
		int i = 0;
		if(StringUtils.isNotBlank(roleName)){
			sql.append(" AND r.name like ?").append(++i);
			params.add(StringUtils.join("%", roleName, "%"));
		}
		Page<Object[]> page = dynamicQuery.nativeQuery(Object[].class,
				new PageRequest(pageNumber, pageSize), sql.toString(),params.toArray());
		List<DocSourceVO> returnList = Lists.newArrayList(Lists.transform(
				page.getContent(), new Function<Object[], DocSourceVO>() {
					@Override
					public DocSourceVO apply(Object[] objs) {
                        DocSourceVO vo =  new DocSourceVO(objs);
						return vo;
					}
				}));
		Page<DocSourceVO> resultPage = new PageImpl<DocSourceVO>(returnList, new PageRequest(pageNumber, pageSize), page.getTotalElements());
		return resultPage;
	}

	@Override
	public JsonResult deleteRoleById(Long roleId) {
		Role role = roleDao.findOne(roleId);
		if(role.getType()==1){//管理员
			return new JsonResult(ResultCode.ERROR_CODE,"该角色为管理员，不可删除",null);
		}
		if(CollectionUtils.isNotEmpty(roleUserRelDao.findUserIdsByRoleId(roleId))){
			return new JsonResult(ResultCode.ERROR_CODE,"该角色下有用户，不可删除",null);
		}
		roleDao.delete(role);
		roleDocColumnRelDao.deleteRelByRoleId(roleId);
		roleDocSourceRelDao.deleteRelByRoleId(roleId);
		return new JsonResult(ResultCode.SUCCESS_CODE,"删除成功");
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

	@Override
	public JsonResult addDocSource(DocSourceVO docSourceVO,User curUser) throws Exception{
        DocSource docSource = null;
        if(docSourceVO.getId()==null){
            docSource = new DocSource();
            docSource.setCreateTime(new Date());
            docSource.setCreator(curUser.getId());
            docSource.setIfSpecial(docSourceVO.getIfSpecial());
        }else{
            docSource = docSourceDao.findOne(docSourceVO.getId());
        }
        docSource.setLanguageType(docSourceVO.getLanguageType());
        docSource.setName(docSourceVO.getName());
        docSource.setType(docSourceVO.getType());
        docSource.setUpdateTime(new Date());
        docSourceDao.save(docSource);
        return JsonResult.success();
	}

	@Override
	public Role getById(Long id) {
		return roleDao.findOne(id);
	}

	@Override
	public JsonResult editRole(String roleName, String roleType, String englishName, String id, User curUser) {
		Role role = roleDao.findOne(Long.valueOf(id));
		role.setRoleName(roleName);
		role.setEnglishName(englishName);
		role.setType(Integer.valueOf(roleType));
		role.setUpdateTime(CommonHelper.getNow());
		roleDao.save(role);
		return new JsonResult(ResultCode.SUCCESS_CODE,"角色修改成功");
	}
	
	@Override
	public JsonResult getCheckedColumn(Long roleId) {
		List<Long> ll = roleDocColumnRelDao.findColumnIdsByRoleId(roleId);
		return new JsonResult(ResultCode.SUCCESS_CODE,null,ll);
	}

	@Override
	public JsonResult getCheckedSource(Long roleId) {
		List<Long> ll = roleDocSourceRelDao.findSourceIdsByRoleId(roleId);
		return new JsonResult(ResultCode.SUCCESS_CODE,null,ll);
	}

}
