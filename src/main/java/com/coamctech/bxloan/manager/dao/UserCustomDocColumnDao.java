package com.coamctech.bxloan.manager.dao;


import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.UserCustomDocColumn;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCustomDocColumnDao extends JpaSpecificationExecutor<UserCustomDocColumn>,PagingAndSortingRepository<UserCustomDocColumn,Long> {
}
