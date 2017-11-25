package com.coamctech.bxloan.manager.dao;


import com.coamctech.bxloan.manager.domain.Device;
import com.coamctech.bxloan.manager.domain.UserViewHistory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserViewHistoryDao extends JpaSpecificationExecutor<UserViewHistory>,PagingAndSortingRepository<UserViewHistory,Long> {
    UserViewHistory findByDocInfoIdAndUserId(Long docInfoId,Long userId);
}
