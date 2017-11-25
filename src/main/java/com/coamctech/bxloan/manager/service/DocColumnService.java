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
    private UserCustomDocColumnService userCustomDocColumnService;

    /**
     * 查询未订阅栏目
     * @param userId
     * @param parentDocCulumnId
     * @return
     */
    public List<DocColumn> getNoCustomColumns(Long userId,Long parentDocCulumnId){
        List<Long> customColumnIds = userCustomDocColumnService.getCustomColumnIds(userId, parentDocCulumnId);
        List<DocColumn> docColumnList = docColumnDao.findByParentIdAndIdNotIn(parentDocCulumnId, customColumnIds);
        return docColumnList;
    }

    /**
     * 查询已订阅栏目
     * @param userId
     * @param parentDocCulumnId
     * @return
     */
    public Iterable<DocColumn> getCustomColumns(Long userId,Long parentDocCulumnId){
        List<Long> customColumnIds = userCustomDocColumnService.getCustomColumnIds(userId, parentDocCulumnId);
        Iterable<DocColumn> list = docColumnDao.findAll(customColumnIds);
        return list;
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
