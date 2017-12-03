package com.coamctech.bxloan.manager.controller;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.service.AppUserService;
import com.coamctech.bxloan.manager.service.UserService;
import com.coamctech.bxloan.manager.utils.TokenUtils;
import com.coamctech.bxloan.manager.utils.encrypt.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Created by Administrator on 2017/10/20.
 */
@RestController
@RequestMapping(value="api/app/user",method = RequestMethod.POST)
public class AppUserController extends AppBaseController{
    @Autowired
    private AppUserService appUserService;
    /**
     * 登陆接口
     * @param userName 用户名
     * @param password MD5(密码)
     * @return User 对象，包括token ，之后每次请求都带着token，
     */
    @RequestMapping("anon/login")
    public JsonResult login(@RequestParam(name="userName") String userName,
                            @RequestParam(name="password") String password,
                            @RequestParam(name="deviceCode") String deviceCode){
        return appUserService.login(userName,password,deviceCode);
    }

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @RequestMapping("changePassword")
    public JsonResult changePassword(@RequestParam(name="oldPassword") String oldPassword,
                                     @RequestParam(name="newPassword") String newPassword){
        User user = TokenUtils.sessionUser();
        return appUserService.changePassword(user,oldPassword, newPassword);
    }
    /**
     * 开关浏览记录
     * @return
     */
    @RequestMapping("switchViewHistory")
    public JsonResult switchViewHistory(){
        User user = TokenUtils.sessionUser();
        return appUserService.switchViewHistory(user.getId());
    }
    /**
     * 最新版本数据
     * @return
     */
    @RequestMapping("lastVersion")
    public JsonResult lastVersion(){
        return appUserService.lastVersion();
    }
    public static void main(String[] args) {
        System.out.println(MD5Util.md5Hex("12345678"));
    }
}
