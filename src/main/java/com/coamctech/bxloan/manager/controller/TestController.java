package com.coamctech.bxloan.manager.controller;

import com.coamctech.bxloan.manager.dao.TLOrderDao;
import com.coamctech.bxloan.manager.domain.TLOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.util.Random;
import java.util.Set;

/**
 * Created by Administrator on 2017/10/20.
 */
@RestController
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    @Autowired
    private TLOrderDao tlOrderDao;

   // @Autowired
   // private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/")
    public Page<TLOrder> home(HttpSession session){
        logger.info("1--------------------");
        session.setAttribute("userId",new Random().nextLong());

        Page<TLOrder> page = tlOrderDao.findAll(new PageRequest(1, 2));
        logger.info("page={}",page);
      //  Set<String> keys = stringRedisTemplate.keys("spring:session:*");
        return page;
       // return page.getContent();
//        return "hello world"+page.getTotalElements();
    }
}
