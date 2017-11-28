package com.coamctech.bxloan.manager.dao;


import com.coamctech.bxloan.manager.domain.DocInfo;
import com.coamctech.bxloan.manager.domain.DocSource;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DocSourceDao extends JpaSpecificationExecutor<DocSource>,PagingAndSortingRepository<DocSource,Long> {
}
