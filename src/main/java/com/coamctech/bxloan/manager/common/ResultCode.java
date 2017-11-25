package com.coamctech.bxloan.manager.common;

import java.io.Serializable;

public class ResultCode implements Serializable {
    //成功
    public static final int SUCCESS_CODE = 200;
    public static final String SUCCESS_MSG = "操作成功";

    //失败
    public static final int ERROR_CODE = 500;
    public static final String ERROR_MSG = "服务器内部错误，请重试";

    /**通用错误码：6xx,记录业务级别通用错误，比如：数据库错误**/
    public static final int PARAM_ERROR_CODE = 601;
    public static final String PARAM_ERROR_MSG = "参数错误，请检查参数";
    public static final String TOKEN_NULL_MSG = "参数错误，没有TOKEN";
    public static final String DEVICE_CODE_ERROR_MSG = "该设备已登录其它账号，不能再登录该账号";
    public static final int USER_NULL_CODE = 602;
    public static final String USER_NULL_MSG = "用户不存在或密码错误";
    public static final int PASSWORD_ERR_CODE = 603;
    public static final String PASSWORD_ERR_MSG = "用户不存在或密码错误";

    /**业务相关错误码：7xx,记录业务级别具体错误，比如登陆密码错误**/
    public static final int ERROR_CODE_701 = 701;
    public static final int ERROR_CODE_702 = 702;

}