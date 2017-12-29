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
	public JsonResult addColumn(DocColumn docColumn,String userIds, String sourceIds, Long loginId) {
		Long parentId = docColumn.getParentId();
		if(null!=parentId){
			DocColumn parentDocColumn = docColumnDao.findOne(parentId);
			docColumn.setLevel(parentDocColumn.getLevel()+1);
		}
		String conditionField = docColumn.getConditionField();
		Integer conditionType = docColumn.getConditionType();
		if(null!=conditionType &&conditionType.equals(2)){
			conditionField =conditionField.replace("标题关键字","title").replace("来源关键字","sourceName").replace("文章关键字","body");
		}
		//换行符转换成List
		/*List<String> list = CommonHelper.strToList(conditionField,CommonHelper.NEW_LINE);
		StringBuffer str= new StringBuffer();
		if(null!=list &&list.size()>0){
			for(int i = 0; i<list.size();i++){
				String line = list.get(i);
				line = line.replaceAll(" ", "");	
				if(null!=conditionType &&conditionType.equals(1)){
					str.append("title like '%").append(line).append("%' or  sourceName like '%").append(line).append("%' or  body like '%").append(line).append("%'");
					continue;
				}
				String [] strArray = line.split("=");
				if(strArray.length>1){
					String beforeLine= strArray[0];
					String afterLine= strArray[1].trim();
					beforeLine= beforeLine.replace("标题关键字", "title like '%");
					beforeLine= beforeLine.replace("来源关键字", "sourceName like '%");
					beforeLine= beforeLine.replace("文章关键字", "body like '%");
					str.append(beforeLine).append(afterLine).append("%'");
				}else{
					//逻辑拼接
					str.append(" ").append(line).append(" ");
				}
			}
		}else{
			//没有换行符默认是普通模式
			conditionField = conditionField.replaceAll(" ", "");
			str.append("title like '%").append(conditionField).append("%' or  sourceName like '%").append(conditionField).append("%' or  body like '%").append(conditionField).append("%'");
		}*/ 
		if(null==docColumn.getId()){
			docColumn.setCreateTime(new Date());
		}else{
			DocColumn old = docColumnDao.findOne(docColumn.getId());
			docColumn.setCreateTime(old.getCreateTime());
		}
		docColumn.setConditionField(conditionField);
		docColumn.setUpdateTime(new Date());
		docColumn.setCreator(loginId);
		try{
			JsonResult result =this.valdationField(docColumn);
			if(200!=result.getCode()){
				return new  JsonResult(ResultCode.ERROR_CODE,result.getMsg());
			}
		}catch(Exception e){
			e.getStackTrace();
			return new  JsonResult(ResultCode.ERROR_CODE,"条件表达语法是错误!");
		}
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
		return new  JsonResult(ResultCode.SUCCESS_CODE,"保存成功");
	}

	private JsonResult valdationField(DocColumn docColumn) {
		String conditionField = docColumn.getConditionField();
		if(StringUtils.isBlank(conditionField)){
			return new  JsonResult(ResultCode.ERROR_CODE,"条件表达式必填!");
		}
		StringBuffer sql = new StringBuffer(); 
		if(StringUtils.isNotBlank(conditionField)){
			conditionField =conditionField.trim();
		}
		Integer conditionType = docColumn.getConditionType();
		StringBuffer conditionStr = new StringBuffer();
		if(null!=conditionType &&conditionType.equals(2)){
			conditionField= conditionField.replace("sourceName", "name");
			conditionStr.append(conditionField);
		}else{
			String [] strArr = conditionField.split(" ");
			for(String str:strArr){
				if(StringUtils.isBlank(str)){
					continue;
				}
				conditionStr.append(" ti.title like '%").append(str).append("%' or ").append(" ti.body like '%").append(str).append("%' or ");
			}
			conditionStr = new StringBuffer (conditionStr.substring(0, conditionStr.toString().lastIndexOf("or")));
		}
		sql.append("select ti.title from t_doc_info ti, t_doc_source ts where ts.id= ti.source_id and ( ").append(conditionStr.toString()).append(" )");
		try{
			Long count = dynamicQuery.nativeQueryCount(sql.toString());
			if(0==count){
				return new  JsonResult(ResultCode.ERROR_CODE,"无法检索文章,请添加相关关键字文章!");
			}
		}catch(Exception e){
			return new  JsonResult(ResultCode.ERROR_CODE,"条件表达语法是错误!");
		}
		return  new  JsonResult(ResultCode.SUCCESS_CODE,"");
		
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
		Integer conditionType = vo.getConditionType();
		if(null!=conditionType &&conditionType.equals(2)){
			conditionField =conditionField.replace("title","标题关键字").replace("sourceName","来源关键字").replace("body","文章关键字");
		}
		vo.setConditionField(conditionField);
		return vo;
	}

	@Override
	public JsonResult validateDelte(Long id) {
		List list = docColumnDao.findByParentId(id);
		if(null!=list&&list.size()>0){
			return new  JsonResult(ResultCode.ERROR_CODE,"该栏目下包含子栏目,不可以删除！");
		}
		return new JsonResult(ResultCode.SUCCESS_CODE,"");
	}

	@Override
	public List<Long> getSources(Long docColumnIds) {
		return this.docColumnDocSourceRelDao.findSourceIdsByDocColumnId(docColumnIds);
	}

	@Override
	public List<Long> getUsers(Long docColumnIds) {
		return this.userDocColumnRelDao.findUserByDocColumnId(docColumnIds);
	}
   
}	
