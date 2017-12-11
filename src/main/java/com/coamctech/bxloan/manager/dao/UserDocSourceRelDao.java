package com.coamctech.bxloan.manager.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.coamctech.bxloan.manager.domain.UserDocSourceRel;

public interface UserDocSourceRelDao extends JpaSpecificationExecutor<UserDocSourceRel>,PagingAndSortingRepository<UserDocSourceRel,Long>{
    List<UserDocSourceRel> findByUserId(Long userId);
	@Modifying
	@Query("delete from UserDocSourceRel where userId=?1")
	public void deleteRelByUserId(Long userId);
    @Modifying
    @Query("delete from UserDocSourceRel where docSourceId=?1")
    public void deleteRelByDocSourceId(Long docSourceId);
	
	@Query("select docSourceId from UserDocSourceRel where userId=?1")
	public List<Long> findSourceIdsByUserId(Long userId);
}
