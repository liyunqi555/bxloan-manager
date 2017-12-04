package com.coamctech.bxloan.manager.service;

import com.alibaba.fastjson.JSONReader;
import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.DocColumnDao;
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
import java.util.function.Consumer;

/**
 * Created by Administrator on 2017/11/12.
 */
@Service
@Transactional
public class UserCustomDocColumnService extends BaseService<UserCustomDocColumn,Long>{
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserCustomDocColumnDao userCustomDocColumnDao;
    @Autowired
    private UserDocColumnOrderService userDocColumnOrderService;
    @Autowired
    private DocColumnService docColumnService;
    @Autowired
    private RoleUserRelService roleUserRelService;
    @Autowired
    private RoleDocColumnService roleDocColumnService;

    /**
     * 根据一级栏目查询某用户已定制的下级栏目id
     * @param userId
     * @param docColumnParentId
     * @return
     */
    public List<Long> getCustomColumnIds(Long userId,Long docColumnParentId){
        List<UserCustomDocColumn> list = userCustomDocColumnDao.findByUserIdAndDocColumnParentId(userId,docColumnParentId);
        List<Long> childColumns = new ArrayList<>();
        list.forEach(userCustomDocColumn->{childColumns.add(userCustomDocColumn.getDocColumnId());});
        return childColumns;
    }
    /**
     * 根据一级栏目查询某用户已定制的下级栏目id
     * @param userId
     * @param docColumnParentId
     * @return
     */
    public List<UserCustomDocColumn> getCustomColumns(Long userId,Long docColumnParentId){
        List<UserCustomDocColumn> list = userCustomDocColumnDao.findByUserIdAndDocColumnParentId(userId,docColumnParentId);
        return list;
    }
    public List<UserCustomDocColumn> getCustomColumns(Long userId,List<Long> docColumnParentIds){
        List<UserCustomDocColumn> list = userCustomDocColumnDao.findByUserIdAndDocColumnParentIdIn(userId,docColumnParentIds);
        return list;
    }
    /**
     * 某用户是否已经定制某栏目
     * @param userId
     * @param docColumnId
     * @return
     */
    public boolean ifCustomColumnId(Long userId,Long docColumnId,Long docColumnParentId){
        UserCustomDocColumn userCustomDocColumn = userCustomDocColumnDao.
                findByUserIdAndDocColumnIdAndDocColumnParentId(userId, docColumnId, docColumnParentId);
        return userCustomDocColumn!=null;
    }
    /**
     * 某用户是否已经定制某栏目
     * @param userId
     * @param docColumnId
     * @return
     */
    public boolean ifCustomColumnId(Long userId,Long docColumnId){
        UserCustomDocColumn userCustomDocColumn = userCustomDocColumnDao.findByUserIdAndDocColumnId(userId, docColumnId);
        return userCustomDocColumn!=null;
    }
    /**
     * 订阅某个栏目
     * @param userId
     * @param customColumnId
     * @return
     */
    public JsonResult customColumn(Long userId,Long customColumnId){
        if(customColumnId==null){
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,"该栏目不存在");
        }
        if(ifCustomColumnId(userId,customColumnId)){
            return new JsonResult(ResultCode.ERROR_CODE_701,"该栏目已订阅");
        }
        DocColumn docColumn = docColumnService.findOne(customColumnId);
        if(docColumn==null){
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,"该栏目不存在");
        }

        UserCustomDocColumn userCustomDocColumn = new UserCustomDocColumn();
        userCustomDocColumn.setDocColumnId(customColumnId);
        userCustomDocColumn.setCreateTime(new Date());
        Map<String,Object> params = new HashMap<>();
        params.put("userId",userId);
        userCustomDocColumn.setDocColumnParentId(docColumn.getParentId());
        userCustomDocColumn.setUserId(userId);
        userCustomDocColumnDao.save(userCustomDocColumn);
        userDocColumnOrderService.save(userId,docColumn.getId());
        return new JsonResult(ResultCode.SUCCESS_CODE,ResultCode.SUCCESS_MSG);
    }
    /**
     * 订阅某个栏目
     * @param userId
     * @param customColumnId
     * @return
     */
    public JsonResult cancelCustomColumn(Long userId,Long customColumnId){
        if(customColumnId==null){
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,"该栏目不存在");
        }
        UserCustomDocColumn delUserCustomDocColumn = userCustomDocColumnDao.findByUserIdAndDocColumnId(userId, customColumnId);
        if(delUserCustomDocColumn==null){
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,"该栏目未订阅");
        }
        userCustomDocColumnDao.delete(delUserCustomDocColumn);

        userDocColumnOrderService.deleteByUserIdAndDocColumnId(userId,customColumnId);
        return new JsonResult(ResultCode.SUCCESS_CODE,ResultCode.SUCCESS_MSG);
    }
}
