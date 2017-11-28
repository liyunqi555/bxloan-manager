package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.Page;
import com.coamctech.bxloan.manager.common.PageList;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.DocInfoDao;
import com.coamctech.bxloan.manager.dao.UserDocColumnRelDao;
import com.coamctech.bxloan.manager.domain.*;
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


    public List<DocInfo> getTopDocInfos(Long parentColumnId,Integer topCount){

        List<Long> columnIds = docColumnService.getChildColumnIdsByParentId(parentColumnId);
        //List<DocInfo> docInfos = docInfoDao.findByColumnIdInOrderByIfTopDescUpdateTimeDesc(columnIds, topCount);
        List<DocInfo> docInfos = docInfoDao.findFirst6ByColumnIdInOrderByIfTopDescUpdateTimeDesc(columnIds);
        docInfos.forEach(docInfo -> {
            parseImgUrlOfDocInfo(docInfo);
        });
        return docInfos;
    }

    /**
     *  分页查询某个栏目下的资讯
     * @param page 分页查询
     * @param userId  登录用户id
     * @param columnId 栏目id
     * @return
     */
    public List<DocInfo> docInfos(Page page,Long userId,Long columnId){
        if(!userCustomDocColumnService.ifCustomColumnId(userId,columnId)){
            return Collections.EMPTY_LIST;
        }
        PageList<DocInfo> pageList =   this.getDocInfos(page, Arrays.asList(columnId), null);
        return pageList.getList();
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
        List<Long> childColumnIds = new ArrayList<>();
        parentColumnIds.forEach(parentColumnId->{
            List<Long> columnIds = userCustomDocColumnService.getCustomColumnIds(userId,parentColumnId);
            if(columnIds.size()>0){
                childColumnIds.addAll(columnIds);
            }
        });


        if(childColumnIds.size()==0){
            return Collections.EMPTY_LIST;
        }
        PageList<DocInfo> pageList =   this.getDocInfos(page, childColumnIds, keyWorld);
        return pageList.getList();
    }

    /**
     * 分页查询多个栏目下的资讯
     * @param page
     * @param columnIds  多个栏目的id
     * @param keyworld 搜索词，如果为空，则查询所有
     * @return
     */
    public PageList<DocInfo> getDocInfos(Page page,List<Long> columnIds,String keyworld){
        Map<String, Object> param = new HashMap<>();
        String sql = " from DocInfo where  columnId in (:columnId) ";
        param.put("columnId",columnIds);
        if(StringUtils.isNotEmpty(keyworld)){
            sql = sql + " and title like :title ";
            param.put("title","%"+keyworld+"%");
        }
        sql = sql + " order by updateTime desc ";

        PageList<DocInfo> pageList = this.pageList(page,sql,param);
        List<Long> sourceIds = new ArrayList<>();
        pageList.getList().forEach(docInfo -> {
            sourceIds.add(docInfo.getSourceId());
            parseImgUrlOfDocInfo(docInfo);
        });
        Iterable<DocSource> docSources = docSourceService.findAll(sourceIds);
        pageList.getList().forEach(docInfo->{
            if(docInfo.getSourceId()!=null){
                docSources.forEach(docSource->{
                    if(docInfo.getSourceId().equals(docSource.getId())){
                        docInfo.setSourceName(docSource.getName());
                    }
                });
            }

        });
        return pageList;
    }
    private void parseImgUrlOfDocInfo(DocInfo docInfo){
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
        userViewHistoryService.save(docInfo,userId);
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
        docInfos.forEach(docInfo->{
            parseImgUrlOfDocInfo(docInfo);
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
        List<DocInfo> docInfosList = new ArrayList<>();
        docInfos.forEach(docInfo->{
            parseImgUrlOfDocInfo(docInfo);
            for(UserStore userStore:userStores) {
                if (userStore.getDocInfoId().equals(docInfo.getId())) {
                    docInfo.setViewTime(userStore.getCreateTime());
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
