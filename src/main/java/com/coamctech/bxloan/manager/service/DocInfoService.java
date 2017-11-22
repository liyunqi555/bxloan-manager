package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.Page;
import com.coamctech.bxloan.manager.common.PageList;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.DocInfoDao;
import com.coamctech.bxloan.manager.dao.UserDocColumnRelDao;
import com.coamctech.bxloan.manager.domain.DocInfo;
import com.coamctech.bxloan.manager.domain.UserDocColumnRel;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    /**
     * 获取首页资讯
     * 首先从用户定制的栏目中查询
     * 如果定制的栏目为空，则从该用户被分配的栏目中查询
     * @param userId
     * @param parentColumnId
     * @return
     */
    public PageList<DocInfo> homeDocInfos(Page page,Long userId,Long parentColumnId){
        List<Long> columnIds = userCustomDocColumnService.getCustomColumnIds(userId,parentColumnId);

        if(columnIds.size()==0){
            List<Long> allChildColumnIds = this.docColumnService.getChildColumnIdsByParentId(parentColumnId);
            List<UserDocColumnRel> userDocColumnRels = userDocColumnRelDao.findByUserIdAndDocColumnIdIn(userId, allChildColumnIds);
            userDocColumnRels.forEach(userDocColumnRel->{columnIds.add(userDocColumnRel.getDocColumnId());});
        }
        return this.getDocInfos(page, columnIds, null);
    }
    /**
     * 搜索关键字
     * 从用户定制的栏目中查询
     * @param userId
     * @return
     */
    public PageList<DocInfo> searchDocInfos(Page page,Long userId,Long parentColumnId,String keyWorld){
        List<Long> columnIds = userCustomDocColumnService.getCustomColumnIds(userId,parentColumnId);

        if(columnIds.size()==0){
            List<Long> allChildColumnIds = this.docColumnService.getChildColumnIdsByParentId(parentColumnId);
            List<UserDocColumnRel> userDocColumnRels = userDocColumnRelDao.findByUserIdAndDocColumnIdIn(userId, allChildColumnIds);
            userDocColumnRels.forEach(userDocColumnRel->{columnIds.add(userDocColumnRel.getDocColumnId());});
        }
        return this.getDocInfos(page, columnIds, null);
    }
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
                Elements elements = Jsoup.parse(body,"UTF-8").select("img[src]");
                String imgUrl = elements.attr("src");
                docInfo.setImgUrl(imgUrl);
        });
        return pageList;
    }
    public JsonResult articleDetail(Long id){
        DocInfo docInfo = docInfoDao.findOne(id);
        return new JsonResult(ResultCode.SUCCESS_CODE,ResultCode.SUCCESS_MSG, docInfo);
    }
}
