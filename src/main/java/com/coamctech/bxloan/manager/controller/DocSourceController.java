package com.coamctech.bxloan.manager.controller;

import com.coamctech.bxloan.manager.common.DataTablesPage;
import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.domain.DocSource;
import com.coamctech.bxloan.manager.domain.Role;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.service.DocSourceMngService;
import com.coamctech.bxloan.manager.service.RoleMngService;
import com.coamctech.bxloan.manager.service.VO.DocSourceVO;
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
 * Created by Administrator on 2017/10/20.
 */
@Controller
@RequestMapping("/docSourceMng")
public class DocSourceController {
    private static final Logger logger = LoggerFactory.getLogger(DocSourceController.class);
    
    @Autowired
    private DocSourceMngService docSourceMngService;
    /**
     * 来源管理主页面
     */
    @RequestMapping
    public String index(Model model,HttpSession session){
        return "docSourceMng/docSource";
    }
    /**
     * 角色管理主页面初始化
     */
    @RequestMapping("/findByCondition")
    @ResponseBody
    public DataTablesPage findBySearch(@RequestParam("sEcho") Integer sEcho,
                                       @RequestParam("iDisplayStart") Integer firstIndex,
                                       @RequestParam("iDisplayLength") Integer pageSize,
                                       @RequestParam("roleName") String roleName) {
        //当前登录用户
        Page<DocSourceVO> page = null;
        try {
            page = docSourceMngService.findBySearch(firstIndex/pageSize, pageSize,roleName);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new DataTablesPage(sEcho, page);

    }
    /**
     * 角色新增
     */
    @RequestMapping(value="add")
    @ResponseBody
    public JsonResult add(DocSourceVO docSourceVO,HttpServletRequest request){
        try {
            User curUser = (User)request.getSession().getAttribute("user");
            JsonResult r = docSourceMngService.addDocSource(docSourceVO, curUser);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new JsonResult(ResultCode.ERROR_CODE,"服务器异常");
        }


    }

}
