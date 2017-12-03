package com.coamctech.bxloan.manager.dao;


import com.coamctech.bxloan.manager.domain.AppConfig;
import com.coamctech.bxloan.manager.domain.Device;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppConfigDao extends JpaSpecificationExecutor<AppConfig>,PagingAndSortingRepository<AppConfig,Long> {
    AppConfig findTopByOrderByCreateTimeDesc();
}
