package com.coamctech.bxloan.manager.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coamctech.bxloan.manager.common.DataTablesPage;
import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.service.IColumnService;
import com.coamctech.bxloan.manager.service.VO.DocColumnVO;

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
    	return "docColumnMng/main";
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
    @RequestMapping("/getColumnOne")
    @ResponseBody
    public  JsonResult getColumnOne(Long  id) {
    	try{
    		DocColumnVO doc  = columnServiceImpl.findColumn(id);
    		return new  JsonResult(ResultCode.SUCCESS_CODE,"",doc);
    	}catch(Exception e){
    		return new  JsonResult(ResultCode.ERROR_CODE,"");
    	}
     
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
    public  JsonResult deleteColumn(Long  id) {
    	try{
    		JsonResult result = this.columnServiceImpl.validateDelte(id);
    		if(null!=result &&result.getCode()!=200){
    			return new  JsonResult(result.getCode(),result.getMsg());
    		}
    		columnServiceImpl.deleteColumn(id);
    		return new  JsonResult(ResultCode.SUCCESS_CODE,"删除成功");

    	}catch(Exception e){
    		return new  JsonResult(ResultCode.ERROR_CODE,"删除失败");
    	}
     
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
    public  JsonResult addColumn(@ModelAttribute DocColumn docColumn, @RequestParam(required = false) String userIds,@RequestParam(required = false) String sourceIds,HttpServletRequest  request) {
    	JsonResult result = new JsonResult();
    	try{
    		User curUser = (User)request.getSession().getAttribute("user");
    		columnServiceImpl.addColumn(docColumn,userIds,sourceIds,curUser.getId());
    		return new  JsonResult(ResultCode.SUCCESS_CODE,"保存成功");

    	}catch(Exception e){
    		return new  JsonResult(ResultCode.ERROR_CODE,"保存失败");
    	}
    }
    /**
     * 查询合并单据列表信息
     * @param sEcho  datatables的被请求次数
     * @param pageNumber  起始页数
     * @param pageSize  每页多少条记录
     * @param orgId		机构Id
     * @param projectType 单据业务类型(002 逾期 003还款  004结息)
     * @param customerName 客户姓名
     * @param contractNum  合同编号
     * @return DataTablesPage DataTablesPage对象
     */
	@RequestMapping("/findColumnList")
	@ResponseBody
	public DataTablesPage findColumnList(@RequestParam("sEcho") Integer sEcho,
			@RequestParam("iDisplayStart") Integer pageNumber,
			@RequestParam("iDisplayLength") Integer pageSize,@RequestParam( value="docColumName",required=false) String docColumName 
			){
		/**查询导入的记录信息*/
		Page<DocColumnVO> page = columnServiceImpl.findColumnList(pageNumber / pageSize, pageSize, docColumName,null);
		/**处理并返回查询结果*/
		return new DataTablesPage(sEcho, page);
	}
}
