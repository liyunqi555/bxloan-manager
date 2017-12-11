package com.coamctech.bxloan.manager.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.coamctech.bxloan.manager.domain.RoleDocSourceRel;

public interface RoleDocSourceRelDao extends JpaSpecificationExecutor<RoleDocSourceRel>,PagingAndSortingRepository<RoleDocSourceRel,Long>{
	@Modifying
	@Query("delete from RoleDocSourceRel where roleId=?1")
	public void deleteRelByRoleId(Long roleId);
    @Modifying
    @Query("delete from RoleDocSourceRel where docSourceId=?1")
    public void deleteRelByDocSourceId(Long docSourceId);
	
	@Query("select docSourceId from RoleDocSourceRel where roleId=?1")
	public List<Long> findSourceIdsByRoleId(Long roleId);
	
}
