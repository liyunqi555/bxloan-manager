package com.coamctech.bxloan.manager.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.common.DynamicQuery.DynamicQuery;
import com.coamctech.bxloan.manager.dao.DocColumnDao;
import com.coamctech.bxloan.manager.dao.DocColumnDocSourceRelDao;
import com.coamctech.bxloan.manager.dao.DocInfoDao;
import com.coamctech.bxloan.manager.dao.RoleDocColumnRelDao;
import com.coamctech.bxloan.manager.dao.UserDocColumnRelDao;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.DocColumnDocSourceRel;
import com.coamctech.bxloan.manager.domain.UserDocColumnRel;
import com.coamctech.bxloan.manager.domain.UserDocSourceRel;
import com.coamctech.bxloan.manager.service.IColumnService;
import com.coamctech.bxloan.manager.service.VO.DocColumnVO;
import com.coamctech.bxloan.manager.utils.CommonHelper;
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
@Transactional
@Service
public class ColumnServiceImpl implements IColumnService{
	@Autowired
	private DocColumnDao docColumnDao;

	@Autowired
	private DynamicQuery dynamicQuery;
	
	@Autowired
	private DocInfoDao docInfoDao;
	
	@Autowired
	private RoleDocColumnRelDao  roleDocColumnRelDao;
	@Autowired
	private UserDocColumnRelDao userDocColumnRelDao;
	@Autowired
	private DocColumnDocSourceRelDao docColumnDocSourceRelDao;
	
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
			 roleDocColumnRelDao.deleteRelByDocColumnId(columnId);
			 userDocColumnRelDao.deleteRelByDocColumnId(columnId);
		}
	}

	@Override
	public void addColumn(DocColumn docColumn,String userIds, String sourceIds, Long loginId) {
		Long parentId = docColumn.getParentId();
		if(null!=parentId){
			DocColumn parentDocColumn = docColumnDao.findOne(parentId);
			docColumn.setLevel(parentDocColumn.getLevel()+1);
		}
		String conditionField = docColumn.getConditionField();
		Integer conditionType = docColumn.getConditionType();
		StringBuffer str= new StringBuffer();
		//换行符转换成List
		List<String> list = CommonHelper.strToList(conditionField,CommonHelper.NEW_LINE);
		if(null!=list &&list.size()>0){
			for(int i = 0; i<list.size();i++){
				String line = list.get(i);
				line = line.replaceAll(" ", "");	
				String [] strArray = line.split("=");
				if(strArray.length>1){
					String beforeLine= strArray[0];
					String afterLine= strArray[1].trim();
					beforeLine= beforeLine.replace("标题关键字", "title like '%");
					beforeLine= beforeLine.replace("来源关键字", "sourceName like '%");
					beforeLine= beforeLine.replace("文章关键字", "body like '%");
					str.append(beforeLine).append(afterLine).append("%'");
					if(null!=conditionType &&conditionType.equals(1)&&list.size()>1){
						str.append(" and ");
					}
				}else{
					//逻辑拼接
					str.append(" ").append(line).append(" ");
				}
			}
		}
		docColumn.setConditionField(str.toString());
		if(null==docColumn.getId()){
			docColumn.setCreateTime(new Date());
		}
		docColumn.setUpdateTime(new Date());
		docColumn.setCreator(loginId);
		docColumnDao.save(docColumn);
		List<String> userIdList = CommonHelper.strToList(userIds,CommonHelper.SEPARATOR_COMMA);
		List<String> sourceIdList = CommonHelper.strToList(sourceIds,CommonHelper.SEPARATOR_COMMA);
		Long docColumnId = docColumn.getId();
		if(CollectionUtils.isNotEmpty(userIdList)){
			for(String userId:userIdList){
				UserDocColumnRel rel = new  UserDocColumnRel();
				rel.setDocColumnId(docColumnId);
				rel.setUserId(CommonHelper.toLong(userId));
				rel.setCreateTime(CommonHelper.getNow());
				rel.setCreator(loginId);
				userDocColumnRelDao.save(rel);
			}
		}
		
		if(CollectionUtils.isNotEmpty(sourceIdList)){
			for(String sourceId:sourceIdList){
				DocColumnDocSourceRel dds = new  DocColumnDocSourceRel();
				dds.setDocColumnId(docColumnId);
				dds.setDocSourceId(CommonHelper.toLong(sourceId));
				dds.setUpdateTime(CommonHelper.getNow());
				dds.setCreateTime(CommonHelper.getNow());
				dds.setCreator(loginId);
				docColumnDocSourceRelDao.save(dds);
			}
		}
	}

	@Override
	public Page<DocColumnVO> findColumnList(int pageNumber, Integer pageSize, String name,Long loginUserId) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append("select tc.name,tu.user_name,tc.if_special,tc.level,ptu.name parentName,tc.id ,tc.parent_id ,tc.condition_field,tc.condition_type from t_doc_column tc ,t_user tu,t_doc_column ptu where tc.creator=tu.id and ptu.id=tc.parent_id");
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
		sql.append("select tc.name,tu.user_name,tc.if_special,tc.level,ptu.name parentName,tc.id ,tc.parent_id ,tc.condition_field,tc.condition_type from t_doc_column tc ,t_user tu,t_doc_column ptu where tc.creator=tu.id and ptu.id=tc.parent_id");
		if(null!=id){
			params.add(id);
		    sql.append(" and tc.id = ?").append(params.size()); 
		}
		sql.append("   order by tc.create_time desc ");
		List<DocColumnVO> docColumnVOList = Lists.transform(
				dynamicQuery.nativeQuery(Object[].class, sql.toString(), id),
				new Function<Object[], DocColumnVO>() {
					@Override
					public DocColumnVO apply(Object[] objs) {
						return new DocColumnVO(objs);
					}
			});
		if (CollectionUtils.isEmpty(docColumnVOList)) {
			return null;
		}
		//处理特殊字段
		DocColumnVO vo = docColumnVOList.get(0);
		String conditionField = vo.getConditionField();
		List<String> list = CommonHelper.strToList(conditionField,"%'");
		vo.setConditionField(conditionField);
		return vo;
	}

	@Override
	public JsonResult validateDelte(Long id) {
		List list = docColumnDao.findByParentId(id);
		if(null!=list&&list.size()>0){
			return new  JsonResult(ResultCode.ERROR_CODE,"该栏目下包含子栏目,不可以删除！");
		}
/*		List docInfoList = docInfoDao.findByColumnId(id) ;
		if(null!=docInfoList&&docInfoList.size()>0){
			return new  JsonResult(ResultCode.ERROR_CODE,"该栏目下包含文章,不可以删除！");
		}*/
		return null;
	}
   
}	
