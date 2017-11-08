package com.coamctech.bxloan.manager.dao;


import com.coamctech.bxloan.manager.domain.TLOrder;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TLOrderDao  extends JpaSpecificationExecutor<TLOrder>,PagingAndSortingRepository<TLOrder,Long> {
}
