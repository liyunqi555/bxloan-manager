package com.coamctech.bxloan.manager.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.coamctech.bxloan.manager.domain.Role;

public interface RoleDao extends JpaSpecificationExecutor<Role>,PagingAndSortingRepository<Role,Long> {

}
