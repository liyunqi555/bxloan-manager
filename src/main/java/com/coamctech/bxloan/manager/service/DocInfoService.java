package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.Page;
import com.coamctech.bxloan.manager.common.PageList;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.DocInfoDao;
import com.coamctech.bxloan.manager.domain.DocInfo;
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

    public JsonResult banner(){
        List<DocInfo> docInfos = docInfoDao.findFirst6BySourceId(1L,new Sort(Sort.Direction.DESC, "createTime"));
        for(DocInfo di: docInfos){
            String body = di.getBody();
            Elements elements = Jsoup.parse(body,"UTF-8").select("img[src]");
            String imgUrl = elements.attr("src");
            di.setImgUrl(imgUrl);
        }
        return new JsonResult(ResultCode.SUCCESS_CODE,ResultCode.SUCCESS_MSG, docInfos);
    }
    public PageList<DocInfo> news(Page page,Long columnId,String keyworld){
        String sql = " from DocInfo where  columnId=:columnId and title like :title order by createTime desc ";
        Map<String, Object> param = new HashMap<>();
        param.put("columnId",columnId);
        param.put("title","%"+keyworld+"%");

        PageList<DocInfo> pageList = this.pageList(page,sql,param);
        pageList.getList().forEach(new Consumer<DocInfo>() {
            @Override
            public void accept(DocInfo docInfo) {
                String body = docInfo.getBody();
                Elements elements = Jsoup.parse(body,"UTF-8").select("img[src]");
                String imgUrl = elements.attr("src");
                docInfo.setImgUrl(imgUrl);
            }
        });
        return pageList;
    }
    public JsonResult articleDetail(Long id){
        DocInfo docInfo = docInfoDao.findOne(id);
        return new JsonResult(ResultCode.SUCCESS_CODE,ResultCode.SUCCESS_MSG, docInfo);
    }
}
