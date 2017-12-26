package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.Page;
import com.coamctech.bxloan.manager.common.PageList;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.DocInfoDao;
import com.coamctech.bxloan.manager.dao.UserDocColumnRelDao;
import com.coamctech.bxloan.manager.domain.*;
import com.coamctech.bxloan.manager.utils.CommonHelper;
import com.coamctech.bxloan.manager.utils.StringUtils;
import com.coamctech.bxloan.manager.utils.TokenUtils;

import org.apache.commons.collections.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import java.sql.Array;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Administrator on 2017/11/12.
 */
@Service
@Transactional
public class DocInfoService extends BaseService<DocInfo,Long>{
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DocInfoDao docInfoDao;
    @Autowired
    private UserDocColumnRelDao userDocColumnRelDao;
    @Autowired
    private UserCustomDocColumnService userCustomDocColumnService;
    @Autowired
    private DocColumnService docColumnService;
    @Autowired
    private UserViewHistoryService userViewHistoryService;
    @Autowired
    private UserStoreService userStoreService;
    @Autowired
    private DocSourceService docSourceService;
    @Autowired
    private AppUserService appUserService;


    public List<DocInfo> getTopDocInfos(){

        List<DocInfo> docInfos = docInfoDao.findFirst6ByOrderByIfTopDescUpdateTimeDesc();
        parseImgUrl(docInfos);
        return docInfos;
    }

    /**
     *  分页查询某个栏目下的资讯
     * @param page 分页查询
     * @param userId  登录用户id
     * @param columnId 栏目id
     * @return
     */
    public List<DocInfo> docInfos(Page page,Long userId,Long columnId,Long topLevelColumnId){
        if(topLevelColumnId!=this.topLevelColumnIdReport){
            if(!userCustomDocColumnService.ifCustomColumnId(userId, columnId, topLevelColumnId)){
                return Collections.EMPTY_LIST;
            }
        }

        return this.getDocInfos(userId, page, Arrays.asList(columnId), null);
    }


    /**
     * 查询文档信息列表
     * @param page 分页查询
     * @param userId  登录用户id
     * @param parentColumnIds 上级栏目id
     * @param keyWorld 搜索关键字
     * @return
     */
    public List<DocInfo> searchDocInfos(Page page,Long userId,List<Long> parentColumnIds,String keyWorld){
        logger.info("userId={},parentColumnIds={}", userId, parentColumnIds.get(0));

        List<Long> childColumnIds = docColumnService.getCanVisitColumnIds(userId,parentColumnIds);
        if(childColumnIds.size()==0){
            return Collections.EMPTY_LIST;
        }
        return this.getDocInfos(userId, page, childColumnIds, keyWorld);
    }

