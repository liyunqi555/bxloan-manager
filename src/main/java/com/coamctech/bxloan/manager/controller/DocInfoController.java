package com.coamctech.bxloan.manager.controller;

import com.coamctech.bxloan.manager.service.DocInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.coamctech.bxloan.manager.dao.DocInfoDao;
import com.coamctech.bxloan.manager.domain.DocInfo;

@RequestMapping("/docInfo")
@Controller
public class DocInfoController {
	
	@Autowired
	private DocInfoDao docInfoDao;
	@Autowired
	private DocInfoService docInfoService;

	@RequestMapping
	public String index(){
		return "docInfo";
	}
	
    @RequestMapping(value="/article/{docId}")
    public String index2(Model model,@PathVariable("docId") Long docId){
    	DocInfo doc = docInfoDao.findOne(docId);
		doc = docInfoService.parseImgUrl(doc);
    	if(StringUtils.isNotBlank(doc.getCnBoty())){
    		model.addAttribute("body", doc.getCnBoty());
    	}else{
    		model.addAttribute("body", doc.getBody());
    	}
    	if(StringUtils.isNotBlank(doc.getCnTitle())){
        	model.addAttribute("title", doc.getCnTitle());
    	}else{
    		model.addAttribute("title", doc.getTitle());
    	}
    	return "article";
    }

}
