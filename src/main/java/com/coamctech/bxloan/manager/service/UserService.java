package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.UserDao;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.utils.*;
import com.coamctech.bxloan.manager.utils.encrypt.Digests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.transaction.Transactional;

/**
 * Created by Administrator on 2017/11/12.
 */
@Service
@Transactional
public class UserService {
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserDao userDao;

    public JsonResult login(String userName,String password){
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,ResultCode.PARAM_ERROR_MSG);
        }
        User user = userDao.findByUserName("admin");
        //验证用户非空
        if (user==null) {
            return new JsonResult(ResultCode.USER_NULL_CODE,ResultCode.USER_NULL_MSG);
        }
        //验证密码
        if(!user.getPassword().equals(password)) {
            return new JsonResult(ResultCode.PASSWORD_ERR_CODE,ResultCode.PASSWORD_ERR_MSG);
        }
        //重置签名
        String sign = DigestUtils.md5DigestAsHex(Encodes.encodeHex(Digests.generateSalt(4)).getBytes()).toUpperCase();
        user.setSign(sign);
        userDao.save(user);

        User nUser = CopyUtils.clone(user, new User(), tuser -> {
            tuser.setPassword(null);
        });
        String token = TokenUtils.token(user.getId().intValue());
        nUser.setToken(token);
        return new JsonResult(ResultCode.SUCCESS_CODE,ResultCode.SUCCESS_MSG, nUser);
    }
}
