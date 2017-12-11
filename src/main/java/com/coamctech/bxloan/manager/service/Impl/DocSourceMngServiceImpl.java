package com.coamctech.bxloan.manager.service.Impl;

import com.coamctech.bxloan.manager.common.DynamicQuery.DynamicQuery;
import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.*;
import com.coamctech.bxloan.manager.domain.*;
import com.coamctech.bxloan.manager.service.DocSourceMngService;
import com.coamctech.bxloan.manager.service.RoleMngService;
import com.coamctech.bxloan.manager.service.VO.DocSourceVO;
import com.coamctech.bxloan.manager.service.VO.RoleVO;
import com.coamctech.bxloan.manager.utils.CommonHelper;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class DocSourceMngServiceImpl implements DocSourceMngService{
	
	@Autowired
	private DynamicQuery dynamicQuery;
	

	@Autowired
	private RoleUserRelDao roleUserRelDao;
	
	@Autowired
	private RoleDocColumnRelDao roleDocColumnRelDao;
	
	@Autowired
	private RoleDocSourceRelDao roleDocSourceRelDao;
	
	@Autowired
	private UserDocColumnRelDao userDocColumnRelDao;
	
	@Autowired
	private UserDocSourceRelDao userDocSourceRelDao;
    @Autowired
    private DocSourceDao docSourceDao;
	
	@Override
	public Page<DocSourceVO> findBySearch(Integer pageNumber, Integer pageSize,
			String roleName) throws ParseException{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT r.id,r.name,r.type,r.if_special,r.language_type ");
		sql.append(" FROM t_doc_source r WHERE 1=1");
		int i = 0;
		if(StringUtils.isNotBlank(roleName)){
			sql.append(" AND r.name like ?").append(++i);
			params.add(StringUtils.join("%", roleName, "%"));
		}
		Page<Object[]> page = dynamicQuery.nativeQuery(Object[].class,
				new PageRequest(pageNumber, pageSize), sql.toString(),params.toArray());
		List<DocSourceVO> returnList = Lists.newArrayList(Lists.transform(
				page.getContent(), new Function<Object[], DocSourceVO>() {
					@Override
					public DocSourceVO apply(Object[] objs) {
                        DocSourceVO vo =  new DocSourceVO(objs);
						return vo;
					}
				}));
		Page<DocSourceVO> resultPage = new PageImpl<DocSourceVO>(returnList, new PageRequest(pageNumber, pageSize), page.getTotalElements());
		return resultPage;
	}

	@Override
	public JsonResult deleteById(Long docSourceId) {
        DocSource docSource = docSourceDao.findOne(docSourceId);
		docSourceDao.delete(docSource);
        userDocSourceRelDao.deleteRelByDocSourceId(docSourceId);
		roleDocSourceRelDao.deleteRelByDocSourceId(docSourceId);
		return JsonResult.success();
	}

	@Override
	public JsonResult saveDocSource(DocSourceVO docSourceVO,User curUser) throws Exception{
        DocSource docSource = null;
        if(docSourceVO.getId()==null){
            docSource = new DocSource();
            docSource.setCreateTime(new Date());
            docSource.setCreator(curUser.getId());
            docSource.setIfSpecial(docSourceVO.getIfSpecial());
        }else{
            docSource = docSourceDao.findOne(docSourceVO.getId());
        }
        docSource.setLanguageType(docSourceVO.getLanguageType());
        docSource.setName(docSourceVO.getName());
        docSource.setType(docSourceVO.getType());
        docSource.setUpdateTime(new Date());
        docSourceDao.save(docSource);
        return JsonResult.success();
	}

	@Override
	public DocSource getById(Long id) {
		return docSourceDao.findOne(id);
	}
}
