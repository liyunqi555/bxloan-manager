package com.coamctech.bxloan.manager.controller;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.Page;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.DocInfo;
import com.coamctech.bxloan.manager.service.*;
import com.coamctech.bxloan.manager.utils.StringUtils;
import com.coamctech.bxloan.manager.utils.TokenUtils;
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
 *  数据中心
 * Created by Administrator on 2017/10/20.
 */
@RestController
@RequestMapping(value="api/app/dataCenter",method = RequestMethod.POST)
public class AppDataCenterController extends AppBaseController{

    @Autowired
    private DataCenterService dataCenterService;
    @Autowired
    private UserStoreService userStoreService;
    @RequestMapping("home")
    public JsonResult home(String conceptUri){
        return dataCenterService.entityList(conceptUri);
    }
    @RequestMapping("detail")
    public JsonResult detail(String conceptUri,String entityId){
        return dataCenterService.detail(conceptUri, entityId);
    }

    /**
     * 收藏
     * @param conceptUri
     * @param entityId
     * @return
     */

    @RequestMapping("store")
    public JsonResult store(String conceptUri,String entityId){
        long userId = TokenUtils.sessionUser().getId();
        return userStoreService.storeData(userId,conceptUri, entityId);
    }

    /**
     * 我的收藏
     * @param conceptUri
     * @param entityId
     * @return
     */

    @RequestMapping("myStore")
    public JsonResult myStore(@RequestParam(name="pageIndex",defaultValue ="0") Integer pageIndex){
        long userId = TokenUtils.sessionUser().getId();
        Page page = new Page(pageIndex,DEFAULT_PAGE_SIZE);
        return dataCenterService.myStore(page,userId);
    }
}
