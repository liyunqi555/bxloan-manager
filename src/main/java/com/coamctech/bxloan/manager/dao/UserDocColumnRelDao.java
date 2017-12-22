package com.coamctech.bxloan.manager.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.coamctech.bxloan.manager.domain.UserDocColumnRel;

@Repository
public interface UserDocColumnRelDao extends JpaSpecificationExecutor<UserDocColumnRel>,PagingAndSortingRepository<UserDocColumnRel,Long> {
    List<UserDocColumnRel> findByUserIdAndDocColumnId(Long userId, Long docColumnId);
    List<UserDocColumnRel> findByUserIdAndDocColumnIdIn(Long userId, List<Long> docColumnIds);
	@Modifying
	@Query(value = "delete from UserDocColumnRel where userId=?1")
	public void deleteRelByUserId(Long userId);

	@Query(value="select docColumnId from  UserDocColumnRel where userId=?1")
	public List<Long> findColumnIdsByUserId(Long userId);
	
	@Modifying
	@Query(value = "delete from UserDocColumnRel where docColumnId=?1")
	public void deleteRelByDocColumnId(Long docColumnId);

}

