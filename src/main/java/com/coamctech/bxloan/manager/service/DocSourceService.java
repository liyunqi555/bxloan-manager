package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.dao.DocColumnDao;
import com.coamctech.bxloan.manager.dao.DocSourceDao;
import com.coamctech.bxloan.manager.dao.UserDocSourceRelDao;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.DocSource;
import com.coamctech.bxloan.manager.domain.UserDocSourceRel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/12.
 */
@Service
@Transactional
public class DocSourceService extends BaseService<DocSource,Long>{
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DocSourceDao docSourceDao;
    @Autowired
    private UserDocSourceRelDao userDocSourceRelDao;

    public List<Long> getCanVisitDocSourceIds(Long userId){
        List<UserDocSourceRel> list = userDocSourceRelDao.findByUserId(userId);
        List<Long> ids = new ArrayList<>();
        list.forEach(s->{
            ids.add(s.getDocSourceId());
        });
        return ids;
    }


}
