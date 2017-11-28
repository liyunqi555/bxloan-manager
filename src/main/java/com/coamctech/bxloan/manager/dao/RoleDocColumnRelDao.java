package com.coamctech.bxloan.manager.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.coamctech.bxloan.manager.domain.RoleDocColumnRel;

public interface RoleDocColumnRelDao extends JpaSpecificationExecutor<RoleDocColumnRel>,PagingAndSortingRepository<RoleDocColumnRel,Long> {
	
	@Modifying
	@Query(value="delete from RoleDocColumnRel where userId=?1")
	public void deleteRelByRoleId(Long roleId);
	
}
