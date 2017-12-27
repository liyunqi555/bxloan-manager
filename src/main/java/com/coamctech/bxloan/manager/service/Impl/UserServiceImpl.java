package com.coamctech.bxloan.manager.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.UserDao;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.service.UserMngService;
import com.coamctech.bxloan.manager.service.UserService;
import com.coamctech.bxloan.manager.utils.CommonHelper;
import com.coamctech.bxloan.manager.utils.encrypt.MD5Util;


@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserMngService userMngService;

	@Override
	public JsonResult ifLoginSuccess(String userName, String password) {
		// TODO Auto-generated method stub
		User user = userDao.findByUserName(userName);
		if(user == null){
			return new JsonResult(ResultCode.ERROR_CODE,"用户不存在",null);
		}
		String md5Password = MD5Util.md5Hex(password);
		if(!user.getPassword().equals(md5Password)){
			return new JsonResult(ResultCode.ERROR_CODE,"密码不正确",null);
		}
		if(user.getStartTime()!=null){
			if(CommonHelper.getNow().compareTo(user.getCreateTime())<0){
				return new JsonResult(ResultCode.ERROR_CODE,"用户尚未生效，请联系管理员",null);
			}
		}
		if(user.getEndTime()!=null){
			if(CommonHelper.getNow().compareTo(user.getEndTime())>0){
				return new JsonResult(ResultCode.ERROR_CODE,"用户已失效，请联系管理员",null);
			}
		}
		return new JsonResult(ResultCode.SUCCESS_CODE, "登陆成功", user);

	}
}
