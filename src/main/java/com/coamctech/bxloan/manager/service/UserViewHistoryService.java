package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.DocColumnDao;
import com.coamctech.bxloan.manager.dao.UserCustomDocColumnDao;
import com.coamctech.bxloan.manager.dao.UserViewHistoryDao;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.DocInfo;
import com.coamctech.bxloan.manager.domain.UserCustomDocColumn;
import com.coamctech.bxloan.manager.domain.UserViewHistory;
import com.coamctech.bxloan.manager.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/12.
 */
@Service
@Transactional
public class UserViewHistoryService extends BaseService<UserViewHistory,Long>{
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserViewHistoryDao userViewHistoryDao;
    @Autowired
    private DocColumnService docColumnService;

    public boolean save(DocInfo docInfo,Long userId){
        UserViewHistory userViewHistory = userViewHistoryDao.findByDocInfoIdAndUserId(docInfo.getId(), userId);
        if(userViewHistory==null){
            userViewHistory = new UserViewHistory();
            userViewHistory.setUserId(userId);
            userViewHistory.setDocInfoId(docInfo.getId());
            userViewHistory.setCreateTime(new Date());
        }
        userViewHistory.setUpdateTime(new Date());
        DocColumn docColumn = docColumnService.findOne(docInfo.getColumnId());
        userViewHistory.setDocColumnId(docColumn.getId());
        userViewHistory.setDocColumnParentId(docColumn.getParentId());
        userViewHistoryDao.save(userViewHistory);
        return true;
    }

}
