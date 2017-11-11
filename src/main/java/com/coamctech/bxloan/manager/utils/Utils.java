package com.coamctech.bxloan.manager.utils;

import com.alibaba.fastjson.JSONObject;
import com.coamctech.bxloan.manager.utils.encrypt.Digests;
import com.coamctech.bxloan.manager.utils.encrypt.MD5Util;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 工具类
 */
public class Utils {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 得到显示时间
     * @return   yyyy-MM-dd HH:mm:ss
     */
    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }
        return sdf.format(date);
    }

    /**
     * 根据显示时间得到日期
     * @param time  yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date stringToDate(String time) {
        if (StringUtils.isBlank(time)) {
            return null;
        }
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    /**
     * money为空，返回 showNameIfNull
     * money大于等于10000元，显示 xxx万元，有小数点保留两位，否则不保留小数
     * 否则返回xxx元
     * @param money
     * @return
     */
    public static String formatStrMoney(String money,String showNameIfNull){
        String sMoney = "";
        try {
            if (money != null && money != "" && money.length() > 0) {
                Double allowance = Double.valueOf(money);
                if (allowance > 10000 || allowance == 10000) {
                    Long wance = Math.round(allowance / 1000);
                    allowance = (Double.valueOf(wance.toString())) / 10;
                    if (allowance == Math.floor(allowance)) {
                        sMoney = (int) Math.floor(allowance) + "万";
                    } else {
                        sMoney = allowance.toString() + "万";
                    }
                } else {
                    sMoney = money;
                }
            } else {
                sMoney = showNameIfNull;
            }
        }catch (Exception e){
           sMoney="暂无";
        }
        return sMoney;
    }
    public static String formatBigDecimalMoney(BigDecimal money){
        DecimalFormat df = new DecimalFormat("0.##");
        if(money==null){
            return "0";
        }
        if(money.compareTo(new BigDecimal("10000"))>-1) {
           return df.format(money.divide(new BigDecimal("10000"))) + "万";
        } else {
            return money.toString();
        }
    }

    /**
     * 将对象转成json串
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        if (obj == null)
            return null;
        JSONObject result = new JSONObject();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int j = 0; j < fields.length; j++) {
            fields[j].setAccessible(true);
            // 字段名
            String key = fields[j].getName();
            // 字段类型
            String type = fields[j].getType().getName();
            try {
                // 字段值
                Object value = fields[j].get(obj);
                if (value != null) {
                    //如果是Date类型，转换成 yyyy-MM-dd HH:mm:ss
                    if (type.equalsIgnoreCase("java.util.Date")) {
                        value = Utils.dateToString((Date) value);
                    }
                    result.put(key, value.toString());
                } else {
                    //如果是Integer类型，转换成0
                    if (type.equalsIgnoreCase("java.lang.Integer") || type.equalsIgnoreCase("java.lang.Long")) {
                        value = 0;
                        result.put(key, value.toString());
                    }
                }
            } catch (Exception e) {
                System.out.println("object field transfer failed, but it`s ok. field = " + key + ", type = " + type);
            }
        }
        return JSONObject.toJSONString(result);
    }

    /**
     * 将json串转成对象
     * @param json
     * @param clazz
     * @return
     */
    public static Object fromJson(String json, Class clazz) {
        if (StringUtils.isNotEmpty(json)) {
            return JSONObject.parseObject(json, clazz);
        }
        return null;
    }

    /**
     * 将newObj中待更新的字段值，填充到oldObj中做替换
     * @param oldObj
     * @param newObj
     * @return  更新后的obj
     */
    public static Object fillObj(Object oldObj, Object newObj) {
        Field[] oldf = oldObj.getClass().getDeclaredFields();
        Field[] newf = newObj.getClass().getDeclaredFields();
        for (int j = 0; j < oldf.length; j++) {
            try {
                newf[j].setAccessible(true);
                if (newf[j].get(newObj) != null) {
                    oldf[j].setAccessible(true);
                    oldf[j].set(oldObj, newf[j].get(newObj));
                }
            } catch (IllegalAccessException e) {
                System.out.println("object field transfer failed, but it`s ok. field = " + newf[j].getName() + ", type = " + newf[j].getType().getName());
            }
        }
        return oldObj;
    }


    /**
     * 生成随机盐
     * @return
     */
    public static String generateSalt() {
        return Encodes.encodeHex(Digests.generateSalt(4));
    }

    /**
     * 密码加密
     * @param salt
     * @param password
     * @return
     */
    public static String encryptPassword(String salt, String password) {
        return MD5Util.sha256Hex(salt, password);
    }

    public static void main(String[] args) {
        String ip = "192.168.1.1,192.168.1.2";
        String[] arr = ip.split(",");
        ip = arr[0];
        System.out.println();;

/*

        String salt = generateSalt();
        String password = encryptPassword(salt, "111111");

        System.out.println("salt="+salt);
        System.out.println("password="+password);
*/
    }

    public static String genToken() {
        try {
            return DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes("UTF-8")).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final String ZS_TOKEN_PRE = "ZS-";
    public static final String LS_TOKEN_PRE = "LS-";
    public static final String LS_TOKEN = "L";
    public static final String DL_TOKEN = "X";

    public static String genZSToken() {
        String token = genToken();
        if(token != null) {
            return ZS_TOKEN_PRE + token;
        }
        return token;
    }

    public static String genLSToken() {
        String token = genToken();
        if(token != null) {
            return LS_TOKEN_PRE + token;
        }
        return token;
    }

    /**
     * 获得 token true 登陆 token, false 未登陆 token
     * */
    public static String getToken(Boolean b) {

        String token = genToken();
        if(b) {

            return DL_TOKEN+token;

        } else {

            return LS_TOKEN+token;

        }
    }

    /**
     * 验证手机号码，11位数字
     * @param mobile
     * @return
     */
    public static boolean checkMobile(String mobile) {
        if (mobile == null) {
            return false;
        }
        String regex = "\\d{11}";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 检查ip
     * @param ip
     * @return
     */
    public static boolean checkIp(String ip) {
        if (ip == null) {
            return false;
        }
        String regex = "^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$";
        return ip.matches(regex);
    }


    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String[] arr = ip.split(",");
        ip = arr[0];
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }
}
