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

    /**
     * 首页bannner
     * @return
     */
    @RequestMapping("banner")
    public JsonResult banner(){
        return docInfoService.banner();
    }

    /**
     * 首页智汇天下
     * @return
     */
    @RequestMapping("world")
    public JsonResult world(){
        return docInfoService.banner();
    }

    /**
     * 文章详情
     * @param id 文章ID
     * @return
     */
    @RequestMapping("articleDetail")
    public JsonResult articleDetail(@RequestParam(name="id",defaultValue = "1") Long id){
        return docInfoService.articleDetail(id);
    }

    /**
     * 搜索
     * @param keyword 搜索关键字
     * @return
     */
    @RequestMapping("search")
    public JsonResult search(@RequestParam(name="keyword",defaultValue = "天赋") String keyword){
        JsonResult jsonResult = JsonResult.success();
        Page page = new Page(0,DEFAULT_PAGE_SIZE);
        PageList<DocInfo> pageList = this.docInfoService.news(page, 1L, keyword);
        jsonResult.setBody(pageList.getList());
        return jsonResult;
    }

    @RequestMapping("news")
    public JsonResult news(@RequestParam(name="pageIndex",defaultValue ="1") Integer pageIndex
            ,@RequestParam(name="keyword",defaultValue = "天赋") String keyword,Long columnId){
        JsonResult jsonResult = JsonResult.success();
        Page page = new Page(pageIndex,DEFAULT_PAGE_SIZE);
        PageList<DocInfo> pageList = this.docInfoService.news(page, 1L, keyword);
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
        JsonResult jsonResult = JsonResult.success();
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
