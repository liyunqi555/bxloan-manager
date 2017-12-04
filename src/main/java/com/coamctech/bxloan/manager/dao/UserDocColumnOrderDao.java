package com.coamctech.bxloan.manager.dao;


import com.coamctech.bxloan.manager.domain.UserCustomDocColumn;
import com.coamctech.bxloan.manager.domain.UserDocColumnOrder;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDocColumnOrderDao extends JpaSpecificationExecutor<UserDocColumnOrder>,PagingAndSortingRepository<UserDocColumnOrder,Long> {

    long countByUserId(Long userId);
    UserDocColumnOrder findByUserIdAndDocColumnId(Long userId,Long docColumnId);
    @Query(value = "select max(custom_order) from user_doc_column_order t where t.user_id=?1",nativeQuery = true)
    int getMaxCustomOrderByUserId(Long userId);

    long deleteByUserIdAndDocColumnId(Long userId,Long docColumnId);

}
