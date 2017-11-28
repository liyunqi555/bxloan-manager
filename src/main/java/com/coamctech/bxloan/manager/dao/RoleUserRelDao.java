package com.coamctech.bxloan.manager.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.coamctech.bxloan.manager.domain.Role;
import com.coamctech.bxloan.manager.domain.RoleUserRel;

public interface RoleUserRelDao extends JpaSpecificationExecutor<RoleUserRel>,PagingAndSortingRepository<RoleUserRel,Long>{
	//查找某一用户的所有角色
	@Query(value="select ro.* from role ro,role_user_rel rur where rur.role_id = ro.id and rur.user_id = ?1",nativeQuery=true)
	public List<Role> getRoleByUserId(Long userId);
	
	//根据用户id删除用户角色关联关系
	@Modifying
	@Query(value="delete from RoleUserRel where userId = ?1")
	public void deleteByUserId(Long userId);
	
	//根据角色id删除用户角色关联关系
	@Modifying
	@Query(value="delete from RoleUserRel where roleId = ?1")
	public void deleteByRoleId(Long roleId);
	
	@Query(value ="select roleId from RoleUserRel where userId = ?1")
	public List<Long> findRoleIdsByUserId(Long userId);
	
	//查找某一角色下的所有用户id
	@Query(value="select u.id from user u,role_user_rel rur where rur.user_id = u.id and rur.role_id = ?1",nativeQuery=true)
	public List<Long> findUserIdsByRoleId(Long roleId);

	
}
