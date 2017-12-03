package com.coamctech.bxloan.manager.controller;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.Page;
import com.coamctech.bxloan.manager.service.DataCenterService;
import com.coamctech.bxloan.manager.service.UserStoreService;
import com.coamctech.bxloan.manager.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 *  数据中心
 * Created by Administrator on 2017/10/20.
 */
@Controller
@RequestMapping(value="api/app/files",method = RequestMethod.GET)
public class AppFileController extends AppBaseController{

    @Autowired
    private DataCenterService dataCenterService;
    @Autowired
    private UserStoreService userStoreService;

    @RequestMapping("img")
    public void img(@RequestParam(name="mediaId") Long mediaId,HttpServletResponse response){
        List<byte[]> list = dataCenterService.getMedia(mediaId);
        writeResponse(response,list);
    }
    private void writeResponse(HttpServletResponse response, List<byte[]> list){
        response.reset();
        OutputStream toClient = null ;
        Long fileSize = 0L;
        for(int i=0;i<list.size();i++){
            fileSize+=list.get(i).length;
        }
        try {
            // 设置response的Header
            //response.addHeader("Content-Disposition", "attachment;filename=" + new String("cc".getBytes())+".jpg");
            response.addHeader("Content-Length", "" + fileSize);
            toClient = new BufferedOutputStream(response.getOutputStream());
            //response.setContentType("application/octet-stream");
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
