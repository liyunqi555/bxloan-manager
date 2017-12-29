package com.coamctech.bxloan.manager.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.service.VO.DocColumnVO;

/**   
 * 类名称：IColumnService<br/>
 * 类描述 ：栏目管理接口<br/>
 * 创建人: zhaoqingwen<br/>
 * 创建时间：2017年12月7日 下午3:54:46<br/>  
 * 具体方法：
 * 	1.获取栏目菜单
 * 	2.修改栏目
 * 	3.删除栏目
 * 	4.新增栏目
 * 版本： V1.0
 */

public interface IColumnService {
	 /**
     *获取栏目菜单
     * @return
     *
     * @lastModified zhaoqingwen 2017年12月7日 下午3:53:07  
     */
    public  JsonResult getAllColumn();
    
    /**
     *修改栏目
     * @param id 
     * @param columnId
     * @return
     *
     * @lastModified zhaoqingwen 2017年12月7日 下午3:53:22  
     */
    public  JsonResult modifyColumn(DocColumn docColumn, Long id);
    
    /**
     *删除栏目
     * @param id
     *
     * @lastModified zhaoqingwen 2017年12月7日 下午3:53:22  
     */
    public  void deleteColumn(Long id);
    
    /**
     *新增栏目
     * @param sourceIds  来源ID
     * @param userIds  用户ID
     * @param loginId 登陆人ID
     * @param columnId
     * @return
     *
     * @lastModified zhaoqingwen 2017年12月7日 下午3:53:22  
     */
    public  JsonResult addColumn(DocColumn docColumn, String userIds, String sourceIds, Long loginId);

	/**
	 *获取栏目菜单
	 * @return List集合
	 *
	 * @lastModified zhaoqingwen 2017年12月7日 下午4:27:58  
	 */
	List<DocColumn> getAllColumnList();

	/**
	 *查找所有栏目
	 * @param i
	 * @param pageSize
	 * @param name
	 * @param loginUserId
	 * @return
	 *
	 * @lastModified zhaoqingwen 2017年12月22日 上午11:18:02  
	 */
	Page<DocColumnVO> findColumnList(int i, Integer pageSize, String name,Long loginUserId);

	/**
	 *根据ID查找栏目
	 * @param id
	 * @return
	 *
	 * @lastModified zhaoqingwen 2017年12月22日 上午11:18:27  
	 */
	 DocColumnVO findColumn(Long id);

	/**
	 *验证是否可以删除
	 * @param id
	 * @return
	 *
	 * @lastModified zhaoqingwen 2017年12月22日 上午11:19:11  
	 */
	 JsonResult validateDelte(Long id);

	 /**
	 *根据栏目查询来源
	 * @param docColumnIds
	 * @return 来源Id集合
	 *
	 * @lastModified zhaoqingwen 2017年12月28日 上午2:46:43  
	 */
	 List<Long>  getSources(Long docColumnIds);

	 /**
	 *根据栏目查询来源
	 * @param docColumnIds
	 * @return 用户集合
	 *
	 * @lastModified zhaoqingwen 2017年12月28日 上午2:48:26  
	 */
	List<Long>  getUsers(Long docColumnIds);
}
