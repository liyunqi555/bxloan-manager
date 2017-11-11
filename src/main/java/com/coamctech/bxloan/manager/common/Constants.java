package com.coamctech.bxloan.manager.common;


import com.coamctech.bxloan.manager.utils.Encodes;

/**
 * Created by tiansj on 15/7/16.
 */
public class Constants {

    // shiro redis cache prefix
    public static String SHIRO_REDIS_SESSION = "shiro_redis_session:";
    public static String SHIRO_REDIS_CACHE = "shiro_redis_cache:";
    public static String SESSSION_USER = "SESSION_USER";

    public static final int USER_STATUS_ENABLE = 1;
    public static final int USER_STATUS_DISABLE = 2;

    public static final int ROLE_STATUS_ENABLE = 1;
    public static final int ROLE_STATUS_DISABLE = 2;


    public static final String SYSTEM_NAME = "security";

    //security_token_temp表的type值
    public static final String SECURITY_QUESTION_DEFAULT = "您母亲的名字？";

    public static final String SALT_LOGIN  = "(huayuan.login)";
    public static final String SALT_SECURITY  = "(huayuan.security)";
    public static final String SALT_ANSWER  = "(huayuan.answer)";
    public static final String SALT_EMAIL_URL  = "(huayuan.email.url)";
    public static final String SALT_ID_NUMBER  = "7ac63dc67cc712be0f94847ff915bbd3";
    public static final byte[] SALT_ID_NUMBER_KEY = Encodes.decodeHex(SALT_ID_NUMBER);

    //发送短信、email时，取不到uid，则取默认uid
    public static final Long DEFALT_UID = 0L;

    public static final int PWD_TYPE_LOGIN = 0;
    public static final int PWD_TYPE_SECURITY = 1;

    public static final int PWD_INCORRECT = 0;
    public static final int PWD_CORRECT = 1;
    public static final int PWD_LOCKED = 2;

    public static final int USER_FORBID_STATUS_INIT = 0;
    public static final int USER_FORBID_STATUS_NORMAL = 1;
    public static final int USER_FORBID_STATUS_FORBID = 2;
}
