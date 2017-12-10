package com.coamctech.bxloan.manager.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.service.IColumnService;
import com.coamctech.bxloan.manager.service.Impl.ColumnServiceImpl;

/**   
 * 类名称：CloumnManagementController<br/>
 * 类描述 ：栏目管理控制类<br/>
 * 创建人: zhaoqingwen<br/>
 * 创建时间：2017年12月7日 下午3:41:53<br/>  
 * 修改人：
 * 修改时间：  
 * 修改备注：
 * 版本： V1.0
 */
@Controller
@RequestMapping("/docColumnMng")
public class ColumnManagementController {
	@Autowired
	private IColumnService  columnServiceImpl;
	
    /**
     *初始化
     * @return
     *
     * @author zhaoqingwen
     * @lastModified zhaoqingwen 2017年12月7日 下午3:43:45  
     */
	@RequestMapping
    public String init(Model model,HttpSession session){
    	return "/docColumnMng/main";
    }
    
 
    /**
     *获取栏目菜单
     * @return
     *
     * @author zhaoqingwen
     * @lastModified zhaoqingwen 2017年12月7日 下午3:53:07  
     */
    @RequestMapping("/getAllColumn")
    @ResponseBody
    public List<DocColumn> getAllColumnList() {
    	 List<DocColumn> lsit  = columnServiceImpl.getAllColumnList();
		return lsit;
     
    }
    /**
     *修改栏目
     * @param columnId
     * @return
     *
     * @author zhaoqingwen
     * @lastModified zhaoqingwen 2017年12月7日 下午3:53:22  
     */
    @RequestMapping("/modifyColumn")
    @ResponseBody
    public  JsonResult modifyColumn(String columnId) {
		return null;
     
    }
    /**
     *删除栏目
     * @param columnId
     * @return
     *
     * @author zhaoqingwen
     * @lastModified zhaoqingwen 2017年12月7日 下午3:53:31  
     */
    @RequestMapping("/deleteColumn")
    @ResponseBody
    public  JsonResult deleteColumn(String columnId) {
		return null;
     
    }
    /**
     *增加栏目
     * @return
     *
     * @author zhaoqingwen
     * @lastModified zhaoqingwen 2017年12月7日 下午3:53:39  
     */
    @RequestMapping("/addColumn")
    @ResponseBody
    public  JsonResult addColumn(@ModelAttribute DocColumn individual,HttpServletRequest request) {
    	String a = "a";
		return null;
    	
    }
}
