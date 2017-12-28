package com.coamctech.bxloan.manager.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.coamctech.bxloan.manager.domain.DocInfo;

@Repository
public interface DocInfoDao extends JpaSpecificationExecutor<DocInfo>,PagingAndSortingRepository<DocInfo,Long> {
    List<DocInfo> findFirst6ByOrderByIfTopDescUpdateTimeDesc();
    //List<DocInfo> findByColumnId(Long columnIds);
}
