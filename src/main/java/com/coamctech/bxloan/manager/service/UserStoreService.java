package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.DeviceDao;
import com.coamctech.bxloan.manager.dao.UserDao;
import com.coamctech.bxloan.manager.dao.UserStoreDao;
import com.coamctech.bxloan.manager.domain.Device;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.domain.UserStore;
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

/**
 * Created by Administrator on 2017/11/12.
 */
@Service
@Transactional
public class UserStoreService extends BaseService<UserStore,Long>{
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserStoreDao userStoreDao;

    public JsonResult store(Long userId,Long docInfoId){

        return JsonResult.success();
    }
}
