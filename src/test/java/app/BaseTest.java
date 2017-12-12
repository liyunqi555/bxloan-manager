package app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.coamctech.bxloan.manager.utils.Encodes;
import com.coamctech.bxloan.manager.utils.TokenUtils;
import com.coamctech.bxloan.manager.utils.encrypt.Cryptos;
import com.coamctech.bxloan.manager.utils.encrypt.MD5Util;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {
	private static Logger logger = LoggerFactory.getLogger(BaseTest.class);
	protected static final String base = "http://localhost:8081/";
//   protected static final String base = "http://211.99.230.29:8096/";
    //admin
    protected static String token = "ee859669ff48b631da9401687e250c3cv101000000_788fc2920c99964013f1703d99aa7394ba61eada";
    //vip1
//    protected static String token = "C667EFAA2F3B2D80D1D2184519813E57";
    protected static String sign = "C667EFAA2F3B2D80D1D2184519813E57";
    public static void main(String[] args) {
     login();
        //login_vip1();
//        topColumns();
//       banner();
        //customColumn();
//
        haveCustomDocColumns();
        //noCustomDocColumns();
//        switchOrder();
//        lastVersion();
//        cancelCustomColumn();
//        worlds();
//        docInfos();
//        search();
//        docInfoDetail();
//        store();
//        cancelStore();
//        myStore();
//        myHistory();
        dataCenter_home();
        dataCenter_detail();
        //dataCenter_store();
//        dataCenter_myStore();
//        changePassword();
//        switchViewHistory();
        saveFeedBack();
    }
    public static void saveFeedBack(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("content","首页轮播图点击进去为什么不能收藏？"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/user/saveFeedBack");
    }
    public static void dataCenter_myStore(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("pageIndex","0"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/dataCenter/myStore");
    }
    public static void dataCenter_store(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("conceptUri","人物"));
        nvps.add(new NameValuePair("entityid","曹操"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/dataCenter/store");
    }
    public static void dataCenter_detail(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        //nvps.add(new NameValuePair("conceptUri","人物"));
       // nvps.add(new NameValuePair("entityid","曹操"));
        nvps.add(new NameValuePair("conceptUri","国家"));
        nvps.add(new NameValuePair("entityid","中华人民共和国"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/dataCenter/detail");
    }

    public static void dataCenter_home(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("conceptUri","国家"));
        nvps.add(new NameValuePair("propertyUri","亚洲"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/dataCenter/home");
    }
    public static void myHistory(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("pageIndex","0"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/home/myHistory");
    }
    public static void myStore(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("pageIndex","0"));
        nvps.add(new NameValuePair("topLevelColumnId","1"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/home/myStore");
    }
    public static void cancelStore(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("idsStr","1"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/home/cancelStore");
    }
    public static void store(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("docInfoId","19"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/home/store");
    }
    public static void docInfoDetail(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("docInfoId","18"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/home/docInfoDetail");
    }
    public static void search(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("pageIndex","0"));
        nvps.add(new NameValuePair("keyword","关键的1"));
        nvps.add(new NameValuePair("topLevelColumnId","1"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/home/search");
    }
    public static void docInfos(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("pageIndex","1"));
        nvps.add(new NameValuePair("columnId","4"));
        nvps.add(new NameValuePair("topLevelColumnId","1"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/home/docInfos");
    }
    public static void switchOrder(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("customColumnIdOne","6"));
        nvps.add(new NameValuePair("customColumnIdTwo","7"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/home/switchOrder");
    }
    public static void haveCustomDocColumns(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("topLevelColumnId","1"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/home/haveCustomDocColumns");
    }
    public static void cancelCustomColumn(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("columnId","7"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/home/cancelCustomColumn");
    }
    public static void customColumn(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("columnId","4"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/home/customColumn");
    }
    public static void noCustomDocColumns(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("topLevelColumnId","1"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/home/noCustomDocColumns");
    }
    public static void worlds(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("pageIndex","0"));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/home/worlds");
    }
    public static void banner(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/home/banner");
    }
    public static void topColumns(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/home/topColumns");
    }
    protected static void lastVersion(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/user/lastVersion");
    }
    protected static void switchViewHistory(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/user/switchViewHistory");
    }
    protected static void changePassword(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("oldPassword", MD5Util.md5Hex("123456789")));
        nvps.add(new NameValuePair("newPassword", MD5Util.md5Hex("12345678")));
        addTokenAndSign(nvps);
        String res = post(nvps,"api/app/user/changePassword");
    }
    protected static void login_vip1(){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("userName", "vip1"));
        nvps.add(new NameValuePair("password", MD5Util.md5Hex("12345678")));
        nvps.add(new NameValuePair("deviceCode", "123456789abcdefvip1"));
        String res = post(nvps,"api/app/user/anon/login");
    }
    protected static void login(){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new NameValuePair("userName", "admin"));
		nvps.add(new NameValuePair("password", MD5Util.md5Hex("12345678")));
		nvps.add(new NameValuePair("deviceCode", "123456789abcdef9"));
		String res = post(nvps,"api/app/user/anon/login");
        JSONObject jo = JSONObject.parseObject(res);
        token=jo.getJSONObject("body").getString("token");
        sign = jo.getJSONObject("body").getString("sign");

	}
    public static void addTokenAndSign(List<NameValuePair> nvps){
        Map<String,String> params = new HashMap<String,String>();
        nvps.forEach(nvp -> {
            params.put(nvp.getName(), nvp.getValue());
        });
        params.put("token",token);
        nvps.add(new NameValuePair("token", token));
        nvps.add(new NameValuePair("sign", createSign(params, sign)));
    }
	public static String post(List<NameValuePair> nvps,String uri){
		uri = base+uri;
		logger.info("uri={}",uri);
		logger.info("params={}",nvps.toString());
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(uri);
		method.addParameters(nvps.toArray(new NameValuePair[nvps.size()]));
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		String result = "";
		try {
			client.executeMethod(method);
			result = method.getResponseBodyAsString();
			logger.info("response status code = {} ",method.getStatusCode());
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		};
		logger.info(result);
		logger.info("===============================================================================================================");
		logger.info(formatJson(result));

		return result;
	}
	 /**
     * 格式化
     * @param jsonStr
     * @return
     */
    private static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);

            //遇到{ [换行，且下一行缩进
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;

              //遇到} ]换行，当前行缩进
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;

               //遇到,换行
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }
 
        return sb.toString();
    }

	/**
	 * 生成签名
	 * 规则:
	 * 		1.请求参数名按字符排序
	 * 		2.按如下格式拼接成字符串str:
	 * 			param1=value1&param2=value2&param3=value3
	 * 		3.使用HMAC-SHA1算法根据签名秘钥对拼接后的字符串进行加密,生成一个字节数组
	 * 		4.对加密后返回的字节数组转换成小写的十六进制字符串
	 *
	 * @params 请求参数 包括token
	 *	signKey 签名秘钥 登陆后返回
	 */

	public static String createSign(Map<String,String> params,String signKey){
		if(params==null || params.size()==0){
			return "";
		}
		TreeMap<String,String> treemap = new TreeMap<>(TokenUtils.URLSortedComparator.INSTANCE);
		params.entrySet().forEach(entry -> {
			treemap.put(entry.getKey(), entry.getValue());
		});
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String,String> entry : treemap.entrySet()){
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		sb.deleteCharAt(sb.length() - 1);
        logger.info("sb={},signKey={}",sb.toString(),signKey);
		try {
			return Encodes.encodeHex(Cryptos.hmacSha1(sb.toString().getBytes("UTF-8"),signKey.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
    /**
     * 添加space
     * @param sb
     * @param indent
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
}
