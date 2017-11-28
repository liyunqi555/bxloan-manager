package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.DeviceDao;
import com.coamctech.bxloan.manager.dao.UserDao;
import com.coamctech.bxloan.manager.domain.Device;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.domain.UserStore;
import com.coamctech.bxloan.manager.utils.*;
import com.coamctech.bxloan.manager.utils.encrypt.Digests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import javax.transaction.Transactional;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/12.
 */
@Service
@Transactional
public class AppUserService  extends BaseService<User,Long> {
    @Autowired
    private UserDao userDao;
    @Autowired
    private DeviceDao deviceDao;

    public JsonResult login(String userName, String password, String deviceCode) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password) || StringUtils.isBlank(deviceCode)) {
            return new JsonResult(ResultCode.PARAM_ERROR_CODE, ResultCode.PARAM_ERROR_MSG);
        }
        userName = userName.trim();
        User user = userDao.findByUserName(userName);
        //验证用户非空
        if (user == null) {
            return new JsonResult(ResultCode.USER_NULL_CODE, ResultCode.USER_NULL_MSG);
        }
        //验证密码
        if (!user.getPassword().equals(password)) {
            return new JsonResult(ResultCode.PASSWORD_ERR_CODE, ResultCode.PASSWORD_ERR_MSG);
        }
        Device device = deviceDao.findByDeviceCode(deviceCode);
        if (device != null && !device.getUserId().equals(user.getId())) {
            return new JsonResult(ResultCode.PARAM_ERROR_CODE, ResultCode.DEVICE_CODE_ERROR_MSG);
        }
        if (device == null) {
            device = new Device();
            device.setUserId(user.getId());
            device.setDeviceCode(deviceCode);
            device.setCreateTime(new Date());
            deviceDao.save(device);
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
        return new JsonResult(ResultCode.SUCCESS_CODE, ResultCode.SUCCESS_MSG, nUser);
    }

    public JsonResult changePassword(User user, String oldPassword, String newPassword) {
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            return new JsonResult(ResultCode.PARAM_ERROR_CODE, ResultCode.PARAM_ERROR_MSG);
        }
        if (!user.getPassword().equals(oldPassword)) {
            return new JsonResult(ResultCode.PASSWORD_ERR_CODE, ResultCode.PASSWORD_ERR_MSG);
        }
        User user1 = userDao.findOne(user.getId());
        if (user == null) {
            return new JsonResult(ResultCode.USER_NULL_CODE, ResultCode.USER_NULL_MSG);
        }
        user1.setPassword(newPassword);
        user1.setUpdateTime(new Date());
        userDao.save(user1);
        return JsonResult.success();
    }
}