package com.coamctech.bxloan.manager.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;




import org.springframework.util.CollectionUtils;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.DynamicQuery.DynamicQuery;
import com.coamctech.bxloan.manager.dao.DocColumnDao;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.service.IColumnService;
import com.coamctech.bxloan.manager.service.VO.DocColumnVO;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
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

	@Autowired
	private DynamicQuery dynamicQuery;
	
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
	public JsonResult modifyColumn(DocColumn docColumn,Long id) {
		 DocColumn newDocColumn = docColumnDao.findOne(id);
		 newDocColumn.setUpdateTime( new Date());
		 newDocColumn.setName(docColumn.getName());
		 newDocColumn.setIfSpecial(docColumn.getIfSpecial());
		 Long parentId = docColumn.getParentId();
		 if(null!=parentId){
			 DocColumn parentDocColumn = docColumnDao.findOne(parentId);
			 newDocColumn.setLevel(parentDocColumn.getLevel()+1);
		 }
		 newDocColumn.setParentId(parentId);
		docColumnDao.save(docColumn);
		return null;
	}

	@Override
	public void deleteColumn(Long columnId) {
		if(null!=columnId){
			 docColumnDao.delete(Long.valueOf(columnId));
		}
	}

	@Override
	public void addColumn(DocColumn docColumn,Long userId) {
		 Long parentId = docColumn.getParentId();
		 if(null!=parentId){
			 DocColumn parentDocColumn = docColumnDao.findOne(parentId);
			 docColumn.setLevel(parentDocColumn.getLevel()+1);
		 }
		 docColumn.setCreateTime(new Date());
		 docColumn.setUpdateTime(new Date());
		 docColumn.setCreator(userId);
		 docColumnDao.save(docColumn);
	}

	@Override
	public Page<DocColumnVO> findColumnList(int pageNumber, Integer pageSize, String name,Long loginUserId) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append("select tc.name,tu.user_name,tc.if_special,tc.level,ptu.name parentName,tc.id ,tc.parent_id  from t_doc_column tc ,t_user tu,t_doc_column ptu where tc.creator=tu.id and ptu.id=tc.parent_id");
		if(StringUtils.isNotBlank(name)){
			params.add("%"+String.valueOf(name)+"%");
		    sql.append(" and tc.name like ?").append(params.size()); 
		}
		if(null!=loginUserId){
		 }
		sql.append("   order by tc.create_time desc ");
		Page<Object[]> page = dynamicQuery.nativeQuery(Object[].class,	new PageRequest(pageNumber, pageSize), sql.toString(), params.toArray());
        List<DocColumnVO> docColumnVOList = Lists.newArrayList(Lists.transform(page.getContent(),
    			new Function<Object[], DocColumnVO>() {
    				@Override
    				public DocColumnVO apply(Object[] objs) {
    					return new DocColumnVO(objs);
    				}
    			}));
        for (int i= 0 ;i<docColumnVOList.size();i++){
        	DocColumnVO vo = docColumnVOList.get(i);
     	   //序列号 从开始
     	   vo.setOrder(i+1);
         }
 		Page<DocColumnVO> resultPage = new PageImpl<DocColumnVO>(docColumnVOList, new PageRequest(pageNumber, pageSize),page.getTotalElements());
		return resultPage;
	}

	@Override
	public DocColumnVO findColumn(Long id) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append("select tc.name,tu.user_name,tc.if_special,tc.level,ptu.name parentName,tc.id ,tc.parent_id from t_doc_column tc ,t_user tu,t_doc_column ptu where tc.creator=tu.id and ptu.id=tc.parent_id");
		if(null!=id){
			params.add(id);
		    sql.append(" and tc.id = ?").append(params.size()); 
		}
		sql.append("   order by tc.create_time desc ");
		List<DocColumnVO> v = Lists.transform(
				dynamicQuery.nativeQuery(Object[].class, sql.toString(), id),
				new Function<Object[], DocColumnVO>() {
					@Override
					public DocColumnVO apply(Object[] objs) {
						return new DocColumnVO(objs);
					}
			});
		if (CollectionUtils.isEmpty(v)) {
			return null;
		}
		return v.get(0);
	}
   
}	
