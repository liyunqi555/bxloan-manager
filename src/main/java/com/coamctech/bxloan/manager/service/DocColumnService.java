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
    @Autowired
    private RoleUserRelService roleUserRelService;
    @Autowired
    private RoleDocColumnService roleDocColumnService;

    /**
     * 查询未订阅栏目
     * @param userId
     * @param parentDocCulumnId
     * @return
     */
    public List<DocColumn> getNoCustomColumns(Long userId,List<Long> parentDocCulumnIds){
        List<Long> customColumnIds = getCanVisitColumnIds(userId,parentDocCulumnIds);
        if(customColumnIds.size()==0){
            customColumnIds.add(-1L);
        }
        List<DocColumn> docColumnList = docColumnDao.findByParentIdInAndIdNotIn(parentDocCulumnIds, customColumnIds);
        return docColumnList;
    }

    /**
     * 查询已订阅栏目
     * @param userId
     * @param parentDocCulumnId
     * @return
     */
    public JSONArray getCustomColumns(Long userId,List<Long> parentDocCulumnIds){
        //List<UserCustomDocColumn> customDocColumns = userCustomDocColumnService.getCustomColumns(userId, parentDocCulumnIds);
        List<Long> docColumnIds = this.getCanVisitColumnIds(userId,parentDocCulumnIds);
        if(docColumnIds.isEmpty()){
            return new JSONArray();
        }
        String sql = " SELECT t1.id,t1.name,t1.parent_id,t2.custom_order from t_doc_column t1 " +
                " left join t_user_doc_column_order t2 on t1.id=t2.doc_column_id " +
                " where t1.id in (?1) order by t2.custom_order asc ";
        Query query = this.entityManagerFactory.createEntityManager().createNativeQuery(sql);

        query.setParameter(1,docColumnIds);
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
    public boolean ifCanVisitColumnId(Long userId,Long columnId,Long topLevelColumnId){
        if(userCustomDocColumnService.ifCustomColumnId(userId,columnId,topLevelColumnId)){
            return true;
        }
        List<Long> roleIds = roleUserRelService.getRoleIds(userId);
        if(roleIds.size()==0){
            return false;
        }
        List<Long> columnIds = roleDocColumnService.getColumnIds(roleIds,Arrays.asList(columnId));
        if(columnIds.size()==0){
            return false;
        }
        return true;
    }
    public List<Long> getCanVisitColumnIds(Long userId,List<Long> parentColumnIds){
        List<Long> ids = new ArrayList<>();
        parentColumnIds.forEach(parentColumnId->{
            List<Long> columnIds = userCustomDocColumnService.getCustomColumnIds(userId, parentColumnId);
            if(columnIds.size()>0){
                ids.addAll(columnIds);
            }
            List<Long> roleIds = roleUserRelService.getRoleIds(userId);
            if(roleIds.size()>0){
                List<Long> childColumnIds = getChildColumnIdsByParentId(parentColumnId);
                if(childColumnIds.size()>0){
                    List<Long> roleColumnIds = roleDocColumnService.getColumnIds(roleIds,childColumnIds);

                    if(roleColumnIds.size()>0){
                        ids.addAll(roleColumnIds);
                    }
                }
            }
        });
        return ids;
    }
}
