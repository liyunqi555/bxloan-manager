package com.coamctech.bxloan.manager.service.Impl;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.common.DynamicQuery.DynamicQuery;
import com.coamctech.bxloan.manager.dao.RoleDao;
import com.coamctech.bxloan.manager.dao.RoleUserRelDao;
import com.coamctech.bxloan.manager.dao.UserDao;
import com.coamctech.bxloan.manager.dao.UserDocColumnRelDao;
import com.coamctech.bxloan.manager.dao.UserDocSourceRelDao;
import com.coamctech.bxloan.manager.domain.Role;
import com.coamctech.bxloan.manager.domain.RoleUserRel;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.domain.UserDocColumnRel;
import com.coamctech.bxloan.manager.domain.UserDocSourceRel;
import com.coamctech.bxloan.manager.service.UserMngService;
import com.coamctech.bxloan.manager.service.VO.UserStoreVO;
import com.coamctech.bxloan.manager.service.VO.UserTreeVO;
import com.coamctech.bxloan.manager.service.VO.UserVO;
import com.coamctech.bxloan.manager.utils.CommonHelper;
import com.coamctech.bxloan.manager.utils.ReportExcelUtils;
import com.coamctech.bxloan.manager.utils.encrypt.MD5Util;
import com.google.common.base.Function;
import com.google.common.collect.Lists;


