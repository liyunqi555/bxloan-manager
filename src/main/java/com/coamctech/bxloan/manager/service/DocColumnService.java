package com.coamctech.bxloan.manager.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

import javax.persistence.Query;
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
        if(customColumnIds.size()==0){
            customColumnIds.add(-1L);
        }
        List<DocColumn> docColumnList = docColumnDao.findByParentIdAndIdNotIn(parentDocCulumnId, customColumnIds);
        return docColumnList;
    }

    /**
     * 查询已订阅栏目
     * @param userId
     * @param parentDocCulumnId
     * @return
     */
    public JSONArray getCustomColumns(Long userId,Long parentDocCulumnId){
        //List<UserCustomDocColumn> customDocColumns = userCustomDocColumnService.getCustomColumns(userId, parentDocCulumnId);
        Query query = this.entityManagerFactory.createEntityManager().createNativeQuery(" select t2.id,t2.name,t2.parent_id,t1.custom_order " +
                " from t_user_custom_doc_column t1  " +
                " left join t_doc_column t2 on t1.doc_column_id=t2.id " +
                " where t1.user_id=?1 and t1.doc_column_parent_id=?2 order by t1.custom_order asc ");
        query.setParameter(1,userId);
        query.setParameter(2,parentDocCulumnId);
        List<Object[]> list = query.getResultList();
        JSONArray ja = new JSONArray();
        list.forEach(arr->{
            JSONObject jo = new JSONObject();
            jo.put("id",arr[0]);
            jo.put("name",arr[1]);
            jo.put("parentId",arr[2]);
            jo.put("customOrder",arr[3]);
            ja.add(jo);
        });
        //Iterable<DocColumn> list = docColumnDao.findAll(customColumnIds);
        return ja;
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
