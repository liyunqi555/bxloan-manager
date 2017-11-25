package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.Page;
import com.coamctech.bxloan.manager.common.PageList;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.DocInfoDao;
import com.coamctech.bxloan.manager.dao.UserDocColumnRelDao;
import com.coamctech.bxloan.manager.domain.DocInfo;
import com.coamctech.bxloan.manager.domain.UserDocColumnRel;
import com.coamctech.bxloan.manager.domain.UserViewHistory;
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


    public List<DocInfo> getTopDocInfos(Long parentColumnId,Integer topCount){

        List<Long> columnIds = docColumnService.getChildColumnIdsByParentId(parentColumnId);
        List<DocInfo> docInfos = docInfoDao.findByColumnIdInOrderByIfTopDescUpdateTimeDesc(columnIds, topCount);
        docInfos.forEach(docInfo -> {
            String body = docInfo.getBody();
            Elements elements = Jsoup.parse(body, "UTF-8").select("img[src]");
            String imgUrl = elements.attr("src");
            docInfo.setImgUrl(imgUrl);
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
        if(!"".equals(keyworld)){
            sql = sql + " and title like :title ";
            param.put("title","%"+keyworld+"%");
        }
        sql = sql + " order by updateTime desc ";

        PageList<DocInfo> pageList = this.pageList(page,sql,param);
        pageList.getList().forEach(docInfo -> {
            String body = docInfo.getBody();
            Elements elements = Jsoup.parse(body, "UTF-8").select("img[src]");
            String imgUrl = elements.attr("src");
            docInfo.setImgUrl(imgUrl);
        });
        return pageList;
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
}