    /**
     * 分页查询多个栏目下的资讯
     * @param page
     * @param columnIds  多个栏目的id
     * @param keyworld 搜索词，如果为空，则查询所有
     * @return
     */
    public  List<DocInfo> getDocInfos(Long userId,Page page,List<Long> columnIds,String keyworld){
        List<DocInfo> docInfos = new ArrayList<>();
        if(columnIds.size()==0){
            logger.info("参数栏目为空");
            return docInfos;
        }
        EntityManager entityManager = null;
        try{
            List<Long> canVisitDocSourceIds = docSourceService.getCanVisitDocSourceIds(userId);
            if(canVisitDocSourceIds.size()==0 ){
                logger.info("该用户userId={}没有配置来源",userId);
                return docInfos;
            }

            Map<String, Object> param = new HashMap<>();
            entityManager = this.entityManagerFactory.createEntityManager();
            StringBuilder sql = new StringBuilder();
            sql.append(" select tt.id ,tt.title , tt.cn_title , tt.summary , tt.update_time , tt.body , tt.cn_boty ,tt.name   from ( ")
                    .append( " select row_number()over(order by tempcolumn)temprownumber,*  from ")
                    .append(" ( ")
                    .append( " select top "+(page.getPageIndex()*page.getPageSize()+page.getPageSize())+" tempcolumn=0,")
                    .append("t.id ,t.title , t.cn_title , t.summary , t.update_time , t.body , t.cn_boty ,ds.name ")
                    .append("  from t_doc_info t ,t_doc_source ds  where t.source_id=ds.id ");
            Iterable<DocColumn> docColumns = docColumnService.findAll(columnIds);
            sql.append(" and ( 1=1 ");
            for(DocColumn docColumn:docColumns){
                sql.append(" or ").append(this.getConditionSql(docColumn));
            }
            sql.append(" ) ");
            sql.append(" and  t.source_Id in(:sourceId) ");
            param.put("sourceId",canVisitDocSourceIds);

            if(StringUtils.isNotEmpty(keyworld)){
                sql.append(" and t.title like :title ");
                param.put("title", "%" + keyworld + "%");
            }
            sql.append(" order by t.update_Time desc ");
            sql.append(" ) t1 ").append(" )tt  where temprownumber > ").append(page.getPageIndex()*page.getPageIndex()) ;
            logger.info("sql={}",sql);
            Query query = entityManager.createNativeQuery(sql.toString());
            param.entrySet().forEach(p -> {
                query.setParameter(p.getKey(), p.getValue());
            });
            List<Object[]> list = query.getResultList();

            list.forEach(arr->{
                DocInfo docInfo = new DocInfo();
                //t.id ,t.title , t.cn_title , t.summary , t.update_time , t.body , t.cn_body ,ds.name
                docInfo.setId(CommonHelper.toLong(arr[0]));
                docInfo.setTitle(CommonHelper.toStr(arr[1]));
                docInfo.setCnTitle(CommonHelper.toStr(arr[2]));
                docInfo.setSummary(CommonHelper.toStr(arr[3]));
                docInfo.setUpdateTime(CommonHelper.toDate(arr[4]));
                docInfo.setBody(CommonHelper.toStr(arr[5]));
                docInfo.setCnBoty(CommonHelper.toStr(arr[6]));
                docInfo.setSourceName(CommonHelper.toStr(arr[7]));
                docInfos.add(docInfo);
            });
            logger.info("list.size()=",list.size());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(entityManager!=null){
                entityManager.close();
            }
        }

        parseImgUrl(docInfos);
//        addDocSourceName(pageList.getList());
        addStoreFlag(docInfos,userId);
        return docInfos;
    }
    private String getConditionSql(DocColumn docColumn){
        StringBuilder conditionSql = new StringBuilder();
        String conditionField = docColumn.getCondtionField();
        Integer conditionType = docColumn.getConditionType();

        conditionSql.append(" ( 1=1 ");
        if(conditionType!=null){
            if(conditionType.equals(1)){//普通关联
                String[] arr = conditionField.split(StringUtils.BLANK_STRING);
                for(String v:arr){
                    conditionSql.append(" and (")
                            .append(" t.title like '%").append(v).append("%' ")
                            .append(" or t.body like '%").append(v).append("%' ")
                            .append(" ) ");
                }
            }else if(conditionType.equals(2)) {//高级sql模式
                conditionSql.append(" and ").append(this.replaceConditionField(conditionField,"t.title","ds.name","t.body"));
            }else{
                logger.warn("栏目配置内容有误 columnId={}",docColumn.getId());
            }
        }
        conditionSql.append(" ) ");
        logger.info("docColumnId={},docColumnName={},conditionType={},conditionField={},conditionSql={}",
                docColumn.getId(),docColumn.getName(),conditionType,conditionField,conditionSql);
        return conditionSql.toString();
    }
    private String replaceConditionField(String conditionField,String title,String sourceName,String body){
        conditionField = conditionField.replace(this.TITLE,StringUtils.BLANK_STRING+title+StringUtils.BLANK_STRING);
        conditionField = conditionField.replace(this.SOURCE_NAME,StringUtils.BLANK_STRING+sourceName+StringUtils.BLANK_STRING);
        conditionField = conditionField.replace(this.BODY,StringUtils.BLANK_STRING+body+StringUtils.BLANK_STRING);
        return conditionField;
    }
    private void parseImgUrl(Iterable<DocInfo> docInfos){
        if(docInfos==null){
            return;
        }
        docInfos.forEach(docInfo ->{
            parseImgUrl(docInfo);
        });
    }
    private void addDocSourceName(List<DocInfo> docInfos){
        if(docInfos==null || docInfos.size()==0){
            return;
        }
        List<Long> sourceIds = new ArrayList<>();
        docInfos.forEach(docInfo -> {
            sourceIds.add(docInfo.getSourceId());
        });
        Iterable<DocSource> docSources = docSourceService.findAll(sourceIds);
        docInfos.forEach(docInfo->{
            if(docInfo.getSourceId()!=null){
                docSources.forEach(docSource->{
                    if(docInfo.getSourceId().equals(docSource.getId())){
                        docInfo.setSourceName(docSource.getName());
                    }
                });
            }
        });
    }
    private void addStoreFlag(List<DocInfo> docInfos,Long userId){
        if(docInfos==null || docInfos.size()==0){
            return;
        }
        List<Long> docInfoIds = new ArrayList<>();
        docInfos.forEach(docInfo -> {
            docInfoIds.add(docInfo.getId());
        });
        List<UserStore> userStores = userStoreService.findByUserIdAndDocInfoIds(userId, docInfoIds);
        docInfos.forEach(docInfo -> {
            Long id = docInfo.getId();
            boolean flag = false;
            for(UserStore userStore : userStores){
                if (id.equals(userStore.getDocInfoId())) {
                    flag = true;
                    docInfo.setStoreId(userStore.getId());
                    break;
                }
            }
            docInfo.setStoreFlag(flag?1:0);
        });
    }
    private void parseImgUrl(DocInfo docInfo){
        String body = docInfo.getBody();
        Elements elements = Jsoup.parse(body, "UTF-8").select("img[src]");
        String imgUrl = elements.attr("src");
        docInfo.setImgUrl(imgUrl);
    }

