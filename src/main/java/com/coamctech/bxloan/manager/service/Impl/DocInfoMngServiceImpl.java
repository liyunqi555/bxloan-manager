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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.coamctech.bxloan.manager.common.DynamicQuery.DynamicQuery;
import com.coamctech.bxloan.manager.dao.DocInfoDao;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.DocInfo;
import com.coamctech.bxloan.manager.service.IDocInfoMngService;
import com.coamctech.bxloan.manager.service.VO.DocInfoConditionVO;
import com.coamctech.bxloan.manager.service.VO.DocInfoFormVO;
import com.coamctech.bxloan.manager.service.VO.DocInfoVO;
import com.coamctech.bxloan.manager.utils.CommonHelper;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**   
 * 类名称：DocInfoMngServiceImpl<br/>
 * 类描述 ：<br/>
 * 创建人: zhaoqingwen<br/>
 * 创建时间：2017年12月11日 下午4:55:14<br/>  
 * 修改人：
 * 修改时间：  
 * 修改备注：
 * 版本： V1.0
 */
@Transactional
@Service
public class DocInfoMngServiceImpl implements IDocInfoMngService {
	@Autowired
	private DocInfoDao docInfoDao;

	@Autowired
	private DynamicQuery dynamicQuery;
	
	@Override
	public DocInfoVO getDocInfoOne(Long id, Long userId) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append("select ti.id, ti.title,ti.cn_title,ti.source_id,ti.classification,ti.group_name,ti.website,ti.keyword,ti.summary,ti.body,ti.cn_boty,ts.name sourceName,ti.if_top  from  t_doc_info ti,t_doc_source ts  where  ");
		//sql.append(" ti.column_id=tc.id and");
		sql.append("  ti.source_id = ts.id");
		if(null!=id){
			params.add(id);
			sql.append(" and ti.id = ?").append(params.size()); 
		}
		List<DocInfoVO> docInfoVOs = Lists.transform(
				dynamicQuery.nativeQuery(Object[].class, sql.toString(), id),
				new Function<Object[], DocInfoVO>() {
					@Override
					public DocInfoVO apply(Object[] objs) {
						return new DocInfoVO(objs);
					}
			});
		if (CollectionUtils.isEmpty(docInfoVOs)) {
			return null;
		}
		DocInfoVO vo= docInfoVOs.get(0);
		DocInfoFormVO reslutVO = new  DocInfoFormVO(); 
		DocInfo docInfo = docInfoDao.findOne(id);
		reslutVO.setDocInfo(docInfo);
		reslutVO.setSourceName(vo.getSourceName());
		reslutVO.setColumnName(vo.getColumnName());
		return vo;
	}

	@Override
	public void deleteDocInfo(Long id) {	
		docInfoDao.delete(id);
	}

	@Override
	public void addDocInfo(DocInfo docInfo, Long id) {
		if(null==docInfo.getId()){
			docInfo.setCreateTime(new Date());
		}else{
			DocInfo old = docInfoDao.findOne(docInfo.getId());
			docInfo.setCreateTime(old.getCreateTime());
		}
		docInfo.setUpdateTime(new Date());
		docInfo.setCreator(id);
		//正文处理
		docInfo.setBody(handleText(docInfo.getBody()));
		docInfo.setCnBoty(handleText(docInfo.getCnBoty()));
		docInfoDao.save(docInfo);
	}

	@Override
	public Page<DocInfoVO> findDocInfoList(int pageNumber, Integer pageSize, DocInfoConditionVO docInfoConditionVO) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append("select ti.id, ti.title,ti.cn_title,ti.source_id,ti.classification,ti.group_name,ti.website,ti.keyword,ti.summary,ti.body,ti.cn_boty,ts.name sourceName ,ti.if_top from  t_doc_info ti,t_doc_source ts  where  1=1 ");
		//sql.append(" ti.column_id=tc.id");
		sql.append(" and ti.source_id = ts.id");
		if(null!=docInfoConditionVO){
			/*if(null!=docInfoConditionVO.getColumnId()){
				params.add(docInfoConditionVO.getColumnId());
			    sql.append(" and tc.id = ?").append(params.size()); 
			}*/
			if(null!=docInfoConditionVO.getSourceId()){
				params.add(docInfoConditionVO.getSourceId());
			    sql.append(" and ts.id = ?").append(params.size()); 
			}
			/*if(StringUtils.isNotBlank(docInfoConditionVO.getColumnName())){
				params.add("%"+String.valueOf(docInfoConditionVO.getColumnName())+"%");
			    sql.append(" and tc.name like ?").append(params.size()); 
			}*/
			if(StringUtils.isNotBlank(docInfoConditionVO.getSourceName())){
				params.add("%"+String.valueOf(docInfoConditionVO.getSourceName())+"%");
			    sql.append(" and ts.name like ?").append(params.size()); 
			}
			if(StringUtils.isNotBlank(docInfoConditionVO.getKeyword())){
				//and (ti.body  like '%%'or ti.cn_boty like '%%' or ti.cn_title like '%%'  or ti.title like '%%' or ti.summary like '')
				params.add("%"+String.valueOf(docInfoConditionVO.getKeyword())+"%");
			    sql.append(" and ti.keyword like ?").append(params.size()); 
			}
			if(StringUtils.isNotBlank(docInfoConditionVO.getConditionField())){
				if(docInfoConditionVO.getConditionField().contains("SourceName")||docInfoConditionVO.getConditionField().contains("title")
						&&docInfoConditionVO.getConditionField().contains("body")){
					sql.append(" and ").append(docInfoConditionVO.getConditionField()); 
				}else{
					StringBuffer conditionStr = new StringBuffer();
					String [] strArr = docInfoConditionVO.getConditionField().split(" ");
					for(String str:strArr){
						if(StringUtils.isBlank(str)){
							continue;
						}
						conditionStr.append(" ti.title like '%").append(str).append("%' or ").append(" ti.body like '%").append(str).append("%' or ");
					}
					conditionStr = new StringBuffer (conditionStr.substring(0, conditionStr.toString().lastIndexOf("or")));
					sql.append(" and ").append(conditionStr.toString());
				}
				
			}
		}
		Page<Object[]> page = dynamicQuery.nativeQuery(Object[].class,	new PageRequest(pageNumber, pageSize), sql.toString(), params.toArray());
        List<DocInfoVO> docInfoVOList = Lists.newArrayList(Lists.transform(page.getContent(),
    			new Function<Object[], DocInfoVO>() {
    				@Override
    				public DocInfoVO apply(Object[] objs) {
    					return new DocInfoVO(objs);
    				}
    			}));
 		Page<DocInfoVO> resultPage = new PageImpl<DocInfoVO>(docInfoVOList, new PageRequest(pageNumber, pageSize),page.getTotalElements());
		return resultPage;
	}
	//批量删除入口
	@Override
	public void deleteDocInfo(List<DocInfo> diList, Long id) {
		// TODO Auto-generated method stub
		
	}
	//处理文章
	private String  handleText(String str){
		StringBuffer newText = new StringBuffer();
		//文章是否含有P标签
		List<String> list = CommonHelper.strToList(str,CommonHelper.NEW_LINE);
		if(null!=list &&list.size()>0){
			for(int i = 0; i<list.size();i++){
				String line = list.get(i);
				if(!line.startsWith("<p>")){
					line = "<p>"+line;
				}
				if(!line.endsWith("</p>")){
					line = line+"</p>";
				}
				newText.append(line).append("\n");
			}
		}
		return newText.toString();
	}

}
