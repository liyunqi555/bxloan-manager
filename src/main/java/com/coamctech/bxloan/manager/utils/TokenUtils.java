package com.coamctech.bxloan.manager.utils;

import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.utils.encrypt.Cryptos;
import com.coamctech.bxloan.manager.utils.encrypt.MD5Util;
import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by ningcl on 15/9/9.
 */
public class TokenUtils {

    private final static String sign = "HerNvb341424OPQCNDL877adfqGLKE)O#OK";

    private static byte[] signBytes = null;

    private final static ThreadLocal<User> threadUsers = new ThreadLocal<User>();

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(TokenUtils.class);

    static {
        try {
            signBytes = sign.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static User sessionUser(){
        return threadUsers.get();
    }

    public static void storeSessionUser(User user){
        threadUsers.set(user);
    }

    public static void clearSession(){
        threadUsers.set(null);
    }

    public static enum TokenType {
         LLLEGAL,TEMP,LOGIN
    }

    public static TokenType tokenType(String token){
        String[] ts = token.split("_");
        if(ts[0].length() != 42){
            return TokenType.LLLEGAL;
        }
        String uidStr = ts[0].substring(32);
        if(uidStr.startsWith("v1")){
            uidStr = uidStr.substring(2);
        }else {
            return TokenType.LLLEGAL;
        }

        try {
            if(!Encodes.encodeHex(Cryptos.hmacSha1(ts[0].getBytes("UTF-8"), signBytes)).equals(ts[1])){
                return TokenType.LLLEGAL;
            }

            long uid = Encodes.bytesToInt(Encodes.decodeHex(uidStr));
            if(uid ==0 ){
                return TokenType.TEMP;
            }else if(uid>0){
                return TokenType.LOGIN;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return TokenType.LLLEGAL;
    }

    // when uid=0 token is temporary,when uid>0 is regular
    public static String token(Integer uid){
        StringBuilder sb = new StringBuilder();
        sb.append(MD5Util.md5Hex(UUID.randomUUID().toString())).append("v1").append(Encodes.encodeHex(Encodes.intToBytes(uid)));
        String tk = sb.toString();
        try {
            sb.append('_').append(Encodes.encodeHex(Cryptos.hmacSha1(tk.getBytes("UTF-8"), signBytes)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static Long uid(String token){
        String[] ts = token.split("_");
        if(ts[0].length() != 42){
            return null;
        }
        String uidStr = ts[0].substring(32);
        if(uidStr.startsWith("v1")){
            uidStr = uidStr.substring(2);
        }else {
            return null;
        }

        try {
            if(!Encodes.encodeHex(Cryptos.hmacSha1(ts[0].getBytes("UTF-8"),signBytes)).equals(ts[1])){
                return null;
            }

            long uid = Encodes.bytesToInt(Encodes.decodeHex(uidStr));
            return uid;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean urlIsLegal(TreeMap<String,String> urls,String signKey){
        StringBuilder sb = new StringBuilder();
        String originSign = urls.get("sign");
        urls.remove("sign");
        if(urls.isEmpty()){
            return false;
        }
        for(Map.Entry<String,String> entry : urls.entrySet()){
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);

        try {
            logger.info("**sign:{},originSign:{}**", Encodes.encodeHex(Cryptos.hmacSha1(sb.toString().getBytes("UTF-8"),signKey.getBytes("UTF-8"))), originSign);
            return Encodes.encodeHex(Cryptos.hmacSha1(sb.toString().getBytes("UTF-8"),signKey.getBytes("UTF-8"))).equals(originSign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static class URLSortedComparator implements Comparator {

        public static URLSortedComparator INSTANCE = new URLSortedComparator();

        @Override
        public int compare(Object o1, Object o2) {
            String str1 = (String)o1;
            String str2 = (String)o2;
            return str1.compareTo(str2);
        }
    }

}
