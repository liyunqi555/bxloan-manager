package com.coamctech.bxloan.manager.dao;

import com.coamctech.bxloan.manager.domain.FeedBack;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackDao extends JpaSpecificationExecutor<FeedBack>,PagingAndSortingRepository<FeedBack,Long> {

}
