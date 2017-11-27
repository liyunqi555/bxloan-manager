package com.coamctech.bxloan.manager.dao;


import com.coamctech.bxloan.manager.domain.UserCustomDocColumn;
import com.coamctech.bxloan.manager.domain.UserDocColumnRel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDocColumnRelDao extends JpaSpecificationExecutor<UserDocColumnRel>,PagingAndSortingRepository<UserDocColumnRel,Long> {
    List<UserDocColumnRel> findByUserIdAndDocColumnId(Long userId, Long docColumnId);
    List<UserDocColumnRel> findByUserIdAndDocColumnIdIn(Long userId, List<Long> docColumnIds);
	@Modifying
	@Query(value = "delete from UserDocColumnRel where userId=?1")
	public void deleteRelByUserId(Long userId);

	@Query(value="select docColumnId from  UserDocColumnRel where userId=?1")
	public List<Long> findColumnIdsByUserId(Long userId);
}

