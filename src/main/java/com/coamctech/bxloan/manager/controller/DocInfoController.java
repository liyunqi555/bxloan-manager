package com.coamctech.bxloan.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/docInfo")
@Controller
public class DocInfoController {
	
	@RequestMapping
	public String index(){
		return "docInfo";
	}

}
