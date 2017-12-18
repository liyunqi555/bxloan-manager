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
import com.coamctech.bxloan.manager.dao.UserDocColumnRelDao;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.DocInfo;
import com.coamctech.bxloan.manager.domain.UserCustomDocColumn;
import com.coamctech.bxloan.manager.domain.UserDocColumnRel;
import com.coamctech.bxloan.manager.utils.CommonHelper;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
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
    @Autowired
    private UserDocColumnRelDao userDocColumnRelDao;

    /**
     * 查询未订阅栏目
     * @param userId
     * @param parentDocCulumnIds
     * @return
     */
    public List<DocColumn> getNoCustomColumns(Long userId,List<Long> parentDocCulumnIds){
        List<Long> customColumnIds = userCustomDocColumnService.getCustomColumnIds(userId, parentDocCulumnIds);
        List<Long> canVisitColumnIds = getCanVisitColumnIds(userId,parentDocCulumnIds);
        canVisitColumnIds.removeAll(customColumnIds);
        if(canVisitColumnIds.size()==0){
            return Collections.EMPTY_LIST;
        }
        List<DocColumn> docColumnList = docColumnDao.findByParentIdInAndIdIn(parentDocCulumnIds, canVisitColumnIds);
        return docColumnList;
    }
    public List<Long> getCanVisitColumnIds(Long userId,List<Long> parentDocColumnIds){
        List<Long> reportChildIds = new ArrayList<>();
        parentDocColumnIds.forEach(pid->{
            if(pid.equals(this.topLevelColumnIdReport)){
                reportChildIds.addAll(getChildColumnIdsByParentId(this.topLevelColumnIdReport));
            }
        });
        List<Long> childColumnIds = getChildColumnIdsByParentId(parentDocColumnIds);
        List<UserDocColumnRel> userDocColumnRels = userDocColumnRelDao.findByUserIdAndDocColumnIdIn(userId, childColumnIds);
        List<Long> ids = new ArrayList<>();
        userDocColumnRels.forEach(u->{
            ids.add(u.getDocColumnId());
        });
        ids.addAll(reportChildIds);
        return ids;
    }
    /**
     * 查询已订阅栏目
     * @param userId
     * @param parentDocCulumnIds
     * @return
     */
    public JSONArray getCustomColumns(Long userId,List<Long> parentDocCulumnIds){
        //List<UserCustomDocColumn> customDocColumns = userCustomDocColumnService.getCustomColumns(userId, parentDocCulumnIds);
        EntityManager entityManager = null;
        try{
            entityManager = this.entityManagerFactory.createEntityManager();
            Query query = entityManager.createNativeQuery(" select t2.id,t2.name,t2.parent_id,t1.custom_order,t1.id " +
                    " from t_user_custom_doc_column t1  " +
                    " left join t_doc_column t2 on t1.doc_column_id=t2.id " +
                    " where t1.user_id=?1 and t1.doc_column_parent_id in ?2 order by t1.custom_order asc ");
            query.setParameter(1,userId);
            query.setParameter(2,parentDocCulumnIds);
            List<Object[]> list = query.getResultList();
            JSONArray ja = new JSONArray();
            list.forEach(arr->{
                JSONObject jo = new JSONObject();
                jo.put("id",arr[0]);
                jo.put("name",arr[1]);
                jo.put("parentId",arr[2]);
                jo.put("customOrder",arr[3]);
                if(arr[0]!=null){//如果栏目已经被删除，则删除已定制的栏目
                    ja.add(jo);
                }else{
                    userCustomDocColumnService.delete(CommonHelper.toLong(arr[4]));
                }
            });
            return ja;
        }finally {
            if(entityManager!=null){
                entityManager.close();
            }
        }
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
    /**
     * 查询子栏目ID的集合
     * @param parentDocCulumnIds
     * @return
     */
    public List<Long> getChildColumnIdsByParentId(List<Long> parentDocCulumnIds){
        List<DocColumn> list = docColumnDao.findByParentIdIn(parentDocCulumnIds);
        List<Long> childIds = new ArrayList<>();
        list.forEach(docColumn-> {childIds.add(docColumn.getId());});
        return childIds;
    }
}
