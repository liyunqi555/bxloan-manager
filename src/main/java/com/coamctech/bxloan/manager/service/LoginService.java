package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.domain.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by Administrator on 2017/11/12.
 */
@Service
@Transactional
public class LoginService {

    public boolean login(User user){
        return true;
    }
}
