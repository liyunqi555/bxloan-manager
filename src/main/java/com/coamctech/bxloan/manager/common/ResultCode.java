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

    public static final int DB_ERROR_CODE = 602;
    public static final String DB_ERROR_MSG = "数据库操作失败，请稍后再试";

    //Token有误
    public static final int TOKEN_ERROR_CODE = 603;
    public static final String TOKEN_ERROR_MSG = "请重新登陆!";

    public static final int VERIFY_FAIL_CODE = 604;
    public static final String VERIFY_FAIL_MSG = "请输入正确的验证码";

    public static final int USER_EXIST_CODE = 605;
    public static final String USER_EXIST_MSG = "用户已经存在";

    public static final int VERIFYCODE_EXIST = 606;
    public static final String VERIFYCODE_EXIST_MSG = "验证码错误！";

    public static final int USER_NULL_CODE = 607;
    public static final String USER_NULL_MSG = "用户不存在！";

    public static final int PASSWORD_ERR_CODE = 608;
    public static final String PASSWORD_ERR_MSG = "密码错误！";

    public static final int VERIFYCODE_OUT_TIME_CODE = 609;
    public static final String VERIFYCODE_OUT_TIME_MSG = "验证码无效！";

    public static final int SIGN_LEGAL_CODE = 610;
    public static final String SIGN_LEGAL_MSG = "请重新登陆！";

    public static final int TOKEN_TIME_OUT_CODE = 611;
    public static final String TOKEN_TIME_OUT_MSG = "TOKEN 已过期！";
    public static final String DB_OPTION_ERROR_MSG = "操作失败";
    public static final String INPUT_MOBILE_NUMBER_MSG = "请输入手机号";
    public static final String CHECK_MOBILE_NUMBER_MSG = "请核对您的手机号";
    public static final String PASSWORD_NULL_MSG = "密码不能为空";
    public static final String PASSWORD_LENGTH_MSG = "密码长度不合法，请在6-18之间！";
    public static final String TOKEN_NULL_MSG = "TOKEN 不能为空！";
    public static final String TOKEN_LLLEGAL_MSG = "TOKEN 不合法！";
    public static final String LEGAL_MOBILE_NUMBER_MSG = "非法的手机号码！";
    public static final String IM_USER_EXIST_MSG = "环信帐号已经存在！";
    public static final String OLD_PASSWORD_ERR_MSG = "原始密码错误！";
    public static final String USER_NICK_NAME_TOO_LONG = "用户昵称太长！";
    public static final String ADVICE_CONTENT_TOO_LONG = "意见反馈内容太长！";
    public static final String URL_CONTENT_TOO_LONG = "URL太长！";
    public static final String Drive_SIGN_EXIST = "drive exist！";

    /**业务相关错误码：7xx,记录业务级别具体错误，比如登陆密码错误**/
    public static final int ERROR_CODE_701 = 701;
    public static final int ERROR_CODE_702 = 702;
    public static final int ERROR_CODE_703 = 703;
    public static final int ERROR_CODE_704 = 704;
    public static final int ERROR_CODE_705 = 705;
    public static final int ERROR_CODE_706 = 706;
    public static final int ERROR_CODE_707 = 707;
    public static final int ERROR_CODE_708 = 708;
    public static final int ERROR_CODE_709 = 709;

}