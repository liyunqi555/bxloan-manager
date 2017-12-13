package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.DynamicQuery.DynamicQuery;
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
import com.coamctech.bxloan.manager.service.VO.RoleVO;
import com.coamctech.bxloan.manager.utils.CopyUtils;
import com.coamctech.bxloan.manager.utils.Encodes;
import com.coamctech.bxloan.manager.utils.StringUtils;
import com.coamctech.bxloan.manager.utils.TokenUtils;
import com.coamctech.bxloan.manager.utils.encrypt.Digests;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/12.
 */
@Service
@Transactional
public class FeedBackService extends BaseService<FeedBack,Long> {
    @Autowired
    private FeedBackDao feedBackDao;
    @Autowired
    private DynamicQuery dynamicQuery;

    public JsonResult save(User user,String content) {
        FeedBack feedBack = new FeedBack();
        feedBack.setTitle(user.getNickName()+"的意见反馈");
        feedBack.setUserId(user.getId());
        feedBack.setContent(content);
        feedBack.setUpdateTime(new Date());
        feedBack.setCreateTime(new Date());
        feedBackDao.save(feedBack);
        return JsonResult.success();
    }
    public Page<FeedBack> findBySearch(Integer pageNumber, Integer pageSize,
                                     String roleName) throws ParseException {
        List<Object> params = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT r.id,r.title,r.content,r.user_id,r.create_time,t.user_name ");
        sql.append(" FROM t_feed_back r ,t_user t WHERE t.id = r.user_id ");
        int i = 0;
        if(org.apache.commons.lang3.StringUtils.isNotBlank(roleName)){
            sql.append(" AND r.content like ?").append(++i);
            params.add(org.apache.commons.lang3.StringUtils.join("%", roleName, "%"));
        }
        sql.append(" order by r.create_time desc ");
        Page<Object[]> page = dynamicQuery.nativeQuery(Object[].class,
                new PageRequest(pageNumber, pageSize), sql.toString(),params.toArray());
        List<FeedBack> returnList = Lists.newArrayList(Lists.transform(
                page.getContent(), new Function<Object[], FeedBack>() {
                    @Override
                    public FeedBack apply(Object[] objs) {
                        FeedBack vo = new FeedBack(objs);
                        return vo;
                    }
                }));
        Page<FeedBack> resultPage = new PageImpl<FeedBack>(returnList, new PageRequest(pageNumber, pageSize), page.getTotalElements());
        return resultPage;
    }

}