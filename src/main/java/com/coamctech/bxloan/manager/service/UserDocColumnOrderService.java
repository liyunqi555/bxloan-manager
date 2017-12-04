package com.coamctech.bxloan.manager.service;

import com.alibaba.fastjson.JSONReader;
import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.UserCustomDocColumnDao;
import com.coamctech.bxloan.manager.dao.UserDocColumnOrderDao;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.UserCustomDocColumn;
import com.coamctech.bxloan.manager.domain.UserDocColumnOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by Administrator on 2017/11/12.
 */
@Service
@Transactional
public class UserDocColumnOrderService extends BaseService<UserDocColumnOrder,Long>{
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserDocColumnOrderDao userDocColumnOrderDao;
    /**
     * 调整订阅的栏目的顺序
     * @param userId
     * @return
     */
    public JsonResult save(Long userId,Long docolumnId){
        if(userId==null ||docolumnId==null){
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,"参数错误");
        }
        UserDocColumnOrder userDocColumnOrder = userDocColumnOrderDao.findByUserIdAndDocColumnId(userId,docolumnId);
        if(userDocColumnOrder!=null){
            return JsonResult.success();
        }
        userDocColumnOrder = new UserDocColumnOrder();
        userDocColumnOrder.setDocColumnId(docolumnId);
        userDocColumnOrder.setCreateTime(new Date());
        userDocColumnOrder.setUserId(userId);
        int count = userDocColumnOrderDao.getMaxCustomOrderByUserId(userId);
        userDocColumnOrder.setCustomOrder(count+1);
        return new JsonResult(ResultCode.SUCCESS_CODE,ResultCode.SUCCESS_MSG);
    }
    public JsonResult deleteByUserIdAndDocColumnId(Long userId,Long docolumnId){
        if(userId==null ||docolumnId==null){
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,"参数错误");
        }
        long count = userDocColumnOrderDao.deleteByUserIdAndDocColumnId(userId,docolumnId);
        if(count==0){
            return JsonResult.error();
        }
        return JsonResult.success();
    }

    /**
     * 调整栏目展示顺序
     * @param userId
     * @param columnId1
     * @param columnId2
     * @return
     */
    public JsonResult switchOrder(Long userId,Long columnId1,Long columnId2){
        if(columnId1==null ||columnId2==null){
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,"该栏目不存在");
        }
        UserDocColumnOrder userDocColumnOrderOne = userDocColumnOrderDao.findByUserIdAndDocColumnId(userId, columnId1);
        if(userDocColumnOrderOne==null){
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,"该栏目顺序有问题");
        }
        UserDocColumnOrder userDocColumnOrderTwo = userDocColumnOrderDao.findByUserIdAndDocColumnId(userId, columnId2);
        if(userDocColumnOrderTwo==null){
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,"该栏目顺序有问题");
        }
        Integer customOrderOne = userDocColumnOrderOne.getCustomOrder();
        Integer customOrderTwo = userDocColumnOrderTwo.getCustomOrder();
        userDocColumnOrderOne.setCustomOrder(customOrderTwo);
        userDocColumnOrderTwo.setCustomOrder(customOrderOne);

        userDocColumnOrderDao.save(userDocColumnOrderOne);
        userDocColumnOrderDao.save(userDocColumnOrderTwo);
        return new JsonResult(ResultCode.SUCCESS_CODE,ResultCode.SUCCESS_MSG);
    }
}
