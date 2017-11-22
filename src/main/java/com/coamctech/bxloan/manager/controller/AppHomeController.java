package com.coamctech.bxloan.manager.controller;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.Page;
import com.coamctech.bxloan.manager.common.PageList;
import com.coamctech.bxloan.manager.domain.DocInfo;
import com.coamctech.bxloan.manager.service.DocColumnService;
import com.coamctech.bxloan.manager.service.DocInfoService;
import com.coamctech.bxloan.manager.service.UserService;
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

/**
 *
 * Created by Administrator on 2017/10/20.
 */
@RestController
@RequestMapping(value="api/app/home",method = RequestMethod.GET)
public class AppHomeController {
    private static final Logger logger = LoggerFactory.getLogger(AppHomeController.class);
    private static final Integer DEFAULT_PAGE_SIZE = 10;
    @Autowired
    private UserService userService;
    @Autowired
    private DocInfoService docInfoService;
    @Autowired
    private DocColumnService docColumnService;

    @Value("${top.level.column.id.news}")
    private Long topLevelColumnIdNews;
    @Value("${top.level.column.id.doc}")
    private Long topLevelColumnIdDoc;
    @Value("${top.level.column.id.report}")
    private Long topLevelColumnIdReport;

    @Value("${app.home.banner.count}")
    private Integer appHomeBannerCount;

    /**
     * 首页bannner
     * @return
     */
    @RequestMapping("banner")
    public JsonResult banner(){
        JsonResult jsonResult = JsonResult.success();
         Long userId = TokenUtils.sessionUser().getId();
        Page page = new Page(0,appHomeBannerCount);
        PageList<DocInfo> pageList = docInfoService.homeDocInfos(page, userId, this.topLevelColumnIdDoc);
        jsonResult.setBody(pageList.getList());
        return jsonResult;
    }

    /**
     * 首页智汇天下
     * @param pageIndex based zero
     * @return
     */
    @RequestMapping("worlds")
    public JsonResult worlds(@RequestParam(name="pageIndex",defaultValue ="0") Integer pageIndex){
        JsonResult jsonResult = JsonResult.success();
        Long userId = TokenUtils.sessionUser().getId();
        Page page = new Page(pageIndex,DEFAULT_PAGE_SIZE);
        PageList<DocInfo> pageList = docInfoService.homeDocInfos(page, userId, this.topLevelColumnIdDoc);
        jsonResult.setBody(pageList.getList());
        return jsonResult;
    }

    /**
     * 文章详情
     * @param id 文章ID
     * @return
     */
    @RequestMapping("docInfoDetail")
    public JsonResult docInfoDetail(@RequestParam(name="id") Long id){
        return docInfoService.articleDetail(id);
    }

    /**
     * 搜索
     * @param keyword 搜索关键字
     * @return
     */
    @RequestMapping("search")
    public JsonResult search(@RequestParam(name="pageIndex",defaultValue ="0") Integer pageIndex
            ,@RequestParam(name="keyword",defaultValue = "天赋") String keyword
            ,@RequestParam(name="parentColumnId",required = false) Long parentColumnId){
        JsonResult jsonResult = JsonResult.success();
        Long userId = TokenUtils.sessionUser().getId();
        Page page = new Page(pageIndex,DEFAULT_PAGE_SIZE);
        PageList<DocInfo> pageList = this.docInfoService.searchDocInfos(page,userId,parentColumnId, keyword);
        jsonResult.setBody(pageList.getList());
        return jsonResult;
    }

    @RequestMapping("news")
    public JsonResult news(@RequestParam(name="pageIndex",defaultValue ="0") Integer pageIndex
            ,@RequestParam(name="keyword",defaultValue = "天赋") String keyword,Long columnId){
        JsonResult jsonResult = JsonResult.success();
        Long userId = TokenUtils.sessionUser().getId();
        Page page = new Page(pageIndex,DEFAULT_PAGE_SIZE);
        PageList<DocInfo> pageList = this.docInfoService.searchDocInfos(page, userId,1L, keyword);
        jsonResult.setBody(pageList.getList());
        return jsonResult;
    }

    /**
     * 未订阅栏目列表
     * @param columnId
     * @return
     */
    @RequestMapping("noCustomColumns")
    public JsonResult noCustomColumns(@RequestParam(name = "columnId",defaultValue ="1")Long columnId){
        JsonResult jsonResult = JsonResult.success();
        return this.docColumnService.columns(columnId);
    }


    /**
     * 已订阅栏目
     * @param columnId
     * @return
     */
    @RequestMapping("haveCustomColumns")
    public JsonResult haveCustomColumns(@RequestParam(name = "columnId",defaultValue ="5")Long columnId){
        JsonResult jsonResult = JsonResult.success();
        return this.docColumnService.columns(columnId);
    }
    /**
     * 订阅某个栏目
     * @param columnId
     * @return
     */
    @RequestMapping("customColumn")
    public JsonResult customColumn(@RequestParam(name = "columnId",defaultValue ="5")Long columnId){
        long userId = TokenUtils.sessionUser().getId();
        return this.docColumnService.customColumn(userId,columnId);
    }
    /**
     * 取消订阅某个栏目
     * @param columnId
     * @return
     */
    @RequestMapping("cancelCustomColumn")
    public JsonResult cancelCustomColumn(@RequestParam(name = "columnId",defaultValue ="5")Long columnId){
        JsonResult jsonResult = JsonResult.success();
        return this.docColumnService.columns(columnId);
    }

}
