package com.coamctech.bxloan.manager.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.coamctech.bxloan.manager.domain.DocColumn;
import com.coamctech.bxloan.manager.domain.DocColumnDocSourceRel;

/**   
 * 类名称：DocColumnDocSourceRelDao<br/>
 * 类描述 ：<br/>
 * 创建人: zhaoqingwen<br/>
 * 创建时间：2017年12月27日 下午9:26:01<br/>  
 * 修改人：
 * 修改时间：  
 * 修改备注：
 * 版本： V1.0
 */
@Repository
public interface DocColumnDocSourceRelDao extends JpaSpecificationExecutor<DocColumnDocSourceRel>,PagingAndSortingRepository<DocColumnDocSourceRel,Long> {
	@Query(value="select docSourceId from  DocColumnDocSourceRel where docColumnId=?1")
	public List<Long> findSourceIdsByDocColumnId(Long userId);

}
