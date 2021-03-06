package com.coamctech.bxloan.manager.controller;

import com.alibaba.fastjson.JSONArray;
import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.Page;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.DocInfo;
import com.coamctech.bxloan.manager.service.*;
import com.coamctech.bxloan.manager.utils.StringUtils;
import com.coamctech.bxloan.manager.utils.TokenUtils;
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
        List<DocInfo> docInfos = docInfoService.getTopDocInfos();
        docInfoService.parseImgUrl(docInfos);
        docInfos.forEach(docInfo -> {
            docInfo.setBody("");
            docInfo.setCnBoty("");
        });
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
    public JsonResult worlds(@RequestParam(name="pageIndex") Integer pageIndex){
        JsonResult jsonResult = JsonResult.success();
        Long userId = TokenUtils.sessionUser().getId();
        Page page = new Page(pageIndex,DEFAULT_PAGE_SIZE);
        List<DocInfo> docInfoList = docInfoService.searchDocInfos(page, userId, Arrays.asList(this.topLevelColumnIdDoc), null);
        docInfoService.parseImgUrl(docInfoList);
        docInfoList.forEach(d->{
            d.setBody("");
            d.setCnBoty("");
        });
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
            ,@RequestParam(name="keyword") String keyword
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
        docInfoService.parseImgUrl(docInfos);
        docInfos.forEach(d->{
            d.setBody("");
            d.setCnBoty("");
        });
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
                               @RequestParam(name="columnId",required = false) Long columnId,
                               @RequestParam(name="topLevelColumnId") Long topLevelColumnId){
        Long userId = TokenUtils.sessionUser().getId();
        Page page = new Page(pageIndex,DEFAULT_PAGE_SIZE);
        List<DocInfo> docInfos = new ArrayList<>();
        if(columnId!=null){//二级栏目不为null 直接查询二级栏目的资讯
            docInfos = this.docInfoService.docInfos(page, userId, columnId,topLevelColumnId);
        }else{
            docInfos = this.docInfoService.searchDocInfos(page, userId, Arrays.asList(topLevelColumnId), null);
        }
        docInfoService.parseImgUrl(docInfos);
        docInfos.forEach(d->{
            d.setBody("");
            d.setCnBoty("");
        });
        return JsonResult.success(docInfos);
    }

    /**
     * 未订阅栏目
     * @param topLevelColumnId
     * @return
     */
    @RequestMapping("noCustomDocColumns")
    public JsonResult noCustomDocColumns(@RequestParam(name = "topLevelColumnId",required = false) Long topLevelColumnId){
        Long userId = TokenUtils.sessionUser().getId();
        List<Long> parentDocCulumnIds = new ArrayList<>();
        if(topLevelColumnId!=null){
            parentDocCulumnIds.add(topLevelColumnId);
        }else{
            parentDocCulumnIds.add(topLevelColumnIdNews);
            parentDocCulumnIds.add(topLevelColumnIdDoc);
        }
        List<DocColumn> docColumns = this.docColumnService.getNoCustomColumns(userId,parentDocCulumnIds);
        return new JsonResult(ResultCode.SUCCESS_CODE,ResultCode.SUCCESS_MSG, docColumns);

    }


    /**
     * 已订阅栏目
     * @param topLevelColumnId
     * @return
     */
    @RequestMapping("haveCustomDocColumns")
    public JsonResult haveCustomDocColumns(@RequestParam(name = "topLevelColumnId",required = false) Long topLevelColumnId){
        Long userId = TokenUtils.sessionUser().getId();
        List<Long> parentDocCulumnIds = new ArrayList<>();
        if(topLevelColumnId!=null){
            parentDocCulumnIds.add(topLevelColumnId);
        }else{
            parentDocCulumnIds.add(topLevelColumnIdNews);
            parentDocCulumnIds.add(topLevelColumnIdDoc);
        }
        JSONArray docColumns = this.docColumnService.getCustomColumns(userId,parentDocCulumnIds);
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
    public JsonResult cancelCustomColumn(@RequestParam(name = "columnId")Long columnId){
        long userId = TokenUtils.sessionUser().getId();
        return this.userCustomDocColumnService.cancelCustomColumn(userId, columnId);
    }

    /**
     * 收藏
     * @param docInfoId
     * @return
     */
    @RequestMapping("store")
    public JsonResult store(
            @RequestParam(name = "docInfoId") Long docInfoId,
            @RequestParam(name = "topLevelColumnId") Long topLevelColumnId){
        long userId = TokenUtils.sessionUser().getId();
        return userStoreService.store(userId,docInfoId,topLevelColumnId);
    }

    /**
     * 取消收藏
     * @param idsStr 逗号隔开的收藏id
     * @return
     */
    @RequestMapping("cancelStore")
    public JsonResult cancelStore(@RequestParam(name = "idsStr") String idsStr){
        if(StringUtils.isBlank(idsStr)){
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,ResultCode.PARAM_ERROR_MSG);
        }
        String[] arr = idsStr.split(COMMA);
        if(arr.length==0){
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,ResultCode.PARAM_ERROR_MSG);
        }
        List<Long> longList = new ArrayList<>();
        try{
            List<String> stringList = Arrays.asList(arr);
            stringList.forEach(id->{
                longList.add(Long.valueOf(id));
            });
        }catch (Exception e){
            logger.error("取消收藏参数错误，docInfoIdStr={}",idsStr);
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,ResultCode.PARAM_ERROR_MSG);
        }
        long userId = TokenUtils.sessionUser().getId();
        return userStoreService.cancelStore(userId,longList);
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
        docInfoService.parseImgUrl(docInfos);
        return JsonResult.success(docInfos);
    }
    /**
     * 我的收藏
     * @param pageIndex
     * @return
     */
    @RequestMapping("myStore")
    public JsonResult myStore(@RequestParam(name="pageIndex",defaultValue ="0") Integer pageIndex
            ,@RequestParam(name="topLevelColumnId") Long topLevelColumnId){
        long userId = TokenUtils.sessionUser().getId();
        Page page = new Page(pageIndex,DEFAULT_PAGE_SIZE);
        List<DocInfo> docInfos = docInfoService.myStore(page, userId,topLevelColumnId);
        docInfoService.parseImgUrl(docInfos);
        return JsonResult.success(docInfos);
    }

    /**
     * 栏目排序
     * @param customColumnIdOne
     * @param customColumnIdTwo
     * @return
     */
    @RequestMapping("switchOrder")
    public JsonResult switchOrder(@RequestParam(name = "customColumnIdOne")Long customColumnIdOne,
                                  @RequestParam(name = "customColumnIdTwo")Long customColumnIdTwo){
        Long userId = TokenUtils.sessionUser().getId();
        return userCustomDocColumnService.switchOrder(userId, customColumnIdOne, customColumnIdTwo);
    }
}
