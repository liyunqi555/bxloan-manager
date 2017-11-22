package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.Page;
import com.coamctech.bxloan.manager.common.PageList;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.DocColumnDao;
import com.coamctech.bxloan.manager.dao.DocInfoDao;
import com.coamctech.bxloan.manager.dao.UserCustomDocColumnDao;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.DocInfo;
import com.coamctech.bxloan.manager.domain.UserCustomDocColumn;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Administrator on 2017/11/12.
 */
@Service
@Transactional
public class DocColumnService extends BaseService<DocColumn,Long>{
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DocColumnDao docColumnDao;
    @Autowired
    private UserCustomDocColumnDao userCustomDocColumnDao;

    public JsonResult columns(Long id){
        List<DocColumn> list = this.docColumnDao.findByParentId(id);
        return new JsonResult(ResultCode.SUCCESS_CODE,ResultCode.SUCCESS_MSG, list);

    }
    public JsonResult customColumn(Long userId,Long customColumnId){
        UserCustomDocColumn userCustomDocColumn = new UserCustomDocColumn();
        userCustomDocColumn.setDocColumnId(customColumnId);
        userCustomDocColumn.setCreateTime(new Date());
        Long count = this.userCustomDocColumnDao.count();
        userCustomDocColumn.setCustomOrder(count+1);
        DocColumn docColumn = docColumnDao.findOne(customColumnId);
        userCustomDocColumn.setDocColumnParentId(docColumn.getParentId());
        userCustomDocColumn.setUserId(userId);
        userCustomDocColumnDao.save(userCustomDocColumn);
        return new JsonResult(ResultCode.SUCCESS_CODE,ResultCode.SUCCESS_MSG);
    }

    /**
     * 查询子栏目ID的集合
     * @param parentId
     * @return
     */
    public List<Long> getChildColumnIdsByParentId(Long parentId){
        List<DocColumn> list = docColumnDao.findByParentId(parentId);
        List<Long> childIds = new ArrayList<>();
        list.forEach(docColumn-> {childIds.add(docColumn.getId());});
        return childIds;
    }
}
