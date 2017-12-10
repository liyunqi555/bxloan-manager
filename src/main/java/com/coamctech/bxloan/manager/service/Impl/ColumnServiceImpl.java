package com.coamctech.bxloan.manager.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.dao.DocColumnDao;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.service.IColumnService;

/**   
 * 类名称：ColumnServiceImpl<br/>
 * 类描述 ：栏目管理实现类<br/>
 * 创建人: zhaoqingwen<br/>
 * 创建时间：2017年12月7日 下午4:01:32<br/>  
 * 修改人：
 * 修改时间：  
 * 修改备注：
 * 版本： V1.0
 */
@Service
public class ColumnServiceImpl implements IColumnService{
	@Autowired
	private DocColumnDao docColumnDao;
	
	@Override
	public JsonResult getAllColumn() {
		return null;
	}
	
	@Override
	public List<DocColumn> getAllColumnList() {
		List<DocColumn> docColumnlist = (List<DocColumn>) docColumnDao.findAll();
		return docColumnlist;
	}

	@Override
	public JsonResult modifyColumn(DocColumn docColumn) {
		docColumnDao.save(docColumn);
		return null;
	}

	@Override
	public JsonResult deleteColumn(String columnId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonResult addColumn() {
		// TODO Auto-generated method stub
		return null;
	}
   
}	
