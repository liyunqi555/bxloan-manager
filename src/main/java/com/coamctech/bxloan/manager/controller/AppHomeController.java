package com.coamctech.bxloan.manager.controller;

import com.alibaba.fastjson.JSONReader;
import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.Page;
import com.coamctech.bxloan.manager.common.PageList;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.DocInfo;
import com.coamctech.bxloan.manager.service.*;
import com.coamctech.bxloan.manager.utils.StringUtils;
import com.coamctech.bxloan.manager.utils.TokenUtils;
import com.coamctech.bxloan.manager.utils.encrypt.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 *
 * Created by Administrator on 2017/10/20.
 */
@RestController
@RequestMapping(value="api/app/home",method = RequestMethod.POST)
public class AppHomeController extends AppBaseController{
    @Autowired
    private UserService userService;
    @Autowired
    private DocInfoService docInfoService;
    @Autowired
    private DocColumnService docColumnService;
    @Autowired
    private UserCustomDocColumnService userCustomDocColumnService;
    @Autowired
    private UserStoreService userStoreService;
    @Autowired
    private UserViewHistoryService userViewHistoryService;


    @Value("${top.level.column.id.news}")
    private Long topLevelColumnIdNews;
    @Value("${top.level.column.id.doc}")
    private Long topLevelColumnIdDoc;
    @Value("${top.level.column.id.report}")
    private Long topLevelColumnIdReport;

    @Value("${app.home.banner.count}")
    private Integer appHomeBannerCount;

    @RequestMapping("topColumns")
    public JsonResult topColumns(){
        Map<String,Long> topColumns = new HashMap<>();
        topColumns.put("topLevelColumnIdNews",topLevelColumnIdNews);
        topColumns.put("topLevelColumnIdDoc",topLevelColumnIdDoc);
        topColumns.put("topLevelColumnIdReport",topLevelColumnIdReport);
        JsonResult jsonResult = JsonResult.success();
        jsonResult.setBody(topColumns);
        return jsonResult;
    }
    /**
     * 首页bannner
     * @return
     */
    @RequestMapping("banner")
    public JsonResult banner(){
        JsonResult jsonResult = JsonResult.success();
        List<DocInfo> docInfos = docInfoService.getTopDocInfos(this.topLevelColumnIdDoc,this.appHomeBannerCount);
        jsonResult.setBody(docInfos);
        return jsonResult;
    }

    /**
     * 首页智汇天下
     * 展示智库报告的内容
     * @param pageIndex based zero
     * @return
     */
    @RequestMapping("worlds")
    public JsonResult worlds(@RequestParam(name="pageIndex",defaultValue ="0") Integer pageIndex){
        JsonResult jsonResult = JsonResult.success();
        Long userId = TokenUtils.sessionUser().getId();
        Page page = new Page(pageIndex,DEFAULT_PAGE_SIZE);
        List<DocInfo> docInfoList = docInfoService.searchDocInfos(page, userId, Arrays.asList(this.topLevelColumnIdDoc), null);
        jsonResult.setBody(docInfoList);
        return  jsonResult;
    }

    /**
     * 搜索
     * @param pageIndex 第几页
     * @param keyword 搜索关键词
     * @param topLevelColumnId 搜索栏目
     * @return
     */
    @RequestMapping("search")
    public JsonResult search(@RequestParam(name="pageIndex",defaultValue ="0") Integer pageIndex
            ,@RequestParam(name="keyword",defaultValue = "天赋") String keyword
            ,@RequestParam(name="topLevelColumnId",required = false) Long topLevelColumnId){
        JsonResult jsonResult = JsonResult.success();
        Long userId = TokenUtils.sessionUser().getId();
        Page page = new Page(pageIndex,DEFAULT_PAGE_SIZE);
        List<Long> parentColumnIds = new ArrayList<>();
        if(topLevelColumnId==null){
            parentColumnIds.add(topLevelColumnIdDoc);
            parentColumnIds.add(topLevelColumnIdNews);
            parentColumnIds.add(topLevelColumnIdReport);
        }else{
            parentColumnIds.add(topLevelColumnId);
        }
        List<DocInfo> docInfos = this.docInfoService.searchDocInfos(page, userId, parentColumnIds, keyword);
        jsonResult.setBody(docInfos);
        return jsonResult;
    }
    /**
     *文章详情
     * @param docInfoId 文章ID
     * @return
     */
    @RequestMapping("docInfoDetail")
    public JsonResult docInfoDetail(@RequestParam(name="docInfoId") Long docInfoId){
        Long userId = TokenUtils.sessionUser().getId();
        return docInfoService.articleDetail(docInfoId,userId);
    }