    /**
     * 查询文档详情，并记录浏览记录
     * @param docInfoId
     * @param userId
     * @return
     */
    public JsonResult articleDetail(Long docInfoId,Long userId){
        DocInfo docInfo = docInfoDao.findOne(docInfoId);
        User user = appUserService.findOne(userId);
        if(user.getIfStoreViewHitory()==null){
            user.setIfStoreViewHitory(1);
            appUserService.save(user);
        }
        if(user.getIfStoreViewHitory()==1){
            userViewHistoryService.save(docInfo,userId);
        }
        DocSource docSource = docSourceService.findOne(docInfo.getSourceId());
        if(docSource!=null){
            docInfo.setSourceName(docSource.getName());
        }
        return new JsonResult(ResultCode.SUCCESS_CODE,ResultCode.SUCCESS_MSG, docInfo);
    }

    /**
     * 我的历史
     * @param page
     * @param userId
     * @return
     */
    public List<DocInfo> myHistory(Page page,Long userId){

        List<UserViewHistory> userViewHistories = userViewHistoryService.pageHistory(page, userId);
        List<Long> docInfoIds = new ArrayList<>();
        userViewHistories.forEach(userViewHistory->{
            docInfoIds.add(userViewHistory.getDocInfoId());
        });
        Iterable<DocInfo> docInfos = docInfoDao.findAll(docInfoIds);
        List<DocInfo> docInfosList = new ArrayList<>();
        parseImgUrl(docInfos);
        docInfos.forEach(docInfo->{
            for(UserViewHistory userViewHistory:userViewHistories) {
                if (userViewHistory.getDocInfoId().equals(docInfo.getId())) {
                    docInfo.setViewTime(userViewHistory.getUpdateTime());
                    break;
                }
            }
            docInfosList.add(docInfo);
        });
        //按查看顺序排序

        Collections.sort(docInfosList, new DocInfoViewTimeComparator());
        return docInfosList;
    }
    /**
     * 我的收藏
     * @param page
     * @param userId
     * @return
     */
    public List<DocInfo> myStore(Page page,Long userId,Long topLevelColumnId){

        List<UserStore> userStores = userStoreService.pageUserStore(page, userId, topLevelColumnId);
        List<Long> docInfoIds = new ArrayList<>();
        userStores.forEach(userStore->{
            docInfoIds.add(userStore.getDocInfoId());
        });
        Iterable<DocInfo> docInfos = docInfoDao.findAll(docInfoIds);

        parseImgUrl(docInfos);

        List<DocInfo> docInfosList = new ArrayList<>();
        docInfos.forEach(docInfo->{
            for(UserStore userStore:userStores) {
                if (userStore.getDocInfoId().equals(docInfo.getId())) {
                    docInfo.setViewTime(userStore.getCreateTime());
                    docInfo.setStoreId(userStore.getId());
                    break;
                }
            }
            docInfosList.add(docInfo);
        });
        //按收藏顺序排序
        Collections.sort(docInfosList, new DocInfoStoreTimeComparator());
        return docInfosList;
    }
}
