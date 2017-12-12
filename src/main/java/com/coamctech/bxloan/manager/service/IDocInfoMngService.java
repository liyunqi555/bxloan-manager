package com.coamctech.bxloan.manager.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.domain.DocInfo;
import com.coamctech.bxloan.manager.service.VO.DocColumnVO;
import com.coamctech.bxloan.manager.service.VO.DocInfoConditionVO;
import com.coamctech.bxloan.manager.service.VO.DocInfoVO;

/**   
 * 类名称：IDocInfoMngService<br/>
 * 类描述 ：<br/>
 * 创建人: zhaoqingwen<br/>
 * 创建时间：2017年12月11日 下午4:54:34<br/>  
 * 修改人：
 * 修改时间：  
 * 修改备注：
 * 版本： V1.0
 */

public interface IDocInfoMngService {


	/**
	 *查看文章
	 * @param docInfoId
	 * @param userId 登陆人ID
	 * @return
	 *
	 * @lastModified zhaoqingwen 2017年12月11日 下午5:05:18  
	 */
	DocInfoVO getDocInfoOne(Long docInfoId, Long userId);

	/**
	 *删除文档
	 * @param id
	 *
	 * @lastModified zhaoqingwen 2017年12月11日 下午5:05:38  
	 */
	void deleteDocInfo(Long id);
	/**
	 *批量删除文档
	 * @param id
	 *
	 * @lastModified zhaoqingwen 2017年12月11日 下午5:05:38  
	 */
	void deleteDocInfo(List<DocInfo> diList,Long id);
	/**
	 *增加文章
	 * @param docInfo
	 * @param id
	 *
	 * @lastModified zhaoqingwen 2017年12月11日 下午5:07:16  
	 */
	void addDocInfo(DocInfo docInfo, Long id);

	/**
	 *获取文章列表
	 * @param i
	 * @param pageSize
	 * @param docColumName
	 * @param DocInfoConditionVO 条件
	 * @return
	 *
	 * @lastModified zhaoqingwen 2017年12月11日 下午5:07:48  
	 */
	Page<DocInfoVO> findDocInfoList(int pageNumber, Integer pageSize,
			DocInfoConditionVO docInfoConditionVO);

}
