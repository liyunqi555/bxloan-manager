package com.coamctech.bxloan.manager.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.coamctech.bxloan.manager.domain.DocInfo;
import com.coamctech.bxloan.manager.service.DocInfoService;

/**   
 * 类名称：DocumentMngController<br/>
 * 类描述 ：<br/>
 * 创建人: zhaoqingwen<br/>
 * 创建时间：2017年12月10日 下午1:57:57<br/>  
 * 修改人：
 * 修改时间：  
 * 修改备注：
 * 版本： V1.0
 */
@Controller
@RequestMapping("/documentMngr")
public class DocumentMngController {
	private DocInfoService docInfoService;
	/**
	 *查找所有文章
	 * @return
	 *
	 * @lastModified zhaoqingwen 2017年12月10日 下午2:02:05  
	 */
	public List<DocInfo> getAllDocInfo(){
		
		return null;
		
	}
	//根据来源查找文章
	//根据栏目查找文章
}
