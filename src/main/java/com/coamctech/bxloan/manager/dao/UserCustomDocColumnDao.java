package com.coamctech.bxloan.manager.dao;


import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.UserCustomDocColumn;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserCustomDocColumnDao extends JpaSpecificationExecutor<UserCustomDocColumn>,PagingAndSortingRepository<UserCustomDocColumn,Long> {

    List<UserCustomDocColumn> findByUserIdAndDocColumnParentId(Long userId,Long docColumnParentId);
    List<UserCustomDocColumn> findByUserIdAndDocColumnParentIdIn(Long userId,Collection<Long> docColumnParentIds);
    List<UserCustomDocColumn> findByUserId(Long userId);

    UserCustomDocColumn findByUserIdAndDocColumnId(Long userId,Long docColumnId);
    UserCustomDocColumn findByUserIdAndDocColumnIdAndDocColumnParentId(Long userId,Long docColumnId,Long docColumnParentId);
    @Query(value = "select max(t.custom_order) from t_user_custom_doc_column t where t.user_id=?1",nativeQuery = true)
    Integer getMaxCustomOrderByUserId(Long userId);

}
