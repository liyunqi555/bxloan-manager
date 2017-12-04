package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.dao.RoleDocColumnRelDao;
import com.coamctech.bxloan.manager.dao.RoleUserRelDao;
import com.coamctech.bxloan.manager.domain.RoleDocColumnRel;
import com.coamctech.bxloan.manager.domain.RoleUserRel;
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
public class RoleUserRelService extends BaseService<RoleUserRel,Long>{
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RoleUserRelDao roleUserRelDao;

    /**
     * 根据一级栏目查询某用户已定制的下级栏目id
     * @param userId
     * @return
     */
    public List<Long> getRoleIds(Long  userId){
        List<Long> list = roleUserRelDao.findRoleIdsByUserId(userId);
        return list;
    }

}