    /**
     * 资讯列表
     * @param pageIndex
     * @param columnId 栏目id
     * @param topLevelColumnId 上级栏目id
     * @return
     */
    @RequestMapping("docInfos")
    public JsonResult docInfos(@RequestParam(name="pageIndex",defaultValue ="0") Integer pageIndex,
                               @RequestParam(name="columnId") Long columnId,
                               @RequestParam(name="topLevelColumnId") Long topLevelColumnId){
        Long userId = TokenUtils.sessionUser().getId();
        Page page = new Page(pageIndex,DEFAULT_PAGE_SIZE);
        if(columnId!=null){//二级栏目不为null 直接查询二级栏目的资讯
            List<DocInfo> docInfos = this.docInfoService.docInfos(page, userId, columnId);
            return JsonResult.success(docInfos);
        }else{
            List<DocInfo> docInfos = this.docInfoService.searchDocInfos(page, userId, Arrays.asList(topLevelColumnId), null);
            return JsonResult.success(docInfos);
        }
    }

    /**
     * 未订阅栏目
     * @param topLevelColumnId
     * @return
     */
    @RequestMapping("noCustomDocColumns")
    public JsonResult noCustomDocColumns(@RequestParam(name = "topLevelColumnId") Long topLevelColumnId){
        Long userId = TokenUtils.sessionUser().getId();
        Iterable<DocColumn> docColumns = this.docColumnService.getNoCustomColumns(userId, topLevelColumnId);
        return new JsonResult(ResultCode.SUCCESS_CODE,ResultCode.SUCCESS_MSG, docColumns);
    }


    /**
     * 已订阅栏目
     * @param topLevelColumnId
     * @return
     */
    @RequestMapping("haveCustomDocColumns")
    public JsonResult haveCustomDocColumns(@RequestParam(name = "topLevelColumnId") Long topLevelColumnId){
        Long userId = TokenUtils.sessionUser().getId();
        Iterable<DocColumn> docColumns = this.docColumnService.getCustomColumns(userId,topLevelColumnId);
        return new JsonResult(ResultCode.SUCCESS_CODE,ResultCode.SUCCESS_MSG, docColumns);
    }
    /**
     * 订阅某个栏目
     * @param columnId
     * @return
     */
    @RequestMapping("customColumn")
    public JsonResult customColumn(@RequestParam(name = "columnId")Long columnId){
        long userId = TokenUtils.sessionUser().getId();
        return this.userCustomDocColumnService.customColumn(userId, columnId);
    }
    /**
     * 取消订阅某个栏目
     * @param columnId
     * @return
     */
    @RequestMapping("cancelCustomColumn")
    public JsonResult cancelCustomColumn(@RequestParam(name = "columnId",defaultValue ="5")Long columnId){
        long userId = TokenUtils.sessionUser().getId();
        return this.userCustomDocColumnService.cancelCustomColumn(userId, columnId);
    }

    /**
     * 收藏
     * @param docInfoId
     * @return
     */
    @RequestMapping("store")
    public JsonResult store(@RequestParam(name = "docInfoId") Long docInfoId){
        long userId = TokenUtils.sessionUser().getId();
        return userStoreService.store(userId,docInfoId);
    }

    /**
     * 取消收藏
     * @param docInfoIdStr 逗号隔开的资讯id
     * @return
     */
    @RequestMapping("cancelStore")
    public JsonResult cancelStore(@RequestParam(name = "docInfoIdStr") String docInfoIdStr){
        if(StringUtils.isBlank(docInfoIdStr)){
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,ResultCode.PARAM_ERROR_MSG);
        }
        String[] arr = docInfoIdStr.split(COMMA);
        if(arr.length==0){
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,ResultCode.PARAM_ERROR_MSG);
        }
        List<Long> docInfoIds = new ArrayList<>();
        try{
            List<String> ids = Arrays.asList(arr);
            ids.forEach(id->{
                docInfoIds.add(Long.valueOf(id));
            });
        }catch (Exception e){
            logger.error("取消收藏参数错误，docInfoIdStr={}",docInfoIdStr);
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,ResultCode.PARAM_ERROR_MSG);
        }
        long userId = TokenUtils.sessionUser().getId();
        return userStoreService.cancelStore(userId,docInfoIds);
    }

    /**
     * 我的历史
     * @param pageIndex
     * @return
     */
    @RequestMapping("myHistory")
    public JsonResult myHistory(@RequestParam(name="pageIndex",defaultValue ="0") Integer pageIndex){
        long userId = TokenUtils.sessionUser().getId();
        Page page = new Page(pageIndex,DEFAULT_PAGE_SIZE);
       List<DocInfo> docInfos = docInfoService.myHistory(page,userId);
        return JsonResult.success(docInfos);
    }
    /**
     * 我的收藏
     * @param pageIndex
     * @return
     */
    @RequestMapping("myStore")
    public JsonResult myStore(@RequestParam(name="pageIndex",defaultValue ="0") Integer pageIndex
            ,@RequestParam(name="topLevelColumnId",required = false) Long topLevelColumnId){
        long userId = TokenUtils.sessionUser().getId();
        Page page = new Page(pageIndex,DEFAULT_PAGE_SIZE);
        List<DocInfo> docInfos = docInfoService.myStore(page, userId,topLevelColumnId);
        return JsonResult.success(docInfos);
    }
}
