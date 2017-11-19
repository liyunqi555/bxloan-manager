package com.coamctech.bxloan.manager.controller;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.service.DocInfoService;
import com.coamctech.bxloan.manager.service.UserService;
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
@RequestMapping(value="api/app/home",method = RequestMethod.GET)
public class AppHomeController {
    private static final Logger logger = LoggerFactory.getLogger(AppHomeController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private DocInfoService docInfoService;

    /**
     * 首页bannner
     * @return
     */
    @RequestMapping("banner")
    public JsonResult banner(){
        return docInfoService.banner();
    }
}