@Transactional
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
			String userName,User curUser) throws ParseException{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT u.id,u.user_name,u.nick_name,u.birthday,u.email,u.office_phone,u.telephone,u.creator ");
		sql.append(" FROM t_user u WHERE 1=1");
		int i = 0;
		if(StringUtils.isNotBlank(userName)){
			sql.append(" AND u.user_name like ?").append(++i);
			params.add(StringUtils.join("%", userName, "%"));
		}
		if(!curUser.getUserName().equals("admin")){
			sql.append(" AND u.user_name != 'admin'");
		}
		Page<Object[]> page = dynamicQuery.nativeQuery(Object[].class,
				new PageRequest(pageNumber, pageSize), sql.toString(),params.toArray());
		List<UserVO> returnList = Lists.newArrayList(Lists.transform(
				page.getContent(), new Function<Object[], UserVO>() {
					@Override
					public UserVO apply(Object[] objs) {
						UserVO vo =  new UserVO(objs);
						if(vo.getCreator()!=null){
							if(userDao.findOne(vo.getCreator())!=null){
								vo.setCreatorStr(userDao.findOne(vo.getCreator()).getUserName());
							}else{
								vo.setCreatorStr("");
							}
						}
						return vo;
					}
				}));
		Page<UserVO> resultPage = new PageImpl<UserVO>(returnList, new PageRequest(pageNumber, pageSize), page.getTotalElements());
		return resultPage;
	}

	@Override
	public JsonResult deleteUserById(Long userId,User curUser) throws Exception{
		User user = userDao.findOne(userId);
		if(curUser.getUserName().equals("admin")){
			
		}else{
			if(isManager(userId)){
				return new JsonResult(ResultCode.ERROR_CODE,"该用户为其他管理员，您无删除权限，请确认！");
			}
		}
		userDao.delete(user);
		roleUserRelDao.deleteByUserId(userId);
		userDocSourceRelDao.deleteRelByUserId(userId);
		userDocColumnRelDao.deleteRelByUserId(userId);
		return new JsonResult(ResultCode.SUCCESS_CODE,"删除成功");
	}

	@Override
	public JsonResult addOrEdit(User curUser,UserVO vo,  List<String> columnIds, List<String> sourceIds,
			String type) throws Exception{
		if(StringUtils.equals("add", type)){
			//新增
			User user = new User();
			BeanUtils.copyProperties(vo, user,"id","password");
			//判断用户名是否已经存在
			User existUser = userDao.findByUserName(vo.getUserName());
			if(existUser!=null){
				return new JsonResult(ResultCode.ERROR_CODE,"用户名已存在");
			}
			user.setUserName(vo.getUserName());
			user.setBirthday(CommonHelper.str2Date(vo.getBirthday(), CommonHelper.DF_DATE));
			user.setEmail(vo.getEmail());
			user.setNickName(vo.getNickName());
			user.setOfficePhone(vo.getOfficePhone());
			user.setTelephone(vo.getTelephone());
			user.setUpdateTime(CommonHelper.getNow());
			user.setPassword(MD5Util.md5Hex(vo.getPassword()));
			user.setCreateTime(CommonHelper.getNow());
			user.setCreator(curUser.getId());
			user.setStatus(1);//默认为1：启用
			user.setStartTime(CommonHelper.str2Date(vo.getStartTime(), CommonHelper.DF_DATE));
			user.setEndTime(CommonHelper.str2Date(vo.getEndTime(), CommonHelper.DF_DATE));
			userDao.save(user);
			
			
			RoleUserRel rrel = new  RoleUserRel();
			rrel.setRoleId(CommonHelper.toLong(vo.getRoleIds()));
			rrel.setUserId(user.getId());
			roleUserRelDao.save(rrel);
				
			
			
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
			if(user.getUserName()!=vo.getUserName()){
				User existUser = userDao.findByUserName(vo.getUserName());
				if(existUser!=null){
					return new JsonResult(ResultCode.ERROR_CODE,"用户名已存在");
				}
			}
			user.setUserName(vo.getUserName());
			user.setBirthday(CommonHelper.str2Date(vo.getBirthday(), CommonHelper.DF_DATE));
			user.setEmail(vo.getEmail());
			user.setNickName(vo.getNickName());
			user.setOfficePhone(vo.getOfficePhone());
			user.setTelephone(vo.getTelephone());
			user.setUpdateTime(CommonHelper.getNow());
			if(StringUtils.isNotBlank(vo.getPassword())){
				user.setPassword(MD5Util.md5Hex(vo.getPassword()));
			}
			user.setStartTime(CommonHelper.str2Date(vo.getStartTime(), CommonHelper.DF_DATE));
			user.setEndTime(CommonHelper.str2Date(vo.getEndTime(), CommonHelper.DF_DATE));
			userDao.save(user);
			roleUserRelDao.deleteByUserId(user.getId());
			userDocSourceRelDao.deleteRelByUserId(user.getId());
			userDocColumnRelDao.deleteRelByUserId(user.getId());
			
			RoleUserRel rrel = new  RoleUserRel();
			rrel.setRoleId(CommonHelper.toLong(vo.getRoleIds()));
			rrel.setUserId(user.getId());
			roleUserRelDao.save(rrel);
			
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

	@Override
	public List<UserTreeVO> getUserTree() {
		List<UserTreeVO> ll = new ArrayList<>();
		List<Role> allRole =(List<Role>) roleDao.findAll();
		if(CollectionUtils.isNotEmpty(allRole)){
			for(Role r : allRole){
				UserTreeVO vo = new UserTreeVO();
				vo.setId(r.getId()+1000000);
				vo.setName(r.getRoleName());
				ll.add(vo);
				//获取该角色下所有用户
				List<Long> allUserIds = roleUserRelDao.findUserIdsByRoleId(r.getId());
				if(CollectionUtils.isNotEmpty(allUserIds)){
					for(int i = 0;i<allUserIds.size();i++){	
						Long id = CommonHelper.toLong(allUserIds.get(i));
						User u = userDao.findOne(id);
						UserTreeVO vo2 = new UserTreeVO();
						vo2.setId(u.getId());
						vo2.setName(u.getUserName());
						vo2.setParentId(r.getId()+1000000);
						ll.add(vo2);
					}
				}else{
					ll.remove(vo);
				}
			}
		}
		
		return ll;
	}

	@Override
	public JsonResult getCheckedColumn(Long userId) {
		List<Long> ll = userDocColumnRelDao.findColumnIdsByUserId(userId);
		return new JsonResult(ResultCode.SUCCESS_CODE,null,ll);
	}

	@Override
	public JsonResult getCheckedSource(Long userId) {
		List<Long> ll = userDocSourceRelDao.findSourceIdsByUserId(userId);
		return new JsonResult(ResultCode.SUCCESS_CODE,null,ll);
	}

	@Override
	public JsonResult updatePassword(Long userId, String newPassword) throws RuntimeException{
		User user = userDao.findOne(userId);
		user.setPassword(MD5Util.md5Hex(newPassword));
		return new JsonResult(ResultCode.SUCCESS_CODE,"密码修改成功",null);
	}
	
	//判断是否为管理员
	@Override
	public boolean isManager(Long userId){
		List<Long> ll = roleUserRelDao.getRoleIdsByUserId(userId);
		if(CollectionUtils.isNotEmpty(ll)){
			for(int i=0;i<ll.size();i++){
				Long id = CommonHelper.toLong(ll.get(i));
				Role r = roleDao.findOne(id);
				if(r.getType()==1){//管理员
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Page<UserStoreVO> findUserStoreList(Integer pageNumber, Integer pageSize,
			String userName) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT u.id,u.user_name,di.title,u.create_time ");
		sql.append(" FROM t_user_store us,t_user u,t_doc_info di WHERE u.id=us.user_id and us.doc_info_id = di.id");
		int i = 0;
		if(StringUtils.isNotBlank(userName)){
			sql.append(" AND u.user_name like ?").append(++i);
			params.add(StringUtils.join("%", userName, "%"));
		}
		Page<Object[]> page = dynamicQuery.nativeQuery(Object[].class,
				new PageRequest(pageNumber, pageSize), sql.toString(),params.toArray());
		List<UserStoreVO> returnList = Lists.newArrayList(Lists.transform(
				page.getContent(), new Function<Object[], UserStoreVO>() {
					@Override
					public UserStoreVO apply(Object[] objs) {
						UserStoreVO vo = new UserStoreVO(objs);
						return vo;
					}
				}));
		Page<UserStoreVO> resultPage = new PageImpl<UserStoreVO>(returnList, new PageRequest(pageNumber, pageSize), page.getTotalElements());
		return resultPage;
	}

	@Override
	public void exportUserStore(String userName, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try{
			Resource resource = new ClassPathResource("excel/userStore_template.xlsx");
			InputStream is = resource.getInputStream();
			//System.out.println("scr.getInputStream()scr.getInputStream()=="+resource.getInputStream());
			int PAGE_SIZE = 1000000;
			Map<String, List<UserStoreVO>> map = new HashMap<String, List<UserStoreVO>>();
			Page<UserStoreVO> page = this.findUserStoreList(0, PAGE_SIZE, userName);
			List<UserStoreVO> list = page.getContent();
			map.put("reportList", list);
			/**导出excel工具类*/
			ReportExcelUtils reportExcelUtils= new ReportExcelUtils();
			/**加载模版并且生成导出文件落地*/
			String fileName = reportExcelUtils.genernateExcelFileName(is, "excel/", "用户收藏", map);
			/**获取文件路径*/
			String reportPath = "excel/"; 
			File file = new File(reportPath+fileName);
			/**从服务器下载到本地*/
			ReportExcelUtils.downloadFile(file, fileName, request, response);
			/**下载后删除服务器文件*/
			file.delete();
		}catch(Exception e){
			throw new Exception("下载失败！");
		}
		
	}
}