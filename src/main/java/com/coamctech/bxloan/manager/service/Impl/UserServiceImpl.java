package com.coamctech.bxloan.manager.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.UserDao;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.service.UserService;
import com.coamctech.bxloan.manager.utils.encrypt.MD5Util;


@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDao userDao;

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
		return new JsonResult<User>(ResultCode.SUCCESS_CODE, "登陆成功", user);
		
	}

}
