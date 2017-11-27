package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.JsonResult;

public interface UserService {

    public JsonResult ifLoginSuccess(String userName,String password);
}
