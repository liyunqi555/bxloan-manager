package com.coamctech.bxloan.manager.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;


public class ReportExcelUtils {
	private static Logger logger = LoggerFactory.getLogger(ReportExcelUtils.class);

	/**
	 * 模版加载并且生成导出文件落地
	 * 
	 * @param excelKey doc.properties名称
	 * @param uploadKey doc.properties名称
	 * @param params
	 * @throws BizException
	 */
	public String genernateExcelFileName(InputStream is,String reportPath, String fileName,Map params) throws Exception {
		String nowDate = CommonHelper.getNowStr(CommonHelper.DF_DATE) + ".xlsx";
		fileName += nowDate;
		try {
			// 模板加载
			XLSTransformer transformer = new XLSTransformer();
			Workbook resultWorkbook = transformer.transformXLS(is, params);
			is.close();
			if (StringUtils.isNotBlank(reportPath)) {
				// 主目录不存在，新建
				java.io.File myFilePath = new java.io.File(reportPath);
				if (!myFilePath.exists()) {
					myFilePath.mkdirs();
				}
			}
			reportPath = reportPath + fileName;
			File file = new File(reportPath);
			if (!file.exists()) {
				// 文件不存在，新建
				file.createNewFile();
			}
			OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
			resultWorkbook.write(os);
			os.flush();
			os.close();
			return fileName;
		}
		catch (Exception eIo){
			if (logger.isInfoEnabled()) {
				logger.info("===========报表落地异常:" + eIo.getMessage());
			}
			eIo.printStackTrace();
		}
		return fileName;
	}
	
	 
	 public static  void downloadFile(File file,String fileName,HttpServletRequest request,HttpServletResponse response) throws IOException{
	        // 弹出下载对话框(以附件形式打开文件流)
	        String agent = (String) request.getHeader("USER-AGENT");
	        if (agent != null && agent.toUpperCase().indexOf("FIREFOX") >-1) {
	            response.setHeader(
	                    "Content-Disposition",
	                    "attachment; filename=" +  
	                    new String(fileName.getBytes("utf-8"),"ISO8859_1"));
	        } else {
	            response.setHeader( 
	                    "Content-Disposition", 
	                    "attachment; filename=" +
	                            toUtf8String(fileName));
	        }
	 
	        OutputStream stream=response.getOutputStream();
	        outputFile(file, stream);
	  }
	 
	  public static void outputFile(File file,OutputStream stream) throws IOException{
	        BufferedInputStream bis = null;
	        BufferedOutputStream bos = null;
	        int bytesRead = 0;
	        byte[] buffer = new byte[8192];
	        try {
	        	FileInputStream fs = new FileInputStream(file);
	            bis = new BufferedInputStream(fs);
	            bos = new BufferedOutputStream(stream);
	            while ((bytesRead = bis.read(buffer, 0, 8192)) != -1) {
	                bos.write(buffer, 0, bytesRead);
	            }
	            bos.flush();
	        }catch(Exception e){
	        	e.printStackTrace();
	        }finally {
	            bis.close();
	            bos.close();
	        }
	    }
	  
	  private static String toUtf8String(String s) {
	        StringBuffer sb = new StringBuffer();
	        for (int i=0;i<s.length();i++) {
	            char c = s.charAt(i);
	            if (c >= 0 && c <= 255) {
	                sb.append(c);
	            } else {
	                byte[] b;
	                try {
	                    b = Character.toString(c).getBytes("utf-8");
	                } catch (Exception ex) {
	                    b = new byte[0];
	                }
	                for (int j = 0; j < b.length; j++) {
	                    int k = b[j];
	                    if (k < 0) k += 256;
	                    sb.append("%" + Integer.toHexString(k).toUpperCase());
	                }
	            }
	        }
	        return sb.toString();
	 
	    }
}
