package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.Page;
import com.coamctech.bxloan.manager.common.PageList;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.DeviceDao;
import com.coamctech.bxloan.manager.dao.UserDao;
import com.coamctech.bxloan.manager.dao.UserStoreDao;
import com.coamctech.bxloan.manager.domain.*;
import com.coamctech.bxloan.manager.utils.CopyUtils;
import com.coamctech.bxloan.manager.utils.Encodes;
import com.coamctech.bxloan.manager.utils.StringUtils;
import com.coamctech.bxloan.manager.utils.TokenUtils;
import com.coamctech.bxloan.manager.utils.encrypt.Digests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/12.
 */
@Service
@Transactional
public class UserStoreService extends BaseService<UserStore,Long>{
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserStoreDao userStoreDao;
    @Autowired
    private DocInfoService docInfoService;
    @Autowired
    private DocColumnService docColumnService;
    public JsonResult store(Long userId,Long docInfoId){
        if(docInfoId==null || userId==null){
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,ResultCode.PARAM_ERROR_MSG);
        }
        UserStore userStore = userStoreDao.findByUserIdAndDocInfoId(userId, docInfoId);
        if(userStore!=null){
            return JsonResult.success();
        }
        DocInfo docInfo = docInfoService.findOne(docInfoId);
        if(docInfo==null){
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,ResultCode.PARAM_ERROR_MSG);
        }
        DocColumn docColumn = docColumnService.findOne(docInfo.getColumnId());
        userStore = new UserStore();
        userStore.setUserId(userId);
        userStore.setDocInfoId(docInfoId);
        userStore.setCreateTime(new Date());
        userStore.setDocColumnId(docColumn.getId());
        userStore.setDocColumnParentId(docColumn.getParentId());
        userStoreDao.save(userStore);
        return JsonResult.success();
    }

    /**
     * 收藏数据中心数据
     * @param userId
     * @param conceptUri
     * @param entityId
     * @return
     */
    public JsonResult storeData(Long userId,String conceptUri,String entityId){
        if(StringUtils.isBlank(conceptUri) || StringUtils.isBlank(entityId)){
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,ResultCode.PARAM_ERROR_MSG);
        }
        UserStore userStore = userStoreDao.findByUserIdAndConceptUriAndEntityId(userId, conceptUri,entityId);
        if(userStore!=null){
            return JsonResult.success();
        }
        userStore = new UserStore();
        userStore.setUserId(userId);
        userStore.setCreateTime(new Date());
        userStore.setConceptUri(conceptUri);
        userStore.setEntityId(entityId);
        userStoreDao.save(userStore);
        return JsonResult.success();
    }
    public JsonResult cancelStore(Long userId,List<Long> ids){
        if(ids==null || ids.size()==0){
            return new JsonResult(ResultCode.PARAM_ERROR_CODE,ResultCode.PARAM_ERROR_MSG);
        }
        List<UserStore> userStores = userStoreDao.findByUserIdAndIdIn(userId, ids);
        if(userStores.size()==0){
            return JsonResult.success();
        }
        userStoreDao.delete(userStores);
        return JsonResult.success();
    }
    public List<UserStore> pageUserStore(Page page , Long userId,Long docColumnParentId){
        Map<String, Object> param = new HashMap<>();
        String sql = " from UserStore where  userId=:userId and docColumnParentId=:docColumnParentId";
        param.put("userId",userId);
        param.put("docColumnParentId",docColumnParentId);
        sql = sql + " order by createTime desc ";

        PageList<UserStore> pageList = this.pageList(page,sql,param);

        return pageList.getList();
    }
    public List<UserStore> pageUserStoreData(Page page , Long userId){
        Map<String, Object> param = new HashMap<>();
        String sql = " from UserStore where  userId=:userId and docColumnParentId is null ";
        param.put("userId",userId);
        sql = sql + " order by createTime desc ";

        PageList<UserStore> pageList = this.pageList(page,sql,param);

        return pageList.getList();
    }
    public List<UserStore> findByUserIdAndDocInfoIds(Long userId,List<Long> docInfoIds){
        return userStoreDao.findByUserIdAndDocInfoIdIn(userId,docInfoIds);
    }
}
