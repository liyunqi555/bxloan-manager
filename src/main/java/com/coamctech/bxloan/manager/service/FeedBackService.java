package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.ResultCode;
import com.coamctech.bxloan.manager.dao.AppConfigDao;
import com.coamctech.bxloan.manager.dao.DeviceDao;
import com.coamctech.bxloan.manager.dao.FeedBackDao;
import com.coamctech.bxloan.manager.dao.UserDao;
import com.coamctech.bxloan.manager.domain.AppConfig;
import com.coamctech.bxloan.manager.domain.Device;
import com.coamctech.bxloan.manager.domain.FeedBack;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.utils.CopyUtils;
import com.coamctech.bxloan.manager.utils.Encodes;
import com.coamctech.bxloan.manager.utils.StringUtils;
import com.coamctech.bxloan.manager.utils.TokenUtils;
import com.coamctech.bxloan.manager.utils.encrypt.Digests;
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
public class FeedBackService extends BaseService<FeedBack,Long> {
    @Autowired
    private FeedBackDao feedBackDao;

    public JsonResult save(User user,String content) {
        FeedBack feedBack = new FeedBack();
        feedBack.setTitle(user.getUserName()+"的意见反馈");
        feedBack.setUserId(user.getId());
        feedBack.setContent(content);
        feedBack.setUpdateTime(new Date());
        feedBack.setCreateTime(new Date());
        feedBackDao.save(feedBack);
        return JsonResult.success();
    }

}