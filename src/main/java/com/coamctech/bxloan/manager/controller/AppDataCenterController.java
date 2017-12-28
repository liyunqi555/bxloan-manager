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

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

    /**
     * 数据中心首页
     * @param conceptUri
     * @return
     */
    @RequestMapping("home")
    public JsonResult home(@RequestParam(name="pageIndex")Integer pageIndex,@RequestParam(name="conceptUri")String conceptUri,String propertyUri){
        if(StringUtils.isBlank(propertyUri)){
            return dataCenterService.entityList(pageIndex,1000,conceptUri);
        }else{
            return dataCenterService.entityList(pageIndex,1000,conceptUri,propertyUri);
        }
    }

    /**
     *
     * @param conceptUri
     * @param entityid
     * @return
     */
    @RequestMapping("detail")
    public JsonResult detail(@RequestParam(name="conceptUri")String conceptUri
            ,@RequestParam(name="entityid") String entityid){
        long userId = TokenUtils.sessionUser().getId();
        return dataCenterService.detail(userId,conceptUri, entityid);
    }

    /**
     * 收藏
     * @param conceptUri
     * @param entityid
     * @return
     */

    @RequestMapping("store")
    public JsonResult store(@RequestParam(name="conceptUri")String conceptUri
            ,@RequestParam(name="entityid")String entityid){
        long userId = TokenUtils.sessionUser().getId();
        return userStoreService.storeData(userId,conceptUri, entityid);
    }

    /**
     * 我的收藏
     * @return
     */

    @RequestMapping("myStore")
    public JsonResult myStore(@RequestParam(name="pageIndex",defaultValue ="0") Integer pageIndex){
        long userId = TokenUtils.sessionUser().getId();
        Page page = new Page(pageIndex,DEFAULT_PAGE_SIZE);
        return dataCenterService.myStore(page,userId);
    }
    /**
     * 我的收藏
     * @return
     */

    @RequestMapping("file")
    public void file(@RequestParam(name="mediaId") Long mediaId,HttpServletResponse response){
        List<byte[]> list = dataCenterService.getMedia(mediaId);
        response.reset();
        Long fileSize = 0L;
        for(int i=0;i<list.size();i++){
            fileSize+=list.get(i).length;
        }
        OutputStream toClient = null ;
        try {
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String("中国".getBytes()));
            response.addHeader("Content-Length", "" + fileSize);
            toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            for(byte[] arr:list) {
                toClient.write(arr);
            }
            toClient.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(toClient!=null){
                try {
                    toClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
