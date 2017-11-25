package com.coamctech.bxloan.manager.dao;


import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.DocInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DocColumnDao extends JpaSpecificationExecutor<DocColumn>,PagingAndSortingRepository<DocColumn,Long> {
    List<DocColumn> findByParentId(Long parentId);
    List<DocColumn> findByParentIdAndIdNotIn(Long parentId,Collection<Long> ids);
}
