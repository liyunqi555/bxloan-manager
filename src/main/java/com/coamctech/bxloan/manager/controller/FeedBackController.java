package com.coamctech.bxloan.manager.controller;

import com.coamctech.bxloan.manager.common.DataTablesPage;
import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.RoleDao;
import com.coamctech.bxloan.manager.domain.FeedBack;
import com.coamctech.bxloan.manager.domain.Role;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.service.FeedBackService;
import com.coamctech.bxloan.manager.service.RoleMngService;
import com.coamctech.bxloan.manager.service.VO.RoleVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 意见反馈后台管理
 */
@Controller
@RequestMapping("/feedBackMng")
public class FeedBackController {
    private static final Logger logger = LoggerFactory.getLogger(FeedBackController.class);
    
    @Autowired
    private FeedBackService feedBackService;
    @Autowired
    private RoleMngService roleMngService;

    /**
     *意见反馈列表
     */
    @RequestMapping("/findByCondition")
	@ResponseBody
	public DataTablesPage findBySearch(@RequestParam("sEcho") Integer sEcho,
			@RequestParam("iDisplayStart") Integer firstIndex,
			@RequestParam("iDisplayLength") Integer pageSize,
			@RequestParam("roleName") String roleName) {
	    //当前登录用户
 		Page<FeedBack> page = null;
        try {
            page = feedBackService.findBySearch(firstIndex / pageSize, pageSize, roleName);
        } catch (ParseException e) {
            e.printStackTrace();
        }
	 	return new DataTablesPage(sEcho, page);
    }
    
    /**
     * 意见反馈主页面
     */
    @RequestMapping
    public String index(Model model,HttpSession session){
    	return "feedBack";
    }

    /**
     *
     * @param id
     * @return
     */
    @RequestMapping("/initModalData")
    @ResponseBody
    public JsonResult initModalData(Long id ){
    	try{
        	Role role = roleMngService.getById(id);
        	return new JsonResult(ResultCode.SUCCESS_CODE,"初始化弹窗成功",role);
    	}catch (Exception e) {
    		e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResult(ResultCode.ERROR_CODE,"初始化弹窗失败");
    	}
    }
}
