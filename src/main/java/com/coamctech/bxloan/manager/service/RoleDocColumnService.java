package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.RoleDocColumnRelDao;
import com.coamctech.bxloan.manager.dao.UserCustomDocColumnDao;
import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.RoleDocColumnRel;
import com.coamctech.bxloan.manager.domain.UserCustomDocColumn;
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
public class RoleDocColumnService extends BaseService<RoleDocColumnRel,Long>{
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RoleDocColumnRelDao roleDocColumnRelDao;

    /**
     * 根据角色查询所有栏目id
     * @param roleIds
     * @return
     */
    public List<Long> getColumnIds(List<Long> roleIds){
        List<RoleDocColumnRel> list = roleDocColumnRelDao.findByRoleIdIn(roleIds);
        List<Long> columnIds = new ArrayList<>();
        list.forEach(userCustomDocColumn->{
            columnIds.add(userCustomDocColumn.getDocColumnId());
        });
        return columnIds;
    }
    /**
     * 在已知栏目集合里查询与角色关联的栏目id集合
     * @param roleIds
     * @return
     */
    public List<Long> getColumnIds(List<Long> roleIds,List<Long> columnIds){
        List<RoleDocColumnRel> list = roleDocColumnRelDao.findByRoleIdInAndDocColumnIdIn(roleIds, columnIds);
        List<Long> ids = new ArrayList<>();
        list.forEach(roleDocColumnRel->{
            ids.add(roleDocColumnRel.getDocColumnId());
        });
        return columnIds;
    }

}
