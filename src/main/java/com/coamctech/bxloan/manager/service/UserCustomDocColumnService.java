package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.DocColumnDao;
import com.coamctech.bxloan.manager.dao.UserCustomDocColumnDao;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.UserCustomDocColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    public List<Long> getCustomColumnIds(Long userId,Long docColumnParentId){
        List<UserCustomDocColumn> list = userCustomDocColumnDao.findByUserIdAndDocColumnParentId(userId,docColumnParentId);
        List<Long> childColumns = new ArrayList<>();
        list.forEach(userCustomDocColumn->{childColumns.add(userCustomDocColumn.getDocColumnId());});
        return childColumns;
    }
}
