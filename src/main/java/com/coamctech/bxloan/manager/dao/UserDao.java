package com.coamctech.bxloan.manager.dao;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.coamctech.bxloan.manager.domain.User;

@Repository
public interface UserDao extends JpaSpecificationExecutor<User>,PagingAndSortingRepository<User,Long> {
    User findByUserName(String userName);
}
